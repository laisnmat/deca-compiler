package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(getLeftOperand().getType() == compiler.environmentType.INT
                || getLeftOperand().getType() == compiler.environmentType.FLOAT)) {
            throw new ContextualError("L'opérande gauche n'est ni un int ni un float", getLocation());
        }

        getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(getRightOperand().getType() == compiler.environmentType.INT
                || getRightOperand().getType() == compiler.environmentType.FLOAT)) {
            throw new ContextualError("L'opérande droite n'est ni un int ni un float", getLocation());
        }

        if (getLeftOperand().getType() == compiler.environmentType.INT
                && getRightOperand().getType() == compiler.environmentType.FLOAT) {
            ConvFloat cv = new ConvFloat(getLeftOperand());
            cv.setType(compiler.environmentType.FLOAT);
            setLeftOperand(cv);
        } else if (getRightOperand().getType() == compiler.environmentType.INT
                && getLeftOperand().getType() == compiler.environmentType.FLOAT) {
            ConvFloat cv = new ConvFloat(getRightOperand());
            cv.setType(compiler.environmentType.FLOAT);
            setRightOperand(cv);
        }

        setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        this.getLeftOperand().codeGenExp(compiler, registerName);
        compiler.addInstruction(new LOAD(Register.getR(registerName), Register.getR(registerName + 1)));
        this.getRightOperand().codeGenExp(compiler, registerName);
        compiler.addInstruction(new CMP(Register.getR(registerName), Register.getR(registerName + 1)));
    }
}
