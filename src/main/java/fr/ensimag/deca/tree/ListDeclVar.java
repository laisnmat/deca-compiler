package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl03
 * @date 14/04/2024
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {
    private static final Logger LOG = Logger.getLogger(ListDeclVar.class);

    /**
     * Offset to the LB Register.
     */
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar dv : getList()) {
            dv.decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler
     *                     contains the "env_types" attribute
     * @param localEnv
     *                     its "parentEnvironment" corresponds to "env_exp_sup"
     *                     attribute
     *                     in precondition, its "current" dictionary corresponds to
     *                     the "env_exp" attribute
     *                     in postcondition, its "current" dictionary corresponds to
     *                     the "env_exp_r" attribute
     * @param currentClass
     *                     corresponds to "class" attribute (null in the main bloc).
     */
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify DeclVar: start");

        // verify rule (3.16) by verifying each DeclVar in this List of DeclVar
        for (AbstractDeclVar declVar : this.getList()) {
            // verify rule (3.17)
            declVar.verifyDeclVar(compiler, localEnv, currentClass);
        }
        LOG.debug("verify DeclVar: end");
    }

    public void codeGenDeclVarList(DecacCompiler compiler, ClassDefinition currentClass,
            AbstractIdentifier methodName) {
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.codeGenDeclVar(compiler, currentClass, methodName);
        }
    }
}
