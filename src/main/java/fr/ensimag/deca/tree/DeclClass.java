package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

import fr.ensimag.deca.tools.IndentPrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl03
 * @date 14/04/2024
 */
public class DeclClass extends AbstractDeclClass {
    private final Logger LOG = Logger.getLogger(DeclClass.class);
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

    private ClassType classType;

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("PASSE 1 : verify class `" + name.getName() + "`: start");
        // (1.3)
        // up {name -> (class(super,{}),type_class(name))} (+) env_types, (down)
        // env_types
        TypeDefinition superClassDef = compiler.environmentType.defOfType(superClass.getName());
        // verify if the super class is declared in the environment
        if (superClassDef == null)
            throw new ContextualError("NullDefinition: Super class `" + superClass.getName() + "` not declared",
                    superClass.getLocation());
        ClassDefinition superClassDefClass = superClassDef.asClassDefinition(
                "Super class `" + superClass.getName() + "` is not a class", superClass.getLocation());

        ClassType currentTypeClass = new ClassType(name.getName(), getLocation(), superClassDefClass);
        ClassDefinition currentClassDef = currentTypeClass.getDefinition();

        // try to declare the class in the environment if it is not already declared
        try {
            compiler.environmentType.declare(name.getName(), currentClassDef);
        } catch (DoubleDefException e) {
            ClassDefinition previous = compiler.environmentType.defOfType(name.getName()).asClassDefinition(
                    "Class `" + name.getName() + "` is not a class", name.getLocation());
            throw new ContextualError(
                    "DoubleDefException: Class `" + name.getName() + "` already declared at " + previous.getLocation(),
                    name.getLocation());
        }
        superClass.verifyType(compiler);
        name.verifyType(compiler);
        LOG.debug("PASSE 1 : verify class: end");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("PASSE 2 : verify class members `" + name.getName() + "`: start");
        ClassDefinition currentClass = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
        // (2.3)
        // TODO: verify if this is necessary for step C
        currentClass.setNumberOfFields(currentClass.getSuperClass().getNumberOfFields());
        currentClass.setNumberOfMethods(currentClass.getSuperClass().getNumberOfMethods());
        // list_decl_field ↓env_types ↓super ↓name ↑env_expf
        fields.verifyListDeclField(compiler, currentClass);
        // list_decl_method ↓env_types ↓super ↑env_expm
        methods.verifyListDeclMethod(compiler, currentClass);

        // condition : (class(__, env_exp_super), __) ≜ env_types(super)
        // Verify that the super class is declared in the environment

        // affectation : new_def := (class(super, (env_types(super)expf ⊕
        // env_expm)/env_exp_super)

        // ,type_class(name))
        // env_typesr := {name 7→ new_def}/env_types
        LOG.debug("PASSE 2 : verify class members: end");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("PASSE 3 : verify class members `" + name.getName() + "`: start");
        // (3.5)
        ClassDefinition currentClass = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
        // list_decl_field ↓env_types ↓env_exp ↓class
        fields.verifyListDeclFieldInit(compiler, currentClass);
        // list_decl_method ↓env_types ↓env_exp ↓class
        methods.verifyListDeclMethodBody(compiler, currentClass.getMembers(), currentClass);
        // condition (class(__, env_exp), __) ≜ env_types(class)
        LOG.debug("PASSE 3 : verify class members: end");
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

    /**
     * Method to generate code for a new Class declaration.
     * 
     * @param compiler
     *            DecacCompiler associated
     * @param currentClass
     *            Current Class in which the class is declared
     */
    protected void codeGenDeclClass(DecacCompiler compiler, ClassDefinition currentClass) {
        Register reg;
        int baseOffset;
        if (currentClass == null) {
            reg = Register.GB;
            baseOffset = compiler.stackManager.getVarGlobalCount();
        } else {
            reg = Register.LB;
            baseOffset = compiler.stackManager.getVarLocalCount();
        }
        baseOffset++;

        // code adresse

        if (superClass == null) {
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        } else {
            compiler.addInstruction(new LEA(((ClassType) superClass.getType()).getAddr(), Register.R0));
        }

        RegisterOffset offset = new RegisterOffset(baseOffset, GPRegister.GB);
        compiler.addInstruction(new STORE(Register.R0, offset));
        ((ClassType) this.name.getType()).setAddr(offset);

        // code attributes ?

        // code methods

        if (currentClass == null) {
            compiler.stackManager.setGlobalCount(baseOffset);
        } else {
            compiler.stackManager.setLocalCount(baseOffset);
        }
    }
}
