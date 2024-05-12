package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;



/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

    @Override
    protected int codeGenInit(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
