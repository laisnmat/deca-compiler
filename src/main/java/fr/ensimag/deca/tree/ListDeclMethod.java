package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl03
 * @date 11/06/2024
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
        }
    }

    protected void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyDeclMethod(compiler, currentClass);
        }
    }

    protected void verifyListDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass)
            throws ContextualError {
        // (3.10)
        // (decl_method ↓env_types ↓env_exp ↓class)*
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyDeclMethodBody(compiler, currentClass);
        }
    }

    protected int codeGenListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass, int classAddress) {
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.codeGenDeclMethod(compiler, currentClass, classAddress);
        }
        return 0;
    }
}
