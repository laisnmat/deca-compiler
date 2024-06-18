package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(DeclMethod.class);

    private final AbstractIdentifier type;
    private final AbstractIdentifier methodName;
    private final ListDeclParam params;
    private final AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam params,
            AbstractMethodBody body) {
        this.type = type;
        this.methodName = methodName;
        this.params = params;
        this.body = body;
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        // (2.7)
        // type ↓env_types ↑type
        type.verifyType(compiler);

        // list_decl_param ↓env_types ↑sig
        Signature sig = params.verifyListDeclParam(compiler, currentClass);
        // si (class(__, env_exp_super), __) ≜ env_types(super)
        // et env_exp_super(name) est défini
        // alors (method(sig2), type2) ≜ env_exp_super(name)
        EnvironmentExp envExp = currentClass.getMembers();
        EnvironmentExp envExpSuper = currentClass.getSuperClass().getMembers();
        ExpDefinition extendsDefinitionSuper;
        if ((extendsDefinitionSuper = envExpSuper.get(methodName.getName())) != null) {
            MethodDefinition extendMethodDefinitionSuper = extendsDefinitionSuper.asMethodDefinition(
                    "super method `" + methodName.getName() + "` is not a method",
                    getLocation());
            // et sig = sig2
            if (!sig.sameSignature(extendMethodDefinitionSuper.getSignature())) {
                throw new ContextualError(
                        "DoubleDefException: Method `" + methodName.getName()
                                + "` as not the same signature as his parent method",
                        methodName.getLocation());
            }
            // et subtype(env_types, type, type2)
            if (!(type.getType().isClass() && extendMethodDefinitionSuper.getType().isClass()
                    && ((ClassType) type.getType()).isSubClassOf((ClassType) extendMethodDefinitionSuper.getType()))
                    && !(type.getType().sameType(extendMethodDefinitionSuper.getType()))) {
                throw new ContextualError(
                        "Incompatible Types: Method `" + methodName.getName()
                                + "` as not the same return type as his parent method",
                        methodName.getLocation());
            }
        }
        // ↑{name 7→ (method(sig), type)}
        MethodDefinition methodDefinition = new MethodDefinition(type.getType(), getLocation(), sig, currentClass,
                currentClass.getNumberOfMethods(), new EnvironmentExp(envExp));
        try {
            envExp.declare(methodName.getName(), methodDefinition);
            currentClass.incNumberOfMethods();
        } catch (DoubleDefException e) {
            ExpDefinition previous = envExp.get(methodName.getName());
            throw new ContextualError(
                    "DoubleDefException: Method `" + methodName.getName() + "` already declared at "
                            + previous.getLocation(),
                    methodName.getLocation());
        }
        methodName.setType(methodDefinition.getType());
        methodName.setDefinition(methodDefinition);
    }

    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        // (3.11)
        // method localenv
        EnvironmentExp localEnv = currentClass.getMembers().get(this.methodName.getName()).asMethodDefinition(
                "Method `" + this.methodName.getName() + "` is not a method", this.getLocation()).getLocalEnv();
        // type ↓env_types ↑return
        type.verifyType(compiler);
        // list_decl_param ↓env_types ↑env_exp_params
        params.verifyListDeclParamInit(compiler, localEnv);
        // method_body ↓env_types ↓env_exp ↓env_exp_params ↓class ↓return
        body.verifyMethodBody(compiler, localEnv, currentClass, type.getType());
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) {
        body.codeGenMethodBody(compiler, currentClass, this.methodName);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        params.decompile(s);
        s.println(") {");
        body.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        params.iter(f);
        body.iter(f);
    }

}
