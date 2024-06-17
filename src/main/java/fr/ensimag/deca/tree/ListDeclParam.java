package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl03
 * @date 11/06/2024
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam p : getList()) {
            p.decompile(s);
        }
    }

    protected Signature verifyListDeclParam(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclParam declParam : getList()) {
            sig.add(declParam.verifyDeclParam(compiler, currentClass));
        }
        return sig;
    }

    protected void verifyListDeclParamInit(DecacCompiler compiler, EnvironmentExp localEnv)
            throws ContextualError {
        // (3.12)
        // (decl_param ↓env_types ↑env_exp
        // {env_expr := env_expr ⊕ env_exp})*
        for (AbstractDeclParam declParam : getList()) {
            declParam.verifyDeclParamInit(compiler, localEnv);
        }
    }
}