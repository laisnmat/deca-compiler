package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl03
 * @date 14/04/2024
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
                condition.verifyExpr(compiler, localEnv,currentClass);
                if (condition.getType() != compiler.environmentType.BOOLEAN){
                    throw new ContextualError("La condition n'est pas une expression booléenne", getLocation());
                }
                thenBranch.verifyListInst(compiler, localEnv,currentClass, returnType);
                elseBranch.verifyListInst(compiler, localEnv,currentClass, returnType);

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        String elseLabel = compiler.labelManager.getLabel("else");
        String endLabel = compiler.labelManager.getLabel("endIfThenElse");
        condition.codeGenExpBool(compiler, false, elseLabel);

        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(new Label(endLabel)));

        compiler.addLabel(new Label(elseLabel));
        elseBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(new Label(endLabel)));
        compiler.addLabel(new Label(endLabel));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
