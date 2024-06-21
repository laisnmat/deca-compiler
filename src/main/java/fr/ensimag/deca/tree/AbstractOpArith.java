package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * Verify the type of the operands and the type of the expression and casts the
     * operands if necessary or throws an error if the types are not compatible
     * 
     * @param compiler
     *                     (type- DecacCompiler) the compiler
     * @param localEnv
     *                     (type- EnvironmentExp) the environment in which the
     *                     expression is checked
     * @param currentClass
     *                     (type- ClassDefinition) the current class
     * @return (type- Type) the type of the expression (int or float)
     * @throws ContextualError
     *                         if the types are not compatible
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(type1);
        if (!(type1.isFloat() || type1.isInt()) || !(type2.isFloat() || type2.isInt())) {
            if (type1.sameType(type2)) {
                throw new ContextualError(
                        "Illegal operand \'" + this.getOperatorName() + "\' between " + type1.getName(),
                        getLocation());
            }
            throw new ContextualError(
                    "Illegal operand \'" + this.getOperatorName() + "\' between " + type1.getName() + " and "
                            + type2.getName(),
                    getLocation());
        }

        if (type1.isFloat() && type2.isInt()) {
            ConvFloat conv = new ConvFloat(this.getRightOperand());
            conv.verifyExpr(compiler, localEnv, currentClass);
            this.setRightOperand(conv);
        }
        if (type1.isInt() && type2.isFloat()) {
            ConvFloat conv = new ConvFloat(this.getLeftOperand());
            conv.verifyExpr(compiler, localEnv, currentClass);
            this.setLeftOperand(conv);
            this.setType(type2);
        }
        return this.getType();
    }

    public DVal codeGenOp(DecacCompiler compiler, GPRegister register) {
        this.getLeftOperand().codeGenExp(compiler, register);
        DVal dvalRightOp = this.getRightOperand().getDVal();
        if (dvalRightOp != null) {
            return dvalRightOp;
        } else {
            if (register.getNumber() == DecacCompiler.MAXREGISTER) {
                compiler.addInstruction(new PUSH(register), "sauvegarde ");
                compiler.stackManager.incrementPushCount();
                this.getRightOperand().codeGenExp(compiler, register);
                compiler.addInstruction(new LOAD(register, GPRegister.R1));
                compiler.addInstruction(new POP(register), "restauration ");
                compiler.stackManager.decrementPushCount();
                return GPRegister.R1;
            } else {
                GPRegister nextRegister = GPRegister.getR(register.getNumber() + 1);
                this.getRightOperand().codeGenExp(compiler, nextRegister);
                return nextRegister;
            }
        }
    }

    /**
     * Generate assembly code for the expression (Modulo) only when we use -n flag
     * 
     * @param compiler The compiler
     */

    public void codeGenOpError(DecacCompiler compiler) {
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.errorManager.addArithmeticOverflowError();
            compiler.addInstruction(new BOV(compiler.labelManager.getArithmeticOverflowError()));
        }
    }
}
