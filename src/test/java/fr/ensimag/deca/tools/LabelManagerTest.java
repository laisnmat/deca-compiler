package fr.ensimag.deca.tools;

import fr.ensimag.deca.codegen.LabelManager;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LabelManagerTest {
    public enum LabelNames {
        endLabel("end"),
        startWhileLabel("startWhile"),
        endWhileLabel("endWhile"),
        elseLabel("else"),
        endIfThenElseLabel("endIfThenElse");

        public final String labelString;

        private LabelNames(String labelString) {
            this.labelString = labelString;
        }
    }

    @Test
    public void testLabelManager() throws UnsupportedEncodingException {
        // TODO
        new LabelManager().getAnd();
    }
}
