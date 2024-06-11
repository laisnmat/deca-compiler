package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl03
 * @date 14/04/2024
 */
public class DeclClass extends AbstractDeclClass {
    Visibility visibility;
    Identifier name, superClass;

    public DeclClass(Visibility visibility, Identifier name, Identifier superClass) {
        Validate.notNull(name);
        this.visibility = visibility;
        this.name = name;
        this.superClass = superClass;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        ClassDefinition classDefinition;
        TypeDefinition typeDefinitionSuper = null;
        if (superClass != null) {
            typeDefinitionSuper = compiler.environmentType.defOfType(superClass.getName());
            if (typeDefinitionSuper == null)
                throw new ContextualError("Super class " + superClass.getName() + " not defined", getLocation());
        }
        if (typeDefinitionSuper != null && !(typeDefinitionSuper instanceof ClassDefinition)) {
            throw new ContextualError("Super class " + superClass.getName() + " is not a ClassDefinition", getLocation());
        }
        ClassDefinition classDefinitionSuper = (ClassDefinition) typeDefinitionSuper;
        classDefinition = new ClassDefinition(
                new ClassType(name.getName(), getLocation(), classDefinitionSuper), getLocation(),
                classDefinitionSuper);
        try {
            compiler.environmentType.declare(name.getName(), classDefinitionSuper);
        } catch (DoubleDefException ex) {
            throw new ContextualError("Class " + name.getName() + " already defined", getLocation());
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
