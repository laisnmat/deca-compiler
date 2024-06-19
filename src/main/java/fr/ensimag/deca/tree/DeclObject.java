package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclObject extends AbstractDeclClass {

    ListDeclMethod methods;

    private final Logger LOG = Logger.getLogger(DeclClass.class);

    public DeclObject() {
        super();

    }

    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // pass
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        // pass
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        // pass
    }

    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        RegisterOffset offset = new RegisterOffset(1, GPRegister.GB);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, offset));
        compiler.stackManager.incrementVarGlobalCount();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // pass
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // pass
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // pass
    }

    // public void initEquals(){
    // this.
    // }

    @Override
    protected void codegenMethods(DecacCompiler compiler) {
    }
}
