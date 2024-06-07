package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.Label;

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
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        if (value) {
            if (jumpOnTrue) {
                compiler.addInstruction(new BRA(label));
            }
        } else {
            if (!jumpOnTrue) {
                compiler.addInstruction(new BRA(label));
            }
        }
    }

    /**
     *
     * To assign a boolean in a variable
     * 
     * @param compiler
     * @param register
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new LOAD((this.getValue() ? 1 : 0), register));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    public DVal getDVal() {
        return new ImmediateInteger((value ? 1 : 0));
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
