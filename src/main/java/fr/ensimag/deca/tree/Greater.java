package fr.ensimag.deca.tree;


/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

}
