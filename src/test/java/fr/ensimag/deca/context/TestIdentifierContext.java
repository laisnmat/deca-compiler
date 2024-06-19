package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;

import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import net.bytebuddy.asm.Advice.Local;

public class TestIdentifierContext {
    final Type INT = new IntType(new SymbolTable().create("int"));
    final Type FLOAT = new FloatType(new SymbolTable().create("float"));
    final Type BOOLEAN = new BooleanType(new SymbolTable().create("boolean"));
    final Type CLASS1 = new ClassType(new SymbolTable().create("class1"));
    final Type CLASS2 = new ClassType(new SymbolTable().create("class2"));
    final Type NULL1 = new NullType(new SymbolTable().create("null1"));

    @Mock
    Identifier ident;
    @Mock
    Definition def;
    @Mock
    EnvironmentExp localEnv;
    @Mock
    ExpDefinition dExpDefinition;

    DecacCompiler compiler;

    @BeforeEach
    public void setup() throws ContextualError {
        CompilerOptions co = new CompilerOptions();
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(co, null);
        when(dExpDefinition.getType()).thenReturn(BOOLEAN);
        when(localEnv.get(any())).thenReturn(dExpDefinition);
        ident = new Identifier(new SymbolTable().create("x"));
    }

    @Test
    public void test() throws ContextualError {
        // check the result
        DecacInternalError thrown = assertThrows(
                DecacInternalError.class, () -> {
                    ident.checkAllDecorations();
                });
        Assertions.assertEquals(
                "Identifier x has no attached Definition",
                thrown.getMessage());
        ident.setDefinition(new FieldDefinition(BOOLEAN, null, null, null, 0));
        DecacInternalError thrown2 = assertThrows(
                DecacInternalError.class, () -> {
                    ident.getClassDefinition();
                });
        Assertions.assertEquals(
                "Identifier x is not a class identifier, you can't call getClassDefinition on it",
                thrown2.getMessage());
        DecacInternalError thrown3 = assertThrows(
                DecacInternalError.class, () -> {
                    ident.getMethodDefinition();
                });
        Assertions.assertEquals(
                "Identifier x is not a method identifier, you can't call getMethodDefinition on it",
                thrown3.getMessage());
        DecacInternalError thrown5 = assertThrows(
                DecacInternalError.class, () -> {
                    ident.getVariableDefinition();
                });
        Assertions.assertEquals(
                "Identifier x is not a variable identifier, you can't call getVariableDefinition on it",
                thrown5.getMessage());
        ident.setDefinition(def);
        DecacInternalError thrown4 = assertThrows(
                DecacInternalError.class, () -> {
                    ident.getExpDefinition();
                });
        Assertions.assertEquals(
                "Identifier x is not a Exp identifier, you can't call getExpDefinition on it",
                thrown4.getMessage());
        ContextualError thrown6 = assertThrows(
                ContextualError.class, () -> {
                    ident.verifyExpr(compiler, localEnv, null);
                });
        Assertions.assertEquals(
                "Identifier x is not a variable, a parameter or a field.",
                thrown6.getMessage());
        ContextualError thrown7 = assertThrows(
                ContextualError.class, () -> {
                    ident.verifyFieldIdent(compiler, localEnv, null);
                });
        Assertions.assertEquals(
                "Identifier x is not a field.",
                thrown7.getMessage());
        ContextualError thrown8 = assertThrows(
                ContextualError.class, () -> {
                    ident.verifyMethodIdent(compiler, localEnv, null);
                });
        Assertions.assertEquals(
                "Identifier x is not a method.",
                thrown8.getMessage());
    }
}
