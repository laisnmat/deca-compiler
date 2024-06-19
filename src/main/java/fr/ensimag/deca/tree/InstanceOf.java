package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.InstanceOfLabel;
import fr.ensimag.deca.codegen.LabelIf;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class InstanceOf extends AbstractExpr {

    private AbstractExpr expression;
    private AbstractIdentifier type;

    public InstanceOf(AbstractExpr expression, AbstractIdentifier type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
        type.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expression.decompile(s);
        s.print(" instanceof ");
        type.decompile(s);
    }

    // @Override
    // protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
    // throw new UnsupportedOperationException("not yet implemented");
    // }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // (3.40)
        // expr ↓env_types ↓env_exp ↓class ↑type1
        Type type1 = expression.verifyExpr(compiler, localEnv, currentClass);
        // type ↓env_types ↑type2
        Type type2 = type.verifyType(compiler);
        // type := type_instanceof_op(type1, type2)
        typeInstanceOfOp(type1, type2);
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    protected void typeInstanceOfOp(Type t1, Type t2)
            throws ContextualError {
        if (!t1.isClassOrNull()) {
            throw new ContextualError(
                    "Incompatible Types: Cannot apply instanceOf operation because " + t1 + " is not a class or null",
                    getLocation());
        }
        if (!t2.isClassOrNull()) {
            throw new ContextualError(
                    "Incompatible Types: Cannot apply instanceOf operation because " + t2 + " is not a class or null",
                    getLocation());
        }
    }

    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {

        GPRegister register = Register.getR(2);
        if (expression.getType().isNull() || type.getType().isNull()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
            return;
        }
        if (type.getType().isObjectType()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
            return;
        }
        if (expression.getType().isObjectType()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
            return;
        }

        ClassType exprType = (ClassType) expression.getType();
        ClassType typeType = (ClassType) type.getType();

        InstanceOfLabel iOLabel = compiler.labelManager.getInstanceOfLabel();
        Label instanceStart = iOLabel.getStartLabel();
        Label instanceEnd = iOLabel.getEndLabel();
        LabelIf labelIf = compiler.labelManager.getIf();
        Label endLabel = labelIf.getEndLabel();
        Label elseLabel = labelIf.getElseLabel();

        RegisterOffset offset = new RegisterOffset(0, register);
        compiler.addComment("Starting InstanceOf code");
        compiler.addInstruction(new LOAD(exprType.getAddr(), register));

        compiler.addLabel(instanceStart);
        compiler.addInstruction(new CMP(typeType.getAddr(), register));
        compiler.addInstruction(new BEQ(endLabel));

        compiler.addInstruction(new LEA(compiler.environmentType.OBJECT.getAddr(), Register.getR(3)));
        compiler.addInstruction(new CMP(Register.getR(3), register));
        compiler.addInstruction(new BEQ(elseLabel));

        compiler.addInstruction(new LOAD(exprType.getAddr(), register));
        compiler.addInstruction(new LOAD(offset, register));
        compiler.addInstruction(new BRA(instanceStart));

        compiler.addLabel(endLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
        compiler.addInstruction(new BRA(instanceEnd));

        compiler.addLabel(elseLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));

        compiler.addLabel(instanceEnd);
        compiler.labelManager.removePrefix();
    }
}
