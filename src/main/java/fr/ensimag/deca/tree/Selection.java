package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Selection extends AbstractLValue {
    private static final Logger LOG = Logger.getLogger(Selection.class);

    private AbstractExpr select;
    private AbstractIdentifier field;

    public Selection(AbstractExpr select, AbstractIdentifier field) {
        this.select = select;
        this.field = field;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        select.iter(f);
        field.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        select.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        select.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify Selection: start");
        select.verifyExpr(compiler, localEnv, currentClass);
        // (class(__, env_exp2), __) ≜ env_types(class2)
        if (!select.getType().isClass()) {
            throw new ContextualError("Incompatible Types: Selection is not possible on a non-class type",
                    getLocation());
        }
        ClassType class1;
        if (currentClass == null) {
            class1 = null;
        } else {
            class1 = currentClass.getType();
        }
        ClassType class2 = (ClassType) select.getType();
        Type classField = field.verifyFieldIdent(compiler, class2.getDefinition().getMembers(), currentClass);

        // subtype(env_types,type_class(class2),type_class(class))
        // subtype(env_types,type_class(class),type_class(class_field))
        if (field.getFieldDefinition().getVisibility() == Visibility.PROTECTED && (!class2.isSubClassOf(class1)
                || !class1.isSubClassOf(field.getFieldDefinition().getContainingClass().getType()))) {
            throw new ContextualError("Protected field " + field.getName() + " is not accessible from "
                    + (class1 == null ? "main" : class1.getName()), getLocation());
        }
        setType(classField);
        LOG.debug("verify Selection: end");
        return classField;
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        select.codeGenExp(compiler, register);
        int index = ((Identifier) this.field).getFieldDefinition().getIndex();
        RegisterOffset offset = new RegisterOffset(index, register);
        compiler.addInstruction(new LOAD(offset, register));
    }

    public void codeGenAssign(DecacCompiler compiler, GPRegister register) {
        select.codeGenExp(compiler, Register.R0);
        int index = ((Identifier) this.field).getFieldDefinition().getIndex();
        RegisterOffset offset = new RegisterOffset(index, Register.R0);
        compiler.addInstruction(new STORE(register, offset));
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        select.codeGenExp(compiler, Register.R0);
        int index = ((Identifier) this.field).getFieldDefinition().getIndex();
        RegisterOffset offset = new RegisterOffset(index, Register.R0);
        compiler.addInstruction(new LOAD(offset, Register.R0));
        compiler.addInstruction(new CMP(jumpOnTrue ? 1 : 0, Register.R0));
        compiler.addInstruction(new BEQ(label));
    }
}
