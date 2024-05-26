package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;

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
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                Type t = getLeftOperand().getType();
                setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, t));
                setType(t);
                // which type should I pass ?
                return t;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // type ident = value; -> LOAD(value, ident.addr)
        int storageRegister = 2;
        getRightOperand().codeGenExp(compiler, storageRegister); // register number where rhs of initialization expr is stored
        Identifier ident = (Identifier)getLeftOperand(); // get the identifier of the assignment
        compiler.addInstruction(new STORE(Register.getR(storageRegister), ident.getExpDefinition().getOperand()));
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
