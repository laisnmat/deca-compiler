package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl03
 * @date 14/04/2024
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Iterator<AbstractDeclVar> iterator = this.iterator();
                for (Iterator<AbstractDeclVar> it = iterator; it.hasNext(); ) {
                    AbstractDeclVar DeclV = it.next();
                    DeclV.verifyDeclVar(compiler, localEnv, currentClass);
                }
    }

    public void codeGenListDeclVar(DecacCompiler compiler) {
        int n = 0; // turn into size of list of classes ? -> so 0 for non object oriented
        for (AbstractDeclVar declV : getList()) {
            // first set operand as d(GB) with d increasing with each global var
                // then later on, get operand to use in expressions like this :
                    // DeclV.getExpDefinition().getOperand(); // get address (memory location)
            declV.codeGenDeclVar(compiler);
        }
    }



}
