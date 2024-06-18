package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;

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
    protected void codeGenMethodBody(DecacCompiler compiler, ClassDefinition currentClass) {
        // List<String> listAsm = new ArrayList<>();
        // listAsm = Arrays.asList(asm.getValue().split("[\n|\t]"));
        // for (String line : listAsm) {
        // if (line.length() != 0)
        // compiler.add(new InlinePortion("\t" + line));
        // }
        compiler.add(new InlinePortion(asm.getValue()));
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
