package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.RTS;

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
    protected void codeGenMethodBody(DecacCompiler compiler, ClassDefinition currentClass,
            AbstractIdentifier methodName) {
        // start method
        compiler.addComment("Code de la méthode " + methodName.getName());
        compiler.addLabel(methodName.getMethodDefinition().getLabel());

        // check if method is empty
        if (insts.isEmpty()) {
            compiler.addInstruction(new RTS());
            return;
        }

        // test stack overflow
        TSTO methodTSTO = new TSTO(0);
        compiler.addInstruction(methodTSTO, "Test de débordement de la pile");

        // save registers
        compiler.addComment("Sauvegarde des registres");
        int numParams = methodName.getMethodDefinition().getSignature().size();
        for (int i = 0; i < numParams; i++) {
            compiler.addInstruction(new PUSH(Register.getR(i + 2)));
            compiler.stackManager.incrementPushCount();
        }

        declVariables.codeGenDeclVarList(compiler, currentClass, methodName);

        ImmediateInteger tstoVal = new ImmediateInteger(compiler.stackManager.currentPushCount);
        methodTSTO.setOperand(tstoVal);
        insts.codeGenListInst(compiler, methodName);

        String endMethod = methodName.getMethodDefinition().getLabel().toString().replace("code", "end");

        compiler.addLabel(new Label(endMethod));
        compiler.addComment("Restauration des registres");

        // restore registers
        for (int i = numParams; i > 0; i--) {
            compiler.addInstruction(new POP(Register.getR(i + 2)));
        }
        compiler.addInstruction(new RTS());
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
