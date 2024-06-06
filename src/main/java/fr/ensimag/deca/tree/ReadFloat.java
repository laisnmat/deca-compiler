package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import java.io.PrintStream;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class ReadFloat extends AbstractReadExpr {

    /**
     * Verify the type of the expression and return the type of the expression
     * (float here)
     * 
     * @param compiler
     *            the compiler used to compile the code
     * @param localEnv
     *            the environment in which the expression is checked
     * @param currentClass
     *            the current class
     * @return Type : the type of the expression (float here)
     * @throws ContextualError
     *             if the types are not compatible (no error here
     *             because it's a leaf node)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.FLOAT);
        return this.getType();
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        compiler.errorManager.addIOError();
        super.addComment(compiler, register);
        compiler.addInstruction(new RFLOAT());
        super.codeGenExp(compiler, register);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
