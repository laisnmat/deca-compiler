package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

/**
 * @author gl03
 * @date 14/04/2024
 */
public class DeclVar extends AbstractDeclVar {
    private static final Logger LOG = Logger.getLogger(DeclVar.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify DeclVar: start");
        // type verification
        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError(
                    "Incompatible Types: Variable "
                            + this.varName
                            + " is of type void.",
                    getLocation());
        }

        // create the definition of this variable
        VariableDefinition varDef = new VariableDefinition(type, getLocation());

        // put the definition of this variable into the current environment
        try {
            localEnv.declare(this.varName.getName(), varDef);
        } catch (DoubleDefException dde) {
            throw new ContextualError(
                    "DoubleDefException: Variable " + this.varName.getName()
                            + " is already defined in this environment.",
                    getLocation());
        }

        this.varName.setDefinition(varDef);

        // setting Definition and Type of this Identifier
        type = this.varName.verifyExpr(compiler, localEnv, currentClass);

        // verify the initialization
        this.initialization.verifyInitialization(compiler, type, localEnv, currentClass);
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler, ClassDefinition currentClass) {
        Register reg;
        int baseOffset;
        if (currentClass == null) {
            reg = Register.GB;
            baseOffset = compiler.stackManager.getVarGlobalCount() + 1;
            compiler.stackManager.incrementVarGlobalCount();
        } else {
            reg = Register.LB;
            baseOffset = compiler.stackManager.getVarLocalCount() + 1;
            compiler.stackManager.incrementVarLocalCount();
        }
        RegisterOffset offset = new RegisterOffset(baseOffset, reg);

        // put this variable in the next free space in memory
        this.varName.getExpDefinition().setOperand(offset);

        // if there is an Initialization,
        // then initialize variable using an Assign
        if (initialization instanceof Initialization) {
            Initialization i = (Initialization) this.initialization;
            new Assign(this.varName, i.getExpression()).codeGenInst(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.varName.decompile(s);
        this.initialization.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
