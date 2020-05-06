package de.craftlancer.core.command.newcmd;

public class ParserResult<T> {
    private boolean success;
    private String error;
    private T result;

    public ParserResult(T result) {
        this(result, true, null);
    }
    
    public ParserResult(T result, boolean success, String error) {
        this.error = error;
        this.success = success;
        this.result = result;
    }
    
    public String getError() {
        return error;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public T getResult() {
        return result;
    }
}