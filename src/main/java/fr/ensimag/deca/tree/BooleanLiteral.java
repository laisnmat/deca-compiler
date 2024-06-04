package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.DVal;
import java.io.PrintStream;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    protected DVal getDVal() {
        return new ImmediateInteger(getValue() ? 1 : 0);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }
    /** 
     * For tests in conditions
     * @param compiler
     * @param jumpIfTrue
     * @param label
     */
    @Override
    public void codeGenCond(DecacCompiler compiler, boolean jumpIfTrue, Label label) {
        if (value == jumpIfTrue) {
            compiler.addInstruction(new BEQ(label));
        }
    }
    /**
     *
     * To assign a boolean in a variable
     * @param compiler
     * @param register
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        compiler.addInstruction(new LOAD(value ? 1 : 0, register));

    }

    pblic void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

}
