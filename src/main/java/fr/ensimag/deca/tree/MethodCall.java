package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

public class MethodCall extends AbstractExpr {

    private AbstractExpr classCall;
    private AbstractIdentifier method;
    private ListExpr listExpr; // params list

    public MethodCall(AbstractExpr classCall, AbstractIdentifier method, ListExpr listExpr) {
        this.classCall = classCall;
        this.method = method;
        this.listExpr = listExpr;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classCall.iter(f);
        method.iter(f);
        listExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classCall.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        listExpr.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        classCall.decompile(s);
        s.print(".");
        method.decompile(s);
        s.print("(");
        listExpr.decompile(s);
        s.print(")");
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        Collections.reverse(listExpr.getListModif());
        for (AbstractExpr exp : listExpr.getListModif()) {
            exp.codeGenExp(compiler, register);
            compiler.addInstruction(new PUSH(register));
        }
        classCall.codeGenExp(compiler, register);
        compiler.addInstruction(new PUSH(register));
        // voir table
        compiler.addInstruction(new BSR(method.getMethodDefinition().getLabel()));
        // subsp n'oublie le +1
        compiler.addInstruction(new SUBSP(listExpr.size() + 1));
    }

    public void verifySignature(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Signature signature) throws ContextualError {
        if (listExpr.getList().size() != signature.size()) {
            throw new ContextualError("MethodCall: Wrong number of arguments", getLocation());
        }
        boolean flag = false;
        for (int i = 0; i < listExpr.getList().size(); i++) {
            try {
                listExpr.getList().get(i).verifyRValue(compiler, localEnv, currentClass, signature.paramNumber(i));
            } catch (ContextualError e) {
                flag = true;
            }
        }
        if (flag) {
            throw new ContextualError("MethodCall: Cannot call method " + method.getName() + " with parameters "
                    + listExpr + " instead of " + signature.getArgs(), getLocation());
        }

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type t = classCall.verifyExpr(compiler, localEnv, currentClass);
        if (!t.isClass()) {
            throw new ContextualError("Incompatible Types: Try to call a function of a non class variable",
                    getLocation());
        }
        ClassType classType = (ClassType) t;
        Signature sig = method.verifyMethodIdent(compiler, classType.getDefinition().getMembers(), currentClass);

        this.verifySignature(compiler, localEnv, currentClass, sig);

        Type returnType = method.getType();
        setType(returnType);
        return returnType;
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        compiler.addInstruction(new PUSH(Register.getR(2)));
        this.codeGenExp(compiler, GPRegister.getR(2));
        compiler.addInstruction(new POP(Register.getR(2)));
        compiler.addInstruction(new CMP(new ImmediateInteger(jumpOnTrue ? 1 : 0), Register.R0));
        compiler.addInstruction(new BEQ(label));
    }
}
