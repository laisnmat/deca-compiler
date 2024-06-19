package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodBody extends AbstractMethodBody {
    private ListDeclVar declVariables;
    private ListInst insts;

    public MethodBody(ListDeclVar declVariables, ListInst insts) {
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType) throws ContextualError {
        // (3.14)
        // bloc ↓env_types ↓env_exp ↓env_exp_params ↓class ↓return
        // (3.18)
        // list_decl_var ↓env_types ↓env_exp_sup ↓env_exp ↓class ↑env_exp_r
        declVariables.verifyListDeclVariable(compiler, localEnv, currentClass);
        // list_inst ↓env_types ↓env_exp_r/env_exp_sup ↓class ↓return
        boolean returnStatement = insts.verifyListInst(compiler, localEnv, currentClass, returnType);

        if (!compiler.getCompilerOptions().getNoCheck())
            if (!returnType.isVoid() && !returnStatement)
                throw new ContextualError("ReturnError: Missing return statement in non-void method", getLocation());
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        declVariables.decompile(s);
        insts.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
}
