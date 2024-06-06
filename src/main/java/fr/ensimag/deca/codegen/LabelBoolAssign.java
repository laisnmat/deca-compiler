package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelBoolAssign extends AbstractLabel {
    /**
     * Object used to generate labels for a boolean assignment.
     * @param id
     *      The id of this structure. Must be unique.
     * @param labelManager
     *      The associated labelManager, used to manage prefix.
     */
    public LabelBoolAssign(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    /**
     * Label for the 'False' part of this boolean assignment.
     * Jump to this label when the assignement is False.
     * 
     * Composed of the prefix of the labelManager and the value of this label.
     * @return
     *      an IMA Label.
     */
    public Label getFalseLabel() {
        return new Label(labelManager.getPrefix() + "false");
    }

    /**
     * Label at the end of this boolean assignment.
     * Jump to this label when the assignement is True.
     * 
     * Composed of the prefix of the labelManager and the value of this label.
     * @return
     *      an IMA Label.
     */
    public Label getTrueLabel() {
        return new Label(labelManager.getPrefix() + "true");
    }

    @Override
    public String toString() {
        return "ba" + super.toString();
    }
}
