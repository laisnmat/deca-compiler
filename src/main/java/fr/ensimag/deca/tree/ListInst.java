package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * 
 * @author gl03
 * @date 14/04/2024
 */
public class ListInst extends TreeList<AbstractInst> {
    private static final Logger LOG = Logger.getLogger(ListInst.class);

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler     contains "env_types" attribute
     * @param localEnv     corresponds to "env_exp" attribute
     * @param currentClass
     *                     corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *                     corresponds to "return" attribute (void in the main
     *                     bloc).
     */
    public boolean verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify ListInst: start");

        // verify each Inst in this List of Inst
        // verify rule (3.19)
        boolean returnStatement = false;
        for (AbstractInst inst : this.getList()) {
            // verify rules from (3.20) to (3.27), depending on what kind of AbstractInst is
            // (inst ↓env_types ↓env_exp ↓class ↓return)*

            returnStatement = inst.verifyInst(compiler, localEnv, currentClass, returnType) || returnStatement;
        }

        LOG.debug("verify ListInst: end");
        return returnStatement;
    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            i.codeGenInst(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }
}
