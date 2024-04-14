package fr.ensimag.deca.tree;

/**
 * Function that takes a tree as argument.
 * 
 * @see fr.ensimag.deca.tree.Tree#iter(TreeFunction)
 * 
 * @author gl03
 * @date 14/04/2024
 */
public interface TreeFunction {
    void apply(Tree t);
}
