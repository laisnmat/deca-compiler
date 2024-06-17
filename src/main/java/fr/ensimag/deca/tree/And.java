package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelAnd;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String getOperatorName() {
        return "&&";
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        LabelAnd labelAnd = compiler.labelManager.getAnd();
        this.codeGenCondition(compiler, false, labelAnd.getEndLabel());
        compiler.addLabel(labelAnd.getEndLabel());
        labelAnd.destruct();
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        if (jumpOnTrue) {
            LabelAnd labelAnd = compiler.labelManager.getAnd();
            this.getLeftOperand().codeGenCondition(compiler, false, labelAnd.getEndLabel());
            this.getRightOperand().codeGenCondition(compiler, true, label);
            compiler.addLabel(labelAnd.getEndLabel());
            labelAnd.destruct();
        } else {
            this.getLeftOperand().codeGenCondition(compiler, false, label);
            this.getRightOperand().codeGenCondition(compiler, false, label);
        }
    }
}
