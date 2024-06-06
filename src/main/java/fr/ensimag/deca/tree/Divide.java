package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        this.getLeftOperand().codeGenExp(compiler, register);
        GPRegister nextRegister = GPRegister.getR(register.getNumber() + 1);
        this.getRightOperand().codeGenExp(compiler, nextRegister);
        compiler.addInstruction(new CMP(0, nextRegister));
        compiler.addInstruction(new BEQ(new Label("div_zero_error")));
        if (this.getType().isInt()) {
            compiler.addInstruction(new QUO(nextRegister, register));
        } else {
            compiler.addInstruction(new DIV(nextRegister, register));
        }
    }

}
