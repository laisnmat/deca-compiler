package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl03
 * @date 14/04/2024
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            options.displayTeamBanner();
            System.exit(error ? 1 : 0);
        }
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
        }
        if (options.getParallel()) {
            // we create a thread pool with the number of source files
            ExecutorService executor = Executors.newFixedThreadPool(options.getSourceFiles().size());
            // we create a list of futures to store the result of the compilation
            List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
            for (File source : options.getSourceFiles()) {
                // we instanciate a compiler for each source file
                DecacCompiler compiler = new DecacCompiler(options, source);
                // we create a task for each compiler (depends of the options)
                Callable<Boolean> task = new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        // here that's depends of the options (-v, -p, ...)
                        if (options.getParseDecompile()) {
                            return compiler.decompile();
                        } else if (options.getOnlyVerif()) {
                            return compiler.onlyVerif();
                        } else {
                            return compiler.compile();
                        }
                    }
                };
                // we submit the task to the executor
                futures.add(executor.submit(task));
            }
            for (Future<Boolean> future : futures) {
                try {
                    if (future.get()) {
                        error = true;
                    }
                } catch (InterruptedException
                        | ExecutionException e) {
                    e.printStackTrace();
                    error = true;
                }
            }
            executor.shutdown();

        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (options.getParseDecompile()) {
                    if (compiler.decompile()) {
                        error = true;
                    }
                } else if (options.getOnlyVerif()) {
                    if (compiler.onlyVerif()) {
                        error = true;
                    }
                } else {
                    if (compiler.compile()) {
                        error = true;
                    }
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
