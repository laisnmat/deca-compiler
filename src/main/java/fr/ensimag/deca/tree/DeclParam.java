package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclParam extends AbstractDeclParam {
    private static final Logger LOG = Logger.getLogger(DeclParam.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        Validate.notNull(type);
        Validate.notNull(paramName);
        this.type = type;
        this.paramName = paramName;
    }

    @Override
    protected Type verifyDeclParam(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        // (2.9)
        // type ↓env_types ↑type
        Type t = type.verifyType(compiler);
        // condition type ̸= void
        if (t.isVoid()) {
            throw new ContextualError("Param `" + paramName.getName() + "` is a void type", getLocation());
        }
        return t;
    }

    @Override
    protected void verifyDeclParamInit(DecacCompiler compiler,
            EnvironmentExp localEnv)
            throws ContextualError {
        // (3.13)
        // decl_param ↓env_types ↑{name → (param, type)}
        // type ↓env_types ↑type
        ParamDefinition paramDef = new ParamDefinition(type.getType(), getLocation());
        try {
            localEnv.declare(paramName.getName(), paramDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Param `" + paramName.getName() + "` is already declared", getLocation());
        }
        paramName.setType(paramDef.getType());
        paramName.setDefinition(paramDef);
    }

    @Override
    protected int codeGenDeclParam(DecacCompiler compiler, int localBaseOffset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        paramName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }

}
