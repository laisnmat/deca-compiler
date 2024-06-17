package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodAsmBody extends AbstractMethodBody {
    private StringLiteral asm;

    public MethodAsmBody(StringLiteral asm) {
        this.asm = asm;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        asm.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        // List<String> listAsm = new ArrayList<>();
        // listAsm = Arrays.asList(asm.getValue().split("\n"));
        // for (String line : listAsm) {
        // compiler.add(new InlinePortion(line));
        // }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("{\n");
        s.indent();
        s.println(asm.getValue());
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asm.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        asm.iter(f);
    }

}
