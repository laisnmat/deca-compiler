package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelWhile extends AbstractLabel {
    /**
     * Object used to generate labels for a 'while' structure.
     * @param id
     *      The id of this structure. Must be unique.
     * @param labelManager
     *      The associated labelManager, used to manage prefix.
     */
    public LabelWhile(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    /**
     * Label at the start of this structure.
     * 
     * Composed of the prefix of the labelManager and the value of this label.
     * @return
     *      an IMA Label.
     */
    public Label getStartLabel() {
        return new Label(labelManager.getPrefix() + "start");
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
        return "while" + super.toString();
    }
}
