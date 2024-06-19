package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelBoolExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     *         in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed
     * by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     *
     * implements non-terminals "expr" and "lvalue"
     * of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler
     *                     (contains the "env_types" attribute)
     * @param localEnv
     *                     Environment in which the expression should be checked
     *                     (corresponds to the "env_exp" attribute)
     * @param currentClass
     *                     Definition of the class containing the expression
     *                     (corresponds to the "class" attribute)
     *                     is null in the main bloc.
     * @return the Type of the expression
     *         (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     *
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler
     *                     contains the "env_types" attribute
     * @param localEnv
     *                     corresponds to the "env_exp" attribute
     * @param currentClass
     *                     corresponds to the "class" attribute
     * @param expectedType
     *                     corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
        this.verifyExpr(compiler, localEnv, currentClass);

        // if the type is an int but the expected type is a float,
        // then perform a convertion from int to float
        if (this.getType().isInt() && expectedType.isFloat()) {
            AbstractExpr convExpr = new ConvFloat(this);
            convExpr.verifyExpr(compiler, localEnv, currentClass);
            return convExpr;
        }

        // if the RValue's type is not the same or a subtype of the LValue's type,
        // then throw an exception
        if (!assignCompatible(compiler, expectedType, this.getType())) {
            throw new ContextualError(
                    "Incompatible Types: RValue must have the same type or subtype as LValue: "
                            + this.getType()
                            + " instead of "
                            + expectedType,
                    getLocation());
        }

        // the expected type is the same as the current type
        return this;
    }

    protected boolean assignCompatible(DecacCompiler compiler, Type t1, Type t2) {
        if (subtype(t2, t1)
                || t2.sameType(t1) || (t1.isFloat() && t2.isInt())) {
            return true;
        }
        return false;
    }

    protected boolean subtype(Type t1, Type t2) {
        if ((t1.isNull() && t2.isClassOrNull()) || (t1.isClass() && t2.isClass()
                && ((ClassType) t1).isSubClassOf((ClassType) t2))) {
            return true;
        }
        return false;
    }

    protected boolean castCompatible(DecacCompiler compiler, Type t1, Type t2)
            throws ContextualError {
        if (t1.isVoid()) {
            throw new ContextualError("Incompatible Types:  Cannot cast a void type", getLocation());
        }
        if (assignCompatible(compiler, t1, t2) || assignCompatible(compiler, t2, t1)) {
            return true;
        }
        return false;
    }

    /**
     * Verify an AbstractExpr.
     * As this object is an AbstractExpr, this method will execute verifyExpr by
     * default.
     * Can be overidden in subclasses.
     */
    @Override
    protected boolean verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        verifyExpr(compiler, localEnv, currentClass);
        return false;
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *                     Environment in which the condition should be checked.
     * @param currentClass
     *                     Definition of the class containing the expression, or
     *                     null in
     *                     the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = this.verifyExpr(compiler, localEnv, currentClass);

        // the expression should be of type boolean as it is a condition
        if (type != compiler.environmentType.BOOLEAN) {
            throw new ContextualError(
                    "Incompatible Types: The condition should be of type 'boolean' but is of type '" + type + "'.",

                    getLocation());
        }
    }

    /**
     * Evaluate the expression and stores its result in the register
     * Register.getR(register_name)
     * 
     * @param compiler
     * @param register
     *                 The register where the result is stored.
     */
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Evaluate the expression and returns a Dval that contains the result.
     *
     * @return The DVal of the Register 2: all results will always be stored in the
     *         2th register.
     */
    public DVal getDVal() {
        return null;
    }

    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     * @param printHex
     *                 whether to print the value as hexadecimal
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        this.codeGenExp(compiler, Register.getR(2));
        Register r2 = Register.getR(2);
        compiler.addInstruction(new LOAD(r2, Register.R1));
        if (this.getType().isFloat()) {
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        } else if (this.getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else {
            throw new DecacInternalError("Unexpected case in AbstractExpr.codeGenPrint: please report this bug.");
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, AbstractIdentifier methodName) {
        // if this instruction is of type boolean,
        // then generate it as a condition,
        // else generate it as an expression.
        if (this.getType() != null && this.getType().isBoolean()) {
            // label for this boolean expression
            LabelBoolExp labelBoolExp = compiler.labelManager.getBoolExp();
            this.codeGenCondition(compiler, false, labelBoolExp.getEndLabel());
            compiler.addLabel(labelBoolExp.getEndLabel());
            labelBoolExp.destruct();
        } else {
            // by default, store the result of this expression in the register R2
            this.codeGenExp(compiler, GPRegister.getR(2));
        }
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    protected void addComment(DecacCompiler compiler, GPRegister register) {
        compiler.addComment("codeExp(" + decompile() + ", " + register + ")");
    }
}
