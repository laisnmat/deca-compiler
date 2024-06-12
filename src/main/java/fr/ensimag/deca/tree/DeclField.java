package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
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
    protected void verifyDeclField(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify DeclField: start");
        // type verification
        Type t = this.type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError(
                    "Illegal declaration: Variable "
                            + this.varName
                            + " is of type void.",
                    getLocation());
        }

        // create the definition of this field
        currentClass.incNumberOfFields();
        FieldDefinition fieldDef = new FieldDefinition(t, getLocation(), this.visibility, currentClass,
                currentClass.getNumberOfFields());

        try {
            currentClass.getMembers().declare(this.varName.getName(), fieldDef);
        } catch (EnvironmentExp.DoubleDefException dde) {
            throw new ContextualError("Field " + this.varName.getName() + " is already defined in this class.",
                    getLocation());
        }

        // initialization verification
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);
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

}
