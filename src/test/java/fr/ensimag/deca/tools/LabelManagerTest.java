package fr.ensimag.deca.tools;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.codegen.LabelManager;

public class LabelManagerTest {
    public enum LabelNames {
        endLabel("end"), startWhileLabel("startWhile"), endWhileLabel("endWhile"), elseLabel(
                "else"), endIfThenElseLabel("endIfThenElse");

        public final String labelString;

        private LabelNames(String labelString) {
            this.labelString = labelString;
        }
    }

    @Test
    public void testLabelManager() throws UnsupportedEncodingException {
        new LabelManager().getAnd();
    }
}
