package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import fr.ensimag.ima.pseudocode.Register;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl03
 * @date 14/04/2024
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParseDecompile() {
        return parseDecompile;
    }

    public int getRegisterOption() {
        return registerOption;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public boolean getOnlyVerif() {
        return onlyVerif;
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parseDecompile = false;priate
    int registerOption = 15;
    private boolean onlyVerif = fals
    private List<File> sourceFiles = new ArrayList<File>();pubic

    void parseArgs(String[] args) throws CLIException

        boolean parsingR = false; // if -r option is detected, next arg must be an int value
        List<String> argsList = new ArrayList<String>();
        for (String arg : args) {
            // parse options
            if (parsingR) {
                registerOption = Integer.parseInt(arg) - 1;
                parsingR = false;
                continue;
            }
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-p":
                        if (onlyVerif) {
                            throw new CLIException("Cannot use option -v and -p at the same time");
                        }
                        if (parseDecompile){
                            throw new CLIException("Cannot use the same option twice");
                        }
                        argsList.add("-p");
                        parseDecompile = true;
                        break;
                    case "-d":
                        if (debug < TRACE) {
                            debug++;
                        }
                        break;
                    case "-r":
                        if (argsList.contains("-r")) {
                            throw new CLIException("Cannot use the same option twice");
                        }
                        argsList.add("-r");
                        parsingR = true; // Meaning next arg must be an int
                        break;
                    case "-v":
                        if (parseDecompile) {
                            throw new CLIException("Cannot use argument -v and -p at the same time");
                        }
                        if (onlyVerif) {
                            throw new CLIException("Cannot use argument -v multiple times");
                        }
                        argsList.add("-v");
                        onlyVerif = true;
                        break;
                    default:
                        throw new CLIException("Option not recognized: " + arg);
                }
                continue;
            }

            // parse filename
            if (arg.endsWith(".deca")) {
                this.sourceFiles.add(new File(arg));
                continue;
            }

            // else the provided file does not have the right ".deca" extension
            throw new CLIException(
                    "Argument not recognized: the provided file does not have the extension \".deca\": " + arg);
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
            case QUIET:
                break; // keep default
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case TRACE:
                logger.setLevel(Level.TRACE);
                break;
            default:
                logger.setLevel(Level.ALL);
                break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        System.out.println("-b          (banner)        : prints a banner with the team name");
        System.out.println("-p          (parse)         : stops decac after the construction of the syntax tree");
        System.out.println("-v          (verification)  : stops decac after the verification of the syntax tree");
        System.out.println(
                "-n          (no check)      : removes the tests at execution specified by points 11.1 and 11.3 of Deca semantics");
        System.out.println(
                "-r X        (registers)     : limits the available registers to R0 ... R{X-1}, with 4 <= X <= 16");
        System.out.println(
                "-d          (debug)         : activates debug trace - repeat the option multiple times for more info");
        System.out.println(
                "-P          (parallel)      : if there are multiple source files, parallelize their compilation (to accelerate compilation)");
    }

}
