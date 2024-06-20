package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * This literal
 *
 * @author gl03
 * @date 14/04/2024
 */
public class ThisLiteral extends AbstractExpr {

    private static Logger LOG = Logger.getLogger(ThisLiteral.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // (3.43)
        // verify that the current class is not null (class ̸= 0)
        if (currentClass == null) {
            throw new ContextualError("Illegal operand: Cannot use 'this' outside of a class", getLocation());
        }
        // affectation type := type_class(class)
        setType(currentClass.getType());
        return currentClass.getType();
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        RegisterOffset offset = new RegisterOffset(-2, Register.LB);
        compiler.addInstruction(new LOAD(this.getDVal(), Register.R0));
    }

    @Override
    String prettyPrintNode() {
        return "This";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("This");
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
    public DVal getDVal() {
        return new RegisterOffset(-2, Register.LB);
    }
}
