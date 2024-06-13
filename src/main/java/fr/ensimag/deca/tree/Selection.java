package fr.ensimag.deca.tree;

import java.io.PrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Selection extends AbstractLValue {

    private AbstractExpr classe;
    private AbstractIdentifier attribute;

    public Selection(AbstractExpr classe, AbstractIdentifier attribute) {
        this.classe = classe;
        this.attribute = attribute;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classe.iter(f);
        attribute.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classe.prettyPrint(s, prefix, false);
        attribute.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        classe.decompile(s);
        s.print(".");
        attribute.decompile(s);
    }

    // @Override
    // protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
    // throw new UnsupportedOperationException("not yet implemented");
    // }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
        // Type type = this.type.verifyType(compiler);
        // if (!type.isClass()) {
        // throw new ContextualError("Incompatible Type: " + type.getName() + " is not a
        // class", getLocation());
        // }
        // this.setType(type);
        // return type;
    }

}
