package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Main block of a Deca program.
 *
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractMethodBody extends Tree {

    protected abstract void codeGenMethodBody(DecacCompiler compiler);

    /**
     * Implements non-terminal "MethodBody" of [SyntaxeContextuelle] in pass 3
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType) throws ContextualError;
}
