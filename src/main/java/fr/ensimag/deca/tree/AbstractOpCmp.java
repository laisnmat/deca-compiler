package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // verify that the two operands are of the same type
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        // check that the two operands are comparable
        if (!(type1.isFloat() || type1.isInt()) || !(type2.isFloat() || type2.isInt())) {
            if (type1.sameType(type2)) {
                throw new ContextualError(
                        "Illegal operand \'" + this.getOperatorName() + "\' between " + type1.getName(),
                        getLocation());
            }
            throw new ContextualError(
                    "Illegal operand \'" + this.getOperatorName() + "\' between " + type1.getName() + " and "
                            + type2.getName(),
                    getLocation());
        }

        if (type1.isFloat() && type2.isInt()) {
            this.setRightOperand(new ConvFloat(this.getRightOperand()));
            this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        if (type1.isInt() && type2.isFloat()) {
            this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
            this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }

        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        DVal dvalRightOp = this.getRightOperand().codeGenExp(compiler);
        GPRegister registerLeftOp = (dvalRightOp == GPRegister.getR(2) ? GPRegister.getR(3) : GPRegister.getR(2));
        this.getLeftOperand().codeGenExp(compiler, registerLeftOp);

        compiler.addInstruction(new CMP(dvalRightOp, registerLeftOp));
    }
}
