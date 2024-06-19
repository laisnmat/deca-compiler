package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelClass extends AbstractLabel {

    /**
     * Object used to generate labels for classes.
     * 
     * @param id
     *                     The id of this structure. Must be unique.
     * @param labelManager
     *                     The associated labelManager, used to manage prefix.
     */
    public LabelClass(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    @Override
    public String toString() {
        return "???????????" + super.toString();
    }
}
