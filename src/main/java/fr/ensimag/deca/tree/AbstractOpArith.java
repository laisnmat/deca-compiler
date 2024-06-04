package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(type1);
        if (!type1.sameType(type2)) {
            if (type1.isInt() && type2.isFloat()) {
                ConvFloat conv = new ConvFloat(this.getLeftOperand());
                this.setLeftOperand(conv);
                this.setType(type2);
            } else if (type1.isFloat() && type2.isInt()) {
                ConvFloat conv = new ConvFloat(this.getRightOperand());
                this.setRightOperand(conv);
            } else {
                throw new ContextualError(
                        "Incompatible types \'" + type2.getName() + "\' with \'" + type1.getName() + "\'",
                        getLocation());
            }
        }
        return type1;
    }
}

/*
 * if (!(type1.isFloat() || type1.isInt()) || !(type2.isFloat() ||
 * type2.isInt())) {
 * if (type1.sameType(type2)) {
 * throw new ContextualError(
 * "Illegal operand \'" + this.getOperatorName() + "\' between " +
 * type1.getName(),
 * getLocation());
 * }
 * throw new ContextualError(
 * "Illegal operand \'" + this.getOperatorName() + "\' between " +
 * type1.getName() + " and "
 * + type2.getName(),
 * getLocation());
 * }
 */