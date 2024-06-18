package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class ObjectType extends ClassType {

    public ObjectType(SymbolTable.Symbol objectSymb) {
        super(objectSymb);
        this.definition = new ObjectDefinition(this, Location.BUILTIN, null);

    }

}
