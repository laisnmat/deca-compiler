package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class InstanceOfLabel extends AbstractLabel {
    public InstanceOfLabel(int id, LabelManager labelManager) {
        super(id, labelManager);
    }

    @Override
    public String toString() {
        return "instanceOf" + super.toString();
    }

    public Label getStartLabel() {
        return new Label(labelManager.getPrefix() + "start");
    }

}
