package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodAsmBody extends AbstractMethodBody {
    private StringLiteral asm;

    public MethodAsmBody(StringLiteral asm) {
        this.asm = asm;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
