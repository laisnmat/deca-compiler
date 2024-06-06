package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelOr extends AbstractLabel {
    /**
     * Object used to generate labels for an 'Or' structure.
     * If the first part of the 'or' is True, do not evaluate the rest of the expression.
     * @param id
     *      The id of this structure. Must be unique.
     * @param labelManager
     *      The associated labelManager, used to manage prefix.
     */

    public LabelOr(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    /**
     * Label at the end of this structure.
     * 
     * Composed of the prefix of the labelManager and the value of this label.
     * @return
     *      an IMA Label.
     */
    public Label getEndLabel() {
        return new Label(labelManager.getPrefix() + "end");
    }

    @Override
    public String toString() {
        return "or" + super.toString();
    }
}
