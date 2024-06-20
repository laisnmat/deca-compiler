package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclField extends AbstractDeclField {
    private static final Logger LOG = Logger.getLogger(DeclField.class);

    final private Visibility visibility;
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier varName,
            AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.visibility = visibility;
        this.varName = varName;
        this.initialization = initialization;
    };

    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass, int index)
            throws ContextualError {
        LOG.debug("verify DeclField `" + currentClass.getType() + "." + varName.getName() + "`: start");
        // (2.5)
        // decl_field ↓env_types ↓super ↓class ↑{name → (field(visib, class), type)}
        EnvironmentExp localEnv = currentClass.getMembers();
        // type verification type ≠ void
        this.type.verifyType(compiler);
        if (this.type.getType().isVoid()) {
            throw new ContextualError("Incompatible Types: Field " + this.varName.getName() + " is of type void.",
                    getLocation());
        }
        // {name → (field(visib, class), type)}
        FieldDefinition fieldDef = new FieldDefinition(this.type.getType(), getLocation(), this.visibility,
                currentClass, index);
        // si (class(__,env_exp_super),__)≜env_types(super) et env_exp_super(name) est
        // défini
        // alors env_exp_super(name) = (field(__,__),__)
        if (currentClass.getSuperClass() != null) {
            EnvironmentExp envExpSuper = currentClass.getSuperClass().getMembers();
            ExpDefinition previous = envExpSuper.get(this.varName.getName());
            if (previous != null && !(previous instanceof FieldDefinition)) {
                throw new ContextualError(
                        "DoubleDefException: Cannot define " + this.varName.getName()
                                + " as a field because it is already defined as a method at " + previous.getLocation(),
                        getLocation());
            }
        }
        try {
            localEnv.declare(this.varName.getName(), fieldDef);
            LOG.debug("declField localEnv : " + localEnv);
            currentClass.incNumberOfFields();
        } catch (EnvironmentExp.DoubleDefException e) {
            ExpDefinition previous = localEnv.get(this.varName.getName());
            throw new ContextualError(
                    "Field " + this.varName.getName() + " is already defined at " + previous.getLocation(),
                    getLocation());
        }
        LOG.debug("verify DeclField `" + currentClass.getType() + "." + varName.getName() + "`: end");

    }

    @Override
    protected void verifyDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify DeclField init`" + currentClass.getType() + "." + varName.getName() + "`: start");

        EnvironmentExp localEnv = currentClass.getMembers();
        // set the definition of the field
        this.varName.verifyExpr(compiler, localEnv, currentClass);

        this.initialization.verifyInitialization(compiler, this.type.getType(),
                localEnv, currentClass);
        LOG.debug("verify DeclField init`" + currentClass.getType() + "." + varName.getName() + "`: end");

    }

    @Override
    protected int codeGenDeclField(DecacCompiler compiler, int localBaseOffset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(visibility.toString().toLowerCase() + " ");
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    protected Visibility getVisibility() {
        return visibility;
    }

}
