package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.*;
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
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Multiply;

public class TestOpArithContext {
    final Type INT = new IntType(null);

    final Type BOOLEAN = new BooleanType(null);
    @Mock
    AbstractExpr intexpr1;
    @Mock
    AbstractExpr intexpr2;
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
        when(boolexpr1.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
        when(boolexpr2.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
    }

    @Test
    public void testOpARithInvalidType() throws ContextualError {
        CompilerOptions co = new CompilerOptions();
        DecacCompiler compiler = new DecacCompiler(co, null);
        AbstractExpr left = Mockito.mock(AbstractExpr.class);
        when(left.verifyExpr(compiler, null, null)).thenReturn(INT);
        AbstractExpr right = Mockito.mock(AbstractExpr.class);
        when(right.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
        Multiply t = new Multiply(left, right);
        // check the result
        ContextualError thrown = assertThrows(
                ContextualError.class, () -> {
                    t.verifyExpr(compiler, null, null);
                });
        Assertions.assertEquals("Incompatible types \'" + INT.getName() + "\' with \'" + BOOLEAN.getName() + "\'",
                thrown.getMessage());
        // check that the mocks have been called properly.
        verify(left).verifyExpr(compiler, null, null);
        verify(right).verifyExpr(compiler, null, null);
    }
}
