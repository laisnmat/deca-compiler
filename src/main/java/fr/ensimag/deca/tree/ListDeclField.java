package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl03
 * @date 11/06/2024
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
        }
    }

    protected void verifyListDeclField(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        int i = currentClass.getSuperClass().getNumberOfFields();
        for (AbstractDeclField declField : getList()) {
            i++;
            declField.verifyDeclField(compiler, currentClass, i);
        }
        currentClass.setNumberOfFields(i);
    }

    protected void verifyListDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        for (AbstractDeclField declField : getList()) {
            declField.verifyDeclFieldInit(compiler, currentClass);
        }
    }
}
