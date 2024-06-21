package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.AbstractInitialization;
import fr.ensimag.deca.tree.And;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.DeclField;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.ListDeclField;
import fr.ensimag.deca.tree.ListDeclMethod;
import fr.ensimag.deca.tree.Visibility;

public class TestDeclFieldContext {
        final Type INT = new IntType(new SymbolTable().create("int"));
        final Type FLOAT = new FloatType(new SymbolTable().create("float"));
        final Type BOOLEAN = new BooleanType(new SymbolTable().create("boolean"));
        final Type CLASS1 = new ClassType(new SymbolTable().create("class1"));
        final Type CLASS2 = new ClassType(new SymbolTable().create("class2"));
        final Type NULL1 = new NullType(new SymbolTable().create("null1"));
        final Type VOID = new VoidType(new SymbolTable().create("void"));

        @Mock
        DeclField declField;
        @Mock
        DeclField declField2;
        @Mock
        EnvironmentExp localEnv;
        @Mock
        Visibility visibility;
        @Mock
        AbstractIdentifier type;
        @Mock
        AbstractIdentifier type2;
        @Mock
        AbstractIdentifier varName;
        @Mock
        AbstractIdentifier className;
        @Mock
        AbstractIdentifier className2;
        @Mock
        AbstractIdentifier superClass;
        @Mock
        AbstractInitialization init;
        @Mock
        ListDeclField listDeclField;
        @Mock
        ListDeclField listDeclField2;
        @Mock
        ListDeclClass listDeclClass;
        @Mock
        ListDeclClass listDeclClass2;
        @Mock
        ClassDefinition defClass;
        @Mock
        DeclClass declClass;
        @Mock
        DeclClass declClass2;
        @Mock
        ClassDefinition superClassDef;
        @Mock
        ClassType superClassType;
        @Mock
        ListDeclMethod listDeclMethod;
        EnvironmentExp envExpSuper;
        ExpDefinition previous;

        DecacCompiler compiler;

        @BeforeEach
        public void setup() throws ContextualError {
                CompilerOptions co = new CompilerOptions();
                MockitoAnnotations.openMocks(this);
                compiler = new DecacCompiler(co, null);
                try {
                        compiler.environmentType.declare(new SymbolTable().create("x"), defClass);
                } catch (DoubleDefException e) {

                }
                when(superClassType.getName()).thenReturn(VOID.getName());
                // defClass = new ClassDefinition(superClassType, null, superClassDef);
                when(type.verifyType(compiler)).thenReturn(VOID);
                when(type.getType()).thenReturn(VOID);
                when(type2.verifyType(compiler)).thenReturn(BOOLEAN);
                when(type2.getType()).thenReturn(BOOLEAN);
                when(varName.getName()).thenReturn(new SymbolTable().create("x"));
                superClass.setDefinition(defClass);
                when(defClass.getSuperClass()).thenReturn(superClassDef);
                listDeclField = new ListDeclField();
                declField = new DeclField(visibility, type, varName, init);
                listDeclField.add(declField);
                declClass = new DeclClass(className, superClass, listDeclField, listDeclMethod);
                listDeclClass = new ListDeclClass();
                listDeclClass.add(declClass);
                when(className.getName()).thenReturn(new SymbolTable().create("x"));
                when(defClass.getNumberOfFields()).thenReturn(3);
                when(defClass.getNumberOfMethods()).thenReturn(3);
                when(defClass.getMembers()).thenReturn(localEnv);
                when(superClassDef.getMembers()).thenReturn(localEnv);
        }

        @Test
        public void testVerifyDeclField() throws ContextualError {
                ContextualError thrown = assertThrows(
                                ContextualError.class, () -> {
                                        listDeclClass.verifyListClassMembers(compiler);
                                });
                Assertions.assertEquals(
                                "Incompatible Types: Field " + this.varName.getName() + " is of type void.",
                                thrown.getMessage());
        }
}
