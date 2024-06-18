package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Equals;

public class TestEqualsContext {
    final Type INT = new IntType(new SymbolTable().create("int"));
    final Type FLOAT = new FloatType(new SymbolTable().create("float"));
    final Type BOOLEAN = new BooleanType(new SymbolTable().create("boolean"));

    @Mock
    AbstractExpr intexpr1;
    @Mock
    AbstractExpr intexpr2;
    @Mock
    AbstractExpr floatexpr1;
    @Mock
    AbstractExpr floatexpr2;
    @Mock
    AbstractExpr boolexpr1;
    @Mock
    AbstractExpr boolexpr2;

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
        when(boolexpr1.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
        when(boolexpr2.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
    }

    @Test
    public void testBoolBool() throws ContextualError {
        Equals t = new Equals(boolexpr1, boolexpr2);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        // check that the mocks have been called properly.
        verify(boolexpr1).verifyExpr(compiler, null, null);
        verify(boolexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testBoolInt() throws ContextualError {
        Equals t = new Equals(boolexpr1, intexpr1);
        // check the result
        ContextualError thrown = assertThrows(
                ContextualError.class, () -> {
                    t.verifyExpr(compiler, null, null);
                });
        Assertions.assertEquals(
                "Incompatible Types:  '==' between boolean and int",
                thrown.getMessage());
        // check that the mocks have been called properly.
        verify(boolexpr1, times(2)).verifyExpr(compiler, null, null);
        verify(intexpr1, times(2)).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntBool() throws ContextualError {
        Equals t = new Equals(intexpr1, boolexpr1);
        // check the result
        ContextualError thrown = assertThrows(
                ContextualError.class, () -> {
                    t.verifyExpr(compiler, null, null);
                });
        Assertions.assertEquals(
                "Incompatible Types:  '==' between int and boolean",
                thrown.getMessage());
        // check that the mocks have been called properly.
        verify(intexpr1, times(2)).verifyExpr(compiler, null, null);
        verify(boolexpr1, times(2)).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntInt() throws ContextualError {
        Equals t = new Equals(intexpr1, intexpr2);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        Equals t = new Equals(floatexpr1, floatexpr2);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        // check that the mocks have been called properly.
        verify(floatexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Equals t = new Equals(intexpr1, floatexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        assertTrue(t.getLeftOperand() instanceof ConvFloat);
        assertFalse(t.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1, times(2)).verifyExpr(compiler, null, null);
        verify(floatexpr1, times(2)).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Equals t = new Equals(floatexpr1, intexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        assertFalse(t.getLeftOperand() instanceof ConvFloat);
        assertTrue(t.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(floatexpr1, times(2)).verifyExpr(compiler, null, null);
        verify(intexpr1, times(2)).verifyExpr(compiler, null, null);
    }
}
