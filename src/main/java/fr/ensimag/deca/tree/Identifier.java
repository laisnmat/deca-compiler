package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Deca Identifier
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Identifier extends AbstractIdentifier {
    private static final Logger LOG = Logger.getLogger(Identifier.class);

    private Definition definition;

    private final Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    /**
     * Implements terminal "Identifier" of [SyntaxeContextuelle] in the 3 passes.
     * Verify rule (0.2).
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.verifyLValueIdent(compiler, localEnv, currentClass);
        // identifier ↓env_exp ↑(field(__, __), type)
        // identifier ↓env_exp ↑(param, type)
        // identifier ↓env_exp ↑(var, type);
        Definition def = getDefinition();
        if (!def.isVar() && !def.isParam() && !def.isField()) {
            throw new ContextualError(
                    "Incompatible Types: Identifier " + this.getName() + " is not a variable, a parameter or a field.",
                    getLocation());
        }
        return this.getType();
    }

    public Type verifyLValueIdent(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Identifier: checking if " + this.name + " is defined in the current environment");

        // getting the definition associated with the current Identifier if it exists
        ExpDefinition def = localEnv.get(this.name);
        // if the current Identifier has no definition associated, that means it has
        // never been defined.
        if (def == null) {
            throw new ContextualError("NullDefinition: Identifier " + this.getName() + " is never defined.",
                    getLocation());
        }
        setType(def.getType());
        setDefinition(def);
        return def.getType();
    }

    @Override
    public Type verifyFieldIdent(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // identifier ↓env_exp ↑(field(__, __), type)
        this.verifyLValueIdent(compiler, localEnv, currentClass);
        Definition def = getDefinition();
        if (!def.isField()) {
            throw new ContextualError("Incompatible Types: Identifier " + this.getName() + " is not a field.",
                    getLocation());
        }
        return this.getType();
    }

    @Override
    public Signature verifyMethodIdent(DecacCompiler compiler, EnvironmentExp envExp, ClassDefinition currentClass)
            throws ContextualError {
        this.verifyLValueIdent(compiler, envExp, currentClass);
        try {
            return this.getMethodDefinition().getSignature();
        } catch (DecacInternalError e) {
            throw new ContextualError("Incompatible Types: Identifier " + this.getName() + " is not a method.",
                    getLocation());
        }
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Type: setting type");

        // getting the definition associated with the current Identifier if it exists
        TypeDefinition typeDefinition = compiler.environmentType.defOfType(this.name);

        // if the current Identifier (representing a Type) has no definition associated,
        // that means it has never been defined.
        if (typeDefinition == null) {
            throw new ContextualError("Incompatible Types: Type " + this.getName() + " is never defined.",
                    getLocation());
        }

        // setting Definition and Type of this Identifier
        this.setDefinition(typeDefinition);
        this.setType(typeDefinition.getType());
        return typeDefinition.getType();
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new LOAD(this.getVariableDefinition().getOperand(), register));
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        this.codeGenExp(compiler, Register.R0);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
        compiler.addInstruction(jumpOnTrue ? new BNE(label) : new BEQ(label));
    }

    @Override
    public DVal getDVal() {
        return this.getExpDefinition().getOperand();
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

}
