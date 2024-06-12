package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(DeclMethod.class);

    // final private Visibility visibility;
    // final private AbstractIdentifier type;
    // final private AbstractIdentifier varName;
    // final private AbstractInitialization initialization;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam params,
            AbstractMethodBody body) {
        // Validate.notNull(visibility);
        // Validate.notNull(type);
        // Validate.notNull(varName);
        // Validate.notNull(initialization);
        // this.type = type;
        // this.visibility = visibility;
        // this.varName = varName;
        // this.initialization = initialization;
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int codeGenDeclMethod(DecacCompiler compiler, int localBaseOffset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
