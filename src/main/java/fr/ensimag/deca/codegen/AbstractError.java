package fr.ensimag.deca.codegen;

public abstract class AbstractError {

    protected String errorMsg;
    protected int id;

    public AbstractError(String errorMsg, int id) {
        this.errorMsg = errorMsg;
        this.id = id;
    }

    protected String getErrorMessage() {
        return this.errorMsg;
    }
}
