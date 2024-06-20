package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

public class New extends AbstractExpr {

    private AbstractIdentifier type;

    public New(AbstractIdentifier type) {
        this.type = type;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("New ");
        type.decompile(s);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        type.verifyType(compiler);
        if (!type.getType().isClass()) {
            throw new ContextualError(
                    "Incompatible Types: New is not possible on a non-class type",
                    getLocation());
        }
        setType(type.getType());
        return type.getType();

    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        int classMemorySize = ((ClassType) type.getType()).getDefinition().getNumberOfFields() + 1;
        compiler.addInstruction(
                new NEW(classMemorySize, register));
        compiler.addInstruction(new BOV(compiler.labelManager.getStackOverflowError()));
        compiler.addInstruction(new LEA(((ClassType) type.getType()).getAddr(), Register.R0));

        RegisterOffset offsetR = new RegisterOffset(0, register);
        compiler.addInstruction(new STORE(Register.R0, offsetR));

        compiler.addInstruction(new PUSH(register));
        compiler.stackManager.incrementPushCount();

        Label initLabel = type.getClassDefinition().getLabel();

        compiler.addInstruction(new BSR(initLabel));

        compiler.addInstruction(new POP(register));
        compiler.stackManager.decrementPushCount();
    }
}
