package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public abstract class AbstractLabel {
    /**
     * The unique id of this Label.
     */
    protected final int id;

    /**
     * The prefix that the labelManager had before adding this new Label
     */
    protected final String prefixBefore;

    /**
     * The LabelManager associated with this label.
     */
    protected final LabelManager labelManager;

    /**
     * Label objects are used to get the right labels when generating assembly
     * structures.
     * 
     * Every instance must call .destruct() when not using it anymore.
     * 
     * @param id
     *            The id of this structure. Must be unique.
     * @param labelManager
     *            The associated labelManager, used to manage prefix.
     */
    public AbstractLabel(int id, LabelManager labelManager) {
        this.id = id;
        this.labelManager = labelManager;
        this.prefixBefore = new String(labelManager.getPrefix());
        labelManager.addPrefix(this + ".");
    }

    /**
     * Set back the prefix of the LabelManager to what it was before adding this new
     * Label.
     */
    public void destruct() {
        this.labelManager.removePrefix();
    }

    /**
     * Label at the end of this structure.
     *
     * Composed of the prefix of the labelManager and the value of this label.
     * 
     * @return
     *         an IMA Label.
     */
    public Label getEndLabel() {
        return new Label(labelManager.getPrefix() + "end");
    }

    /**
     * Name of this structure, containing its id.
     */
    @Override
    public String toString() {
        return Integer.toString(this.id);
    }
}
