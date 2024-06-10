package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelBoolExp extends AbstractLabel {
    /**
     * Object used to generate labels for a boolean expression.
     * 
     * @param id
     *            The id of this structure. Must be unique.
     * @param labelManager
     *            The associated labelManager, used to manage prefix.
     */
    public LabelBoolExp(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    @Override
    public String toString() {
        return "be" + super.toString();
    }
}
