package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl03
 * @date 14/04/2024
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

                Type t = type.verifyType(compiler);
                TypeDefinition typeDef = compiler.environmentType.defOfType(t.getName());
                type.setDefinition(typeDef);
                if (t.isVoid())
                    throw new ContextualError("type cannot be void", getLocation());
                try {
                    ExpDefinition def = new VariableDefinition(t, getLocation());
                    localEnv.declare(varName.getName(), def);
                    varName.setDefinition(def);
                }
                catch (EnvironmentExp.DoubleDefException e){
                    throw new ContextualError("identifier is already defined", getLocation());
                    }
                
                initialization.verifyInitialization(compiler, t, localEnv, currentClass);
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        RegisterOffset offset = new RegisterOffset(compiler.getOffset(), Register.GB);
        VariableDefinition varDef = varName.getVariableDefinition();
        varDef.setOperand(offset); // assign offset(GB) to variable
        compiler.increaseOffset(); // increase offset for next variable

        if(initialization instanceof Initialization) {
            Initialization i = (Initialization)initialization;
            int storageRegister = 2; // register number where rhs of initialization expr is stored
            i.getExpression().codeGenExp(compiler, storageRegister);
            compiler.addInstruction(new STORE(Register.getR(storageRegister), varName.getExpDefinition().getOperand()));
        }
        else if (initialization instanceof NoInitialization) {
            // do nothing ?
        }
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
