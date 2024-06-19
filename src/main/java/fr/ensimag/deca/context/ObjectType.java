package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.Location;

public class ObjectType extends ClassType {

    public ObjectType(SymbolTable.Symbol objectSymb) {
        super(objectSymb);
        this.definition = new ObjectDefinition(this, Location.BUILTIN, null);

    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    @Override
    public boolean isObjectType() {
        return true;
    }
}
