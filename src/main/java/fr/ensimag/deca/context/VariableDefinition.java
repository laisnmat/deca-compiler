package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a variable.
 *
 * @author gl03
 * @date 14/04/2024
 */
public class VariableDefinition extends ExpDefinition {
    public VariableDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getNature() {
        return "variable";
    }

    @Override
    public boolean isVar() {
        return true;
    }

    @Override
    public boolean isExpression() {
        return true;
    }
}
