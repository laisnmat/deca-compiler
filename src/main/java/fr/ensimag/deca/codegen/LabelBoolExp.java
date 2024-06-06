package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelBoolExp extends AbstractLabel {
    /**
     * Object used to generate labels for a boolean expression.
     * @param id
     *      The id of this structure. Must be unique.
     * @param labelManager
     *      The associated labelManager, used to manage prefix.
     */
    public LabelBoolExp(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    /**
     * Label at the end of this boolean expression.
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
        return "be" + super.toString();
    }
}
