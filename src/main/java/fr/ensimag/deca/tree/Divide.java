package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

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
        GPRegister nextRegister = GPRegister.getR(register.getNumber());
        this.getRightOperand().codeGenExp(compiler, nextRegister);
        if (this.getType().isInt()) {
            compiler.addInstruction(new QUO(register, nextRegister));
        } else {
            compiler.addInstruction(new DIV(register, nextRegister));
        }
    }

}
