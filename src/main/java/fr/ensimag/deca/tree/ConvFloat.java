package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
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
        this.setType(compiler.environmentType.FLOAT);
        return this.getType();
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        this.getOperand().codeGenExp(compiler, register);
        compiler.addInstruction(new FLOAT(register, register));
    }

    @Override
    public String decompile() {
        return "(float) " + getOperand().decompile();
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
