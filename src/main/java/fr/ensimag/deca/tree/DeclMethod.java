package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(DeclMethod.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam params;
    final private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam params,
            AbstractMethodBody body) {
        this.type = type;
        this.methodName = methodName;
        this.params = params;
        this.body = body;
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify DeclField: start");
        MethodDefinition defSuper;
        // type verification
        Type t = this.type.verifyType(compiler);
        Signature sig = params.verifyListParam(compiler, localEnv, currentClass);
        // create the definition of this field
        currentClass.incNumberOfMethods();
        MethodDefinition methodDef = new MethodDefinition(t, getLocation(), sig, currentClass.getNumberOfMethods());
        try {
            currentClass.getMembers().declare(this.methodName.getName(), methodDef);
        } catch (EnvironmentExp.DoubleDefException dde) {
            throw new ContextualError("Field "
                    + this.methodName.getName()
                    + " is already defined in this class.",
                    getLocation());
        }
        if (localEnv != null && localEnv.get(methodName.getName()) != null) {
            defSuper = localEnv.get(methodName.getName())
                    .asMethodDefinition(
                            "ContextualError: expected a methodDefinition",
                            getLocation());
            if (!sig.sameSignature(defSuper.getSignature())) {
                throw new ContextualError("Method "
                        + this.methodName.getName()
                        + " is already defined in the superClass with a different signature.",
                        getLocation());
            }
            if (!t.sameType(defSuper.getType()) && (!t.isClass() || !defSuper.getType().isClass()
                    || !((ClassType) t).isSubClassOf((ClassType) defSuper.getType()))) {
                throw new ContextualError("Method "
                        + this.methodName.getName()
                        + " is already defined in the superClass with a different return type.",
                        getLocation());
            }
        }
    }

    @Override
    protected int codeGenDeclMethod(DecacCompiler compiler, int localBaseOffset) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        s.println("}");
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
