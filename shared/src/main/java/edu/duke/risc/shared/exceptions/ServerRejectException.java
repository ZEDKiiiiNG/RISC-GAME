package edu.duke.risc.shared.exceptions;

/**
 * Server rejects this action, invalid action
 *
 * @author eason
 * @date 2021/3/11 15:44
 */
public class ServerRejectException extends Exception {

    private String message;

    public ServerRejectException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
