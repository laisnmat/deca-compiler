package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.HashMap;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ObjectType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl03
 * @date 14/04/2024
 */
public class DeclClass extends AbstractDeclClass {
    private final Logger LOG = Logger.getLogger(DeclClass.class);
    private ClassType classType;
    AbstractIdentifier name;
    AbstractIdentifier superClass;
    ListDeclField fields;
    ListDeclMethod methods;
    HashMap<DeclMethod, Label> methodLabels = new HashMap<>();

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
        LOG.debug("PASSE 3 : verify class body `" + name.getName() + "`: start");
        // (3.5)
        ClassDefinition currentClass = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
        // list_decl_field ↓env_types ↓env_exp ↓class
        fields.verifyListDeclFieldInit(compiler, currentClass);
        // list_decl_method ↓env_types ↓env_exp ↓class
        methods.verifyListDeclMethodBody(compiler, currentClass.getMembers(), currentClass);
        // condition (class(__, env_exp), __) ≜ env_types(class)
        LOG.debug("PASSE 3 : verify class body: end");
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
     *                     DecacCompiler associated
     * @param currentClass
     *                     Current Class in which the class is declared
     */
    protected void codeGenDeclClass(DecacCompiler compiler) {
        int baseOffset;
        ClassDefinition currentClass = this.name.getClassDefinition();
        baseOffset = compiler.stackManager.getVarGlobalCount();
        baseOffset++;

        compiler.addComment("Code de la table des méthodes de " + currentClass.getType().getName().getName());

        // code adresse
        compiler.addInstruction(new LEA(((ClassType) superClass.getType()).getAddr(), Register.R0));

        RegisterOffset offset = new RegisterOffset(baseOffset, GPRegister.GB);
        compiler.addInstruction(new STORE(Register.R0, offset));
        ((ClassType) this.name.getType()).setAddr(offset);
        compiler.stackManager.incrementVarGlobalCount();

        // code method table
        HashMap<Symbol, ExpDefinition> memberDefinitions = (HashMap) currentClass.getMembers().getAllMembers();
        memberDefinitions.forEach((sym, def) -> {
            if (def.isMethod()) {
                // set method label as code.class.method
                Label methodLabel = new Label(
                        "code." + ((MethodDefinition) def).getContainingClass().getType() + "." + sym);
                ((MethodDefinition) def).setLabel(methodLabel);

                // LOAD code.class.method, R0
                compiler.addInstruction(new LOAD(new LabelOperand(methodLabel), Register.R0));

                // calculate address of method from address of class + method index
                int methodAddress = compiler.stackManager.getVarGlobalCount() + ((MethodDefinition) def).getIndex();

                // calculate offset for GB
                RegisterOffset methodOffset = new RegisterOffset(methodAddress, GPRegister.GB);

                // STORE R0, x(GB)
                compiler.addInstruction(new STORE(Register.R0, methodOffset));
                compiler.stackManager.incrementVarGlobalCount();
            }
        });
    }

    @Override
    protected void codegenMethods(DecacCompiler compiler) {
        ClassDefinition currentClass = this.name.getClassDefinition();
        methods.codeGenListDeclMethod(compiler, currentClass);
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler) {
        ClassDefinition currentClass = this.name.getClassDefinition();
        compiler.addComment("Initialisation des champs de " + currentClass.getType().getName());
        compiler.addLabel(currentClass.getLabel());
        RegisterOffset thisOffset = new RegisterOffset(-2, Register.LB);

        // on stocke l'objet dans R1
        compiler.addInstruction(new LOAD(thisOffset, Register.R1), "R1 contient l'adresse de l'objet");

        if (this.fields.size() != 0) {
            // init all new fields to 0
            for (AbstractDeclField declField : fields.getList()) {
                declField.codeGenDeclField(compiler, false);
            }
        }

        if (this.superClass != null && !this.superClass.getType().isObjectType()) {
            compiler.addComment("Appel de l'initialisation des champs hérités de "
                    + this.superClass.getClassDefinition().getType().getName());

            compiler.addInstruction(new PUSH(Register.R1), "empile l'objet à initialiser");
            compiler.stackManager.incrementPushCount();
            compiler.addInstruction(new BSR(this.superClass.getClassDefinition().getLabel()),
                    "appel de l'initialisation de la super-classe");
            compiler.addInstruction(new SUBSP(1), "on remet la pile dans l'état initial");
            compiler.stackManager.decrementPushCount();
        }

        if (this.fields.size() != 0) {
            // init any fields that have an explicit initialisation
            for (AbstractDeclField declField : fields.getList()) {
                declField.codeGenDeclField(compiler, true);
            }
        }

        // return
        compiler.addInstruction(new RTS());
    }
}
