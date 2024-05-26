package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;


/**
 * Operator "x >= y"
 * 
 * @author gl03
 * @date 14/04/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }
}
