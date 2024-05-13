package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

import static org.mockito.ArgumentMatchers.eq;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl03
 * @date 14/04/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected int codeGenInit(DecacCompiler compiler) {
        compiler.addInstruction(new FLOAT(this.getOperand().getDVal(), GPRegister.getR(2)));
        return 2;
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
