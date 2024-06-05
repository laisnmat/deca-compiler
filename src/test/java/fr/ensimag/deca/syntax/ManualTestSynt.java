package fr.ensimag.deca.syntax;

import fr.ensimag.deca.CLIException;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractProgram;
import java.io.File;
import java.io.IOException;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Driver to test the Parser (and lexer).
 *
 * @author Ensimag
 * @date 14/04/2024
 */
public class ManualTestSynt {
    public static void main(String[] args) throws IOException {
        // Uncomment the following line to activate debug traces
        // unconditionally for test_synt
        // Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        File file = null;
        if (lex.getSourceName() != null) {
            file = new File(lex.getSourceName());
        }

        String[] custom_args = {"-d", "-d"};
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(custom_args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            System.exit(1);
        }

        final DecacCompiler decacCompiler = new DecacCompiler(options, file);
        parser.setDecacCompiler(decacCompiler);
        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
        if (prog == null) {
            System.exit(1);
        } else {
            prog.prettyPrint(System.out);
        }
    }
}
