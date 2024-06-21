package fr.ensimag.deca.context;

import fr.ensimag.deca.CLIException;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.syntax.AbstractDecaLexer;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import java.io.IOException;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
 * 
 * @author Ensimag
 * @date 14/04/2024
 */
public class ManualTestContext {
    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.OFF);
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);

        String[] custom_args = {};
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(custom_args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            System.exit(1);
        }

        DecacCompiler compiler = new DecacCompiler(options, null);
        parser.setDecacCompiler(compiler);
        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
        if (prog == null) {
            System.exit(1);
            return; // Unreachable, but silents a warning.
        }
        try {
            prog.verifyProgram(compiler);
        } catch (LocationException e) {
            e.display(System.err);
            System.exit(1);
        }
        prog.prettyPrint(System.out);
    }
}
