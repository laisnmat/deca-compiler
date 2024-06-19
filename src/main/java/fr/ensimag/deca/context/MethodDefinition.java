package fr.ensimag.deca.context;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Definition of a method
 *
 * @author gl03
 * @date 14/04/2024
 */
public class MethodDefinition extends ExpDefinition {

    private final EnvironmentExp localEnv;
    private final Signature signature;
    private final ClassDefinition containingClass;
    private Label label;
    private final int index;

    /**
     * 
     * @param type      Return type of the method
     * @param location  Location of the declaration of the method
     * @param signature List of arguments of the method
     * @param memberof  Class the method is defined in
     * @param index     Index of the method in the class. Starts from 0.
     */
    public MethodDefinition(Type type, Location location, Signature signature, ClassDefinition memberof,
            int index,
            EnvironmentExp localEnv) {
        super(type, location);
        this.signature = signature;
        this.containingClass = memberof;
        this.index = index;
        this.localEnv = localEnv;
    }

    @Override
    public boolean isMethod() {
        return true;
    }

    public Label getLabel() {
        Validate.isTrue(label != null,
                "setLabel() should have been called before");
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public MethodDefinition asMethodDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    public Signature getSignature() {
        return signature;
    }

    public ClassDefinition getContainingClass() {
        return containingClass;
    }

    @Override
    public String getNature() {
        return "method";
    }

    @Override
    public boolean isExpression() {
        return false;
    }

    public EnvironmentExp getLocalEnv() {
        return localEnv;
    }

}
