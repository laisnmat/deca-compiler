package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;



/**
 * @author gl03
 * @date 14/04/2024
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }
}
