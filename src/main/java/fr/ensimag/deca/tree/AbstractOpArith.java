package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;

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
     * @param compiler     (type- DecacCompiler) the compiler
     * @param localEnv     (type- EnvironmentExp) the environment in which the
     *                     expression is checked
     * @param currentClass (type- ClassDefinition) the current class
     * @return (type- Type) the type of the expression (int or float)
     * @throws ContextualError if the types are not compatible
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(type1);
        if (!type1.sameType(type2)) {
            if (type1.isInt() && type2.isFloat()) {
                ConvFloat conv = new ConvFloat(this.getLeftOperand());
                this.setLeftOperand(conv);
                this.setType(type2);
            } else if (type1.isFloat() && type2.isInt()) {
                ConvFloat conv = new ConvFloat(this.getRightOperand());
                this.setRightOperand(conv);
            } else {
                throw new ContextualError(
                        "Incompatible types \'" + type2.getName() + "\' with \'" + type1.getName() + "\'",
                        getLocation());
            }
        }
        return type1;
    }

    public DVal codeGenOpperand(DecacCompiler compiler, GPRegister register) {
        this.getLeftOperand().codeGenExp(compiler, register);
        compiler.addInstruction(new PUSH(GPRegister.getR(2)));
        DVal nextRegister = this.getRightOperand().codeGenExp(compiler);
        if (nextRegister instanceof GPRegister) {
            GPRegister next = GPRegister.getR(register.getNumber() + 1);
            compiler.addInstruction(new LOAD(nextRegister, next));
            nextRegister = next;
            compiler.addInstruction(new POP(GPRegister.getR(2)));
        }
        return nextRegister;
    }
}
