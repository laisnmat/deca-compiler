package fr.ensimag.deca.context;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a class.
 *
 * @author gl03
 * @date 14/04/2024
 */
public class ClassDefinition extends TypeDefinition {

    private final EnvironmentExp members;
    private final ClassDefinition superClass;
    private int numberOfFields = 0;
    private int numberOfMethods = 0;

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }

    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    }

    ;

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    public EnvironmentExp getMembers() {
        return members;
    }

    @Override
    public ClassDefinition asClassDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    public Label getInitLabel() {
        return new Label("ajiiuahdfuazhdioaizdhzohduahdhazuidzh"); // todo get init label
    }
}
