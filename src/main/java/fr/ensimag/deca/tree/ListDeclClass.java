package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        // (1.2)
        // (down) env_types, (up) env_typesr
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass declClass : this.getList()) {
            declClass.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        // (2.2)
        // (down) env_types, (up) env_typesr
        LOG.debug("verify listClassMembers: start");
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        // (3.2)
        // (down) env_types
        LOG.debug("verify listClassBody: start");
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody: end");
    }

    /**
     * Method to generate code for all Class declarations.
     * 
     * @param compiler
     *                     Decac Compiler
     * @param currentClass
     *                     Current Class in which the class is declared
     */
    public void codeGenDeclClassList(DecacCompiler compiler) {
        for (AbstractDeclClass declClass : this.getList()) {
            declClass.codeGenDeclClass(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

}
