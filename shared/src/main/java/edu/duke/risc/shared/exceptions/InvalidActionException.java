package edu.duke.risc.shared.exceptions;

/**
 * When the action is invalid
 *
 * @author eason
 * @date 2021/3/11 14:29
 */
public class InvalidActionException extends Exception {

    private String message;

    public InvalidActionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
