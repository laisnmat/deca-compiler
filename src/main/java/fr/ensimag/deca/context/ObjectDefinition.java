package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

public class ObjectDefinition extends ClassDefinition {
    public ObjectDefinition(ObjectType objectType, Location builtin, ClassDefinition superClass) {
        super(objectType, builtin, superClass);
        this.setNumberOfMethods(1);
    }
}
