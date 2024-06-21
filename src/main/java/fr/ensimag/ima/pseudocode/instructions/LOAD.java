package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.NullLiteral;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

/**
 * @author Ensimag
 * @date 14/04/2024
 */
public class LOAD extends BinaryInstructionDValToReg {

    public LOAD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public LOAD(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

    public LOAD(float f, GPRegister r) {
        this(new ImmediateFloat(f), r);
    }

    public LOAD(IntLiteral f, GPRegister r) {
        this(f.getValue(), r);
    }

    public LOAD(FloatLiteral f, GPRegister r) {
        this(f.getValue(), r);
    }
}
