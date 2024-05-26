package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;


/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }
}
