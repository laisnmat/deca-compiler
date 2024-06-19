package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a class.
 *
 * @author gl03
 * @date 14/04/2024
 */
public class ClassType extends Type {

    protected ClassDefinition definition;
    private DAddr addr;

    public DAddr getAddr() {
        return addr;
    }

    public void setAddr(DAddr addr) {
        this.addr = addr;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }

    public ClassDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        if (otherType == null)
            return false;
        return otherType.isClass() && this.getName().getName()
                .equals(otherType.getName().getName());
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        if (this.sameType(potentialSuperClass)) {
            return true;
        }
        ClassDefinition superClassDef = this.definition.getSuperClass();
        while (superClassDef != null) {
            if (superClassDef.getType().sameType(potentialSuperClass)) {
                return true;
            }
            superClassDef = superClassDef.getSuperClass();
        }
        return false;
    }

}
