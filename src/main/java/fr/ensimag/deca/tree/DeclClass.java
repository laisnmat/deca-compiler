package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl03
 * @date 14/04/2024
 */
public class DeclClass extends AbstractDeclClass {
    AbstractIdentifier name, superClass;
    ListDeclField fields;
    ListDeclMethod methods;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass, ListDeclField fields,
            ListDeclMethod methods) {
        Validate.notNull(name);
        this.name = name;
        this.superClass = superClass;
        this.fields = fields;
        this.methods = methods;
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
            throw new ContextualError("Super class " + superClass.getName() + " is not a ClassDefinition",
                    getLocation());
        }
        ClassDefinition classDefinitionSuper = (ClassDefinition) typeDefinitionSuper;
        classDefinition = new ClassDefinition(
                new ClassType(name.getName(), getLocation(), classDefinitionSuper), getLocation(),
                classDefinitionSuper);
        try {
            compiler.environmentType.declare(name.getName(), classDefinition);
        } catch (DoubleDefException ex) {
            throw new ContextualError("Class " + name.getName() + " already defined", getLocation());
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        ClassDefinition def = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
        if (def == null) {
            throw new ContextualError("Class " + name.getName() + " not defined", getLocation());
        }

    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        if (superClass != null) {
            superClass.prettyPrint(s, prefix, false);
        }
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        if (superClass != null) {
            superClass.iter(f);
        }
        fields.iter(f);
        methods.iter(f);
    }

}
