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
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.AbstractDeclClass;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.And;
import fr.ensimag.deca.tree.Equals;

public class TestAbstractOpExactCmpContext {
    final Type INT = new IntType(new SymbolTable().create("int"));
    final Type FLOAT = new FloatType(new SymbolTable().create("float"));
    final Type BOOLEAN = new BooleanType(new SymbolTable().create("boolean"));
    final Type CLASS1 = new ClassType(new SymbolTable().create("class1"));
    final Type CLASS2 = new ClassType(new SymbolTable().create("class2"));
    final Type NULL1 = new NullType(new SymbolTable().create("null1"));

    @Mock
    AbstractExpr intexpr1;
    @Mock
    AbstractExpr boolexpr1;
    @Mock
    AbstractExpr declClass1;
    @Mock
    AbstractExpr declClass2;
    @Mock
    AbstractExpr declNull;
    DecacCompiler compiler;

    @BeforeEach
    public void setup() throws ContextualError {
        CompilerOptions co = new CompilerOptions();
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(co, null);
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        // when(boolexpr1.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
        when(declClass1.verifyExpr(compiler, null, null)).thenReturn(CLASS1);
        when(declClass2.verifyExpr(compiler, null, null)).thenReturn(CLASS2);
        when(declNull.verifyExpr(compiler, null, null)).thenReturn(NULL1);
    }

    @Test
    public void testClassClass() throws ContextualError {
        Equals t = new Equals(declClass1, declClass2);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        // check that the mocks have been called properly.
        verify(declClass1).verifyExpr(compiler, null, null);
        verify(declClass2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntClass() throws ContextualError {
        Equals t = new Equals(intexpr1, declClass2);
        // check the result
        ContextualError thrown = assertThrows(
                ContextualError.class, () -> {
                    t.verifyExpr(compiler, null, null);
                });
        Assertions.assertEquals(
                "Incompatible Types:  '==' between int and class2",
                thrown.getMessage());
        // check that the mocks have been called properly.
        verify(intexpr1, times(2)).verifyExpr(compiler, null, null);
        verify(declClass2, times(2)).verifyExpr(compiler, null, null);
    }

    @Test
    public void testClassNull() throws ContextualError {
        Equals t = new Equals(declNull, declClass2);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        // check that the mocks have been called properly.
        verify(declNull).verifyExpr(compiler, null, null);
        verify(declClass2).verifyExpr(compiler, null, null);
    }
}
