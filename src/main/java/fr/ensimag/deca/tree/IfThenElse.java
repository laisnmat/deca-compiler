package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelIf;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

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
    protected boolean verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        boolean returnStatement;
        condition.verifyCondition(compiler, localEnv, currentClass);
        returnStatement = thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        returnStatement = elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType) && returnStatement;
        return returnStatement;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, AbstractIdentifier methodName) {
        LabelIf labelIf = compiler.labelManager.getIf();
        Label endLabel = labelIf.getEndLabel();
        Label elseLabel = labelIf.getElseLabel();
        boolean isElse = !(this.elseBranch.getList().isEmpty());

        // compiler.addComment(labelIf.toString() + ": " + this.decompile());

        // generate condition, that branches to the 'else' label if there is an 'else',
        // else to the 'end' label.
        this.condition.codeGenCondition(compiler, false, (isElse ? elseLabel : endLabel));

        // generate if instructions
        this.thenBranch.codeGenListInst(compiler, methodName);

        // generate else instructions if there is one
        if (isElse) {
            // when the 'if' condition is true, branch to the end
            compiler.addInstruction(new BRA(endLabel));
            compiler.addLabel(elseLabel);

            this.elseBranch.codeGenListInst(compiler, methodName);
        }

        compiler.addLabel(endLabel);

        // delete the label prefix in the prefix of the LabelManager
        labelIf.destruct();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if(");
        condition.decompile(s);
        s.print("){");
        s.println();
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        if (!this.elseBranch.isEmpty()) {
            s.print("} else {"); // A changer pour else if
            s.println();
            s.indent();
            elseBranch.decompile(s);
            s.unindent();
        }
        s.print("}");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
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
