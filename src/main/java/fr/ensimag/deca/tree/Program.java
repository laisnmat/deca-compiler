package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public ListDeclClass getClasses() {
        return classes;
    }

    public AbstractMain getMain() {
        return main;
    }

    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler)
            throws ContextualError {
        LOG.debug("verify program: start");
        // (1.1)
        // (up) env_types, (down) env_type_predef
        classes.verifyListClass(compiler);
        // (2.1)
        // (up) env_types, (down) env_typesr
        classes.verifyListClassMembers(compiler);

        // (3.1)
        // (down) env_types
        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        compiler.addComment("Class declarations program");
        this.addObjectDeclClass(compiler);
        classes.codeGenDeclClassList(compiler);
        compiler.addComment("Main program");

        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());

        compiler.addFirst(new Line(new ADDSP(compiler.stackManager.getGlobalADDSPValue())));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.errorManager.addTSTOError();
            compiler.addFirst(new Line(new BOV(compiler.labelManager.getStackOverflowError())));
            compiler.addFirst(new Line(new TSTO(compiler.stackManager.getTSTOValue())));
        }
        compiler.errorManager.generateErrors(compiler);
        compiler.addFirst(new Line("stack overflow test"));
    }

    private void addObjectDeclClass(DecacCompiler compiler) {

        AbstractDeclClass declObj = new DeclObject();
        ListDeclClass copy = new ListDeclClass();
        copy.add(declObj);
        for (AbstractDeclClass declClass : classes.getList()) {
            copy.add(declClass);
        }
        classes = copy;
        RegisterOffset offset = new RegisterOffset(1, Register.GB);
        compiler.environmentType.OBJECT.setAddr(offset);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
