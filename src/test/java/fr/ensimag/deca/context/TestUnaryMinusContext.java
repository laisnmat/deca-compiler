package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.ensimag.deca.tree.UnaryMinus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.UnaryMinus;

public class TestUnaryMinusContext {
    final Type INT = new IntType(null);
    final Type FLOAT = new FloatType(null);

    @Mock
    AbstractExpr intexpr1;
    @Mock
    AbstractExpr intexpr2;
    @Mock
    AbstractExpr floatexpr1;
    @Mock
    AbstractExpr floatexpr2;

    DecacCompiler compiler;

    @BeforeEach
    public void setup() throws ContextualError {
        CompilerOptions co = new CompilerOptions();
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(co, null);
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
    }

    @Test
    public void testIntInt() throws ContextualError {
        UnaryMinus t = new UnaryMinus(intexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        UnaryMinus t = new UnaryMinus(intexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isFloat());

        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError {
        UnaryMinus t = new UnaryMinus(floatexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isFloat());

        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testType() throws ContextualError {
        CompilerOptions co = new CompilerOptions();
        DecacCompiler compiler = new DecacCompiler(co, null);
        AbstractExpr left = Mockito.mock(AbstractExpr.class);
        when(left.verifyExpr(compiler, null, null)).thenReturn(INT);
        AbstractExpr right = Mockito.mock(AbstractExpr.class);
        when(right.verifyExpr(compiler, null, null)).thenReturn(INT);
        UnaryMinus t = new UnaryMinus(left);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(left).verifyExpr(compiler, null, null);
        verify(right).verifyExpr(compiler, null, null);
    }
}
