package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 * 
 * Insertion (through method declare) is always done in the "current"
 * dictionary.
 * 
 * @author gl03
 * @date 14/04/2024
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    private final HashMap<Symbol, ExpDefinition> env;

    EnvironmentExp parentEnvironment;

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.env = new HashMap<>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        if (env.containsKey(key))
            return env.get(key);
        if (parentEnvironment == null)
            return null;
        if (parentEnvironment.get(key) != null)
            return parentEnvironment.get(key);
        return null;
    }

    /**
     * Calculate recursively the "lowest" class every method and attribute is
     * defined in
     * 
     * @return Map<Symbol, ExpDefinition> of all methods and attributes of the class
     *         and its superclasses
     */
    public Map<Symbol, ExpDefinition> getAllMembers() {
        HashMap<Symbol, ExpDefinition> members = new HashMap<>();
        if (this.parentEnvironment != null) {
            members.putAll(this.parentEnvironment.getAllMembers());
        }
        members.putAll(this.env);
        return members;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *             Name of the symbol to define
     * @param def
     *             Definition of the symbol
     * @throws DoubleDefException
     *                            if the symbol is already defined at the "current"
     *                            dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (env.containsKey(name))
            throw new DoubleDefException();
        env.put(name, def);
    }

    @Override
    public String toString() {
        String res = "Env = ";
        for (Symbol key : env.keySet()) {
            res += "(" + key + " -> " + env.get(key) + "), ";
        }
        return res;
    }
}
