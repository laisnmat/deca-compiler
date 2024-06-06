package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelIf extends AbstractLabel {
    /**
     * Object used to generate labels for an 'if then else' structure.
     * @param id
     *      The id of this structure. Must be unique.
     * @param labelManager
     *      The associated labelManager, used to manage prefix.
     */
    public LabelIf(int id, LabelManager labelManager) {
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

    /**
     * Label for the 'else' part of an 'if then else' structure.
     * 
     * Composed of the prefix of the labelManager and the value of this label.
     * @return
     *      an IMA Label.
     */
    public Label getElseLabel() {
        return new Label(labelManager.getPrefix() + "else");
    }

    @Override
    public String toString() {
        return "if" + super.toString();
    }
}
