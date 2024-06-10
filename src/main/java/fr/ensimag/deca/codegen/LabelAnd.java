package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelAnd extends AbstractLabel {
    /**
     * Object used to generate labels for an 'And' structure.
     * If the first part of the 'and' is False, do not evaluate the rest of the
     * expression.
     * 
     * @param id
     *            The id of this structure. Must be unique.
     * @param labelManager
     *            The associated labelManager, used to manage prefix.
     */
    public LabelAnd(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    @Override
    public String toString() {
        return "and" + super.toString();
    }
}
