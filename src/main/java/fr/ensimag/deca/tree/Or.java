package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelOr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        LabelOr labelOr = compiler.labelManager.getOr();
        this.codeGenCondition(compiler, true, labelOr.getEndLabel());
        compiler.addLabel(labelOr.getEndLabel());
        labelOr.destruct();
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        // ⟨Code(C1 || C2, b, E)⟩ ≡ ⟨Code(!(!C1 && !C2)), b, E)⟩
        if (jumpOnTrue) {
            this.getLeftOperand().codeGenCondition(compiler, true, label);
            this.getRightOperand().codeGenCondition(compiler, true, label);
        } else {
            LabelOr labelOr = compiler.labelManager.getOr();
            this.getLeftOperand().codeGenCondition(compiler, true, labelOr.getEndLabel());
            this.getRightOperand().codeGenCondition(compiler, false, label);
            compiler.addLabel(labelOr.getEndLabel());
            labelOr.destruct();
        }
    }

    @Override
    public String getOperatorName() {
        return "||";
    }

}
