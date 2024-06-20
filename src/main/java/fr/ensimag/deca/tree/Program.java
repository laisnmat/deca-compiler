package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.lang.reflect.Method;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    private DeclObject declObj;

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
        AbstractDeclClass declObj = new DeclObject();
        this.declObj = (DeclObject) declObj;
        this.initEqualsMethodDef(compiler);
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

    private void initEqualsMethodDef(DecacCompiler compiler) {
        Signature equalsSign = new Signature();
        equalsSign.add(compiler.environmentType.OBJECT);
        ClassDefinition defObj = compiler.environmentType.OBJECT.getDefinition();

        MethodDefinition methDef = new MethodDefinition(compiler.environmentType.BOOLEAN, Location.BUILTIN, equalsSign,
                defObj, 1, null);
        try {
            defObj.getMembers().declare(compiler.symbolTable.create("equals"), methDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new RuntimeException(e);
        }
        defObj.incNumberOfMethods();
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        compiler.addComment("Class declarations program");
        this.addObjectDeclClass(compiler);
        classes.codeGenDeclClassList(compiler);
        compiler.addInstruction(new BRA(compiler.startMainLabel));
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
        this.addObjectMethodEquals(compiler);
        classes.codeGenDeclMethodsList(compiler);
    }

    private void addObjectMethodEquals(DecacCompiler compiler) {
        compiler.addComment("Code de la méthode Equals");
        String label = "code.Object.Equals";
        compiler.addLabel(new Label(label));

        // test stack overflow
        TSTO methodTSTO = new TSTO(0);
        compiler.addInstruction(methodTSTO, "Test de débordement de la pile");

        // save registers
        compiler.addComment("Sauvegarde des registres");
        int numParams = 1;
        for (int i = 0; i < numParams; i++) {
            compiler.addInstruction(new PUSH(Register.getR(i + 2)));
            compiler.stackManager.incrementPushCount();
        }
        String trueMethod = label.replace("code", "true");
        String endMethod = label.replace("code", "end");
        RegisterOffset thisOffset = new RegisterOffset(-2, Register.LB);
        RegisterOffset paramOffset = new RegisterOffset(-3, Register.LB);
        Label trueLabel = new Label(trueMethod);
        Label endLabel = new Label(endMethod);

        RegisterOffset offset0 = new RegisterOffset(0, Register.getR(2));
        RegisterOffset offset1 = new RegisterOffset(0, Register.getR(3));

        compiler.addInstruction(new LOAD(thisOffset, Register.getR(2)));
        compiler.addInstruction(new LEA(offset0, Register.getR(2)));

        compiler.addInstruction(new LOAD(paramOffset, Register.getR(3)));
        compiler.addInstruction(new LEA(offset1, Register.getR(3)));

        compiler.addInstruction(new CMP(Register.getR(2), Register.getR(3)));
        compiler.addInstruction(new BEQ(trueLabel));

        // false
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        compiler.addInstruction(new BRA(endLabel));
        // true
        compiler.addLabel(trueLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R0));
        compiler.addLabel(endLabel);

        ImmediateInteger tstoVal = new ImmediateInteger(compiler.stackManager.getTSTOValue());
        methodTSTO.setOperand(tstoVal);
        // restore registers
        for (int i = numParams; i > 0; i--) {
            compiler.addInstruction(new POP(Register.getR(i + 2)));
            compiler.stackManager.decrementPushCount();
        }
        compiler.addInstruction(new RTS());
    }

    private void addObjectDeclClass(DecacCompiler compiler) {

        ListDeclClass copy = new ListDeclClass();
        copy.add(declObj);
        for (AbstractDeclClass declClass : classes.getList()) {
            copy.add(declClass);
        }
        classes = copy;
        RegisterOffset offset = new RegisterOffset(1, Register.GB);
        compiler.environmentType.OBJECT.setAddr(offset);
    }

    ;

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
