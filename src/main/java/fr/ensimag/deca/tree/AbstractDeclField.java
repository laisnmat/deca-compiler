package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

/**
 *
 * @author gl03
 * @date 11/06/2024
 */
public abstract class AbstractDeclField extends Tree {
        /**
         * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
         *
         * @param compiler
         *                     contains "env_types" attribute
         * @param localEnv
         *                     its "parentEnvironment" corresponds to the "env_exp_sup"
         *                     attribute
         *                     in precondition, its "current" dictionary corresponds to
         *                     the "env_exp" attribute
         *                     in postcondition, its "current" dictionary corresponds to
         *                     the synthetized attribute
         * @param currentClass
         *                     corresponds to the "class" attribute (null in the main
         *                     bloc).
         */
        protected abstract void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass, int index)
                        throws ContextualError;

        protected abstract void verifyDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
                        throws ContextualError;

        /**
         * Generate assembly code for the fields initialisation.
         *
         * @param compiler
         * @param initExplicit
         */
        protected abstract void codeGenDeclField(DecacCompiler compiler, boolean initExplicit);
}
