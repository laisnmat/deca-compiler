package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }

    public Type paramNumber(int n) {
        return args.get(n);
    }

    public int size() {
        return args.size();
    }

    public boolean sameSignature(Signature other) {
        if (this.args.size() != other.args.size()) {
            return false;
        }
        for (int i = 0; i < this.args.size(); i++) {
            if (!this.args.get(i).sameType(other.args.get(i))) {
                return false;
            }
        }
        return true;
    }

    public List<Type> getArgs() {
        return args;
    }

}
