package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.syntax.DecaParser.ExprContext;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.beans.Expression;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Print statement (print, println, ...).
 *
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractPrint extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(AbstractPrint.class);

    private boolean printHex;
    private ListExpr arguments = new ListExpr();

    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify AbstractPrint: start");
        Type type;
        // verify (3.30) and (3.31) for each AbstractExpr
        for (AbstractExpr expr : this.getArguments().getList()) {
            // verify rules from (3.32) to (3.54), depending on what kind of AbstractExpr is
            // expr
            type = expr.verifyExpr(compiler, localEnv, currentClass);

            // condition verification
            if (!type.isInt() && !type.isFloat() && !type.isString()) {
                throw new ContextualError("Arguments of print must be int, float or string", expr.getLocation());
            }
        }
        LOG.debug("verify AbstractPrint: end");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addComment(decompile());
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, printHex);
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print" + this.getSuffix() + "(");
        int size = this.getArguments().size();
        for (AbstractExpr arg : this.getArguments().getList()) {
            arg.decompile(s);
            if (size > 1) {
                s.print(",");
                size = size - 1;
            }
        }
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
