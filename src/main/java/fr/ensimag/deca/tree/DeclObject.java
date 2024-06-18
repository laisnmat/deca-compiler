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

        // Symbol b = new Symbol("boolean");
        // Identifier returnType = new Identifier(b);

        // Symbol name = new Symbol("equals");
        // Identifier methodName = new Identifier(name);

        // ListDeclParam params = new ListDeclParam();

        // Symbol o = new Symbol("Object");
        // Identifier objectType = new Identifier(o);

        // Symbol paramSym = new Symbol("obj");
        // Identifier paramName = new Identifier(paramSym);

        // DeclParam p = new DeclParam(o, paramName);

        // MethodBody body = new MethodBody(null, null);

        // params.add(p);

        // DeclMethod equalsMethod = new DeclMethod(returnType, methodName);
        // this.methods.add();

        // methods = new ListDeclMethod();

        // Identifier returnType = new
        // Identifier(compiler.symbolTable.create("boolean"));
        // Identifier methodName = new
        // Identifier(compiler.symbolTable.create("equals"));
        // ListDeclParam methodParam = new ListDeclParam();
        // Identifier otherType = new Identifier(compiler.symbolTable.create("Object"));
        // Identifier otherIdent = new Identifier(compiler.symbolTable.create("o"));
        // methodParam.add(new DeclParam(otherType, otherIdent));

        // ListInst instEquals = new ListInst();

        // instEquals.add();

        // new PUSH(Register.getR(2));
        // new PUSH(Register.getR(3));
        // new LEA((DAddr) otherIdent.getDVal(), Register.getR(2));
        // RegisterOffset offset = new RegisterOffset(-2, Register.LB);
        // new LEA(offset, Register.getR(3));
        // new CMP(Register.getR(3), Register.getR(2));
        // Label returnCond = new Label("Object.Equals.true");
        // new BEQ(returnCond);

        // Label endLabel = new Label("fin.Object.Equals");
        // // label false
        // new OPP(new ImmediateInteger(0), Register.R0);
        // new BRA(endLabel);
        // // label true
        // compiler.addLabel(returnCond);
        // new OPP(new ImmediateInteger(1), Register.R0);
        // // end
        // compiler.addLabel(endLabel);
        // new POP(Register.getR(2));
        // new POP(Register.getR(3));
        // new RTS();

        // AbstractMethodBody mBody = new MethodBody(null, instEquals);
        // methods.add(new DeclMethod(returnType, methodName, methodParam, mBody));

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
}
