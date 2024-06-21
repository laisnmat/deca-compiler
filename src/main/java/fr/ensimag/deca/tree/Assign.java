package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelBoolAssign;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // verify left operand and get its type
        Type type = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        // verify if right operand can be assigned to left operand, eventually using an
        // additional ConvFloat
        setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, type));

        // set the type of this Assign an returns it
        setType(type);
        return getType();
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        // get the result of the right expression into the register
        super.addComment(compiler, register);
        getRightOperand().codeGenExp(compiler, register);

        // store the content of the register into the memory
        if (this.getLeftOperand() instanceof Identifier) {
            compiler.addInstruction(
                    new STORE(register, ((Identifier) this.getLeftOperand()).getExpDefinition().getOperand()),
                    "assignment");
        } else if (this.getLeftOperand() instanceof Selection) {
            Selection selec = (Selection) this.getLeftOperand();
            selec.codeGenAssign(compiler, register);
        } else {
            throw new DecacInternalError("Unexpected case in Assign.codeGenExp: please report this bug.");
        }
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        // get the result of the right expression into the register
        super.addComment(compiler, jumpOnTrue, label);
        LabelBoolAssign labelBoolAssign = compiler.labelManager.getBoolAssign();

        // 'if' instructions: the boolean takes the value one if the condition is true,
        // else jump to the 'else' instructions
        this.getRightOperand().codeGenCondition(compiler, false, labelBoolAssign.getFalseLabel());
        compiler.addInstruction(new LOAD(1, Register.R0));

        // 'else' instructions: the boolean takes the value zero
        compiler.addInstruction(jumpOnTrue ? new BRA(label) : new BRA(labelBoolAssign.getTrueLabel()));
        compiler.addLabel(labelBoolAssign.getFalseLabel());
        compiler.addInstruction(new LOAD(0, Register.R0));
        // end of the 'else' instructions
        compiler.addLabel(labelBoolAssign.getTrueLabel());

        // store the value into the boolean identifier
        if (this.getLeftOperand() instanceof Identifier) {
            compiler.addInstruction(
                    new STORE(Register.R0,
                            ((Identifier) this.getLeftOperand()).getExpDefinition().getOperand()),
                    "assignment");
        } else if (this.getLeftOperand() instanceof Selection) {
            Selection selec = (Selection) this.getLeftOperand();
            selec.codeGenAssign(compiler, Register.R1);
        } else {
            throw new DecacInternalError("Unexpected case in Assign.codeGenExp: please report this bug.");
        }

        labelBoolAssign.destruct();
    }

    @Override
    public String getOperatorName() {
        return "=";
    }

}
