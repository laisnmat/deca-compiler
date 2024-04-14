package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

/**
 * @author Ensimag
 * @date 14/04/2024
 */
public class SHL extends UnaryInstructionToReg {
    public SHL(GPRegister op1) {
        super(op1);
    }
}
