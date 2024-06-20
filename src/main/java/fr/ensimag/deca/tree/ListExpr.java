package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl03
 * @date 14/04/2024
 */
public class ListExpr extends TreeList<AbstractExpr> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractExpr i : getList()) {
            i.decompileInst(s);
        }
    }

    @Override
    public String toString() {
        String s = "[";
        for (AbstractExpr expr : this.getList()) {
            s += expr.getType() + ", ";
        }
        return s.substring(0, s.length() - 2) + "]";
    }

}
