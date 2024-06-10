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
    private int localBaseOffset = 1;

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
     *            contains the "env_types" attribute
     * @param localEnv
     *            its "parentEnvironment" corresponds to "env_exp_sup"
     *            attribute
     *            in precondition, its "current" dictionary corresponds to
     *            the "env_exp" attribute
     *            in postcondition, its "current" dictionary corresponds to
     *            the "env_exp_r" attribute
     * @param currentClass
     *            corresponds to "class" attribute (null in the main bloc).
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

    public void codeGenDeclVarList(DecacCompiler compiler) {
        for (AbstractDeclVar declVar : this.getList()) {
            int offset = declVar.codeGenDeclVar(compiler, this.getLocalBaseOffset());
            this.setLocalBaseOffset(offset);
        }
    }

    /**
     * Get the offset to the LB Register.
     *
     * @return
     *         the localBaseOffset
     */
    public int getLocalBaseOffset() {
        return this.localBaseOffset;
    }

    /**
     * Set the offset to the LB Register.
     *
     * @param offset
     *            The new offset.
     *            Precondition:
     *            offset > 0
     */
    public void setLocalBaseOffset(int offset) {
        if (offset <= 0) {
            throw new IllegalArgumentException("LocalBase offset should not be less than one.");
        }

        this.localBaseOffset = offset;
    }
}
