package edu.duke.risc.shared.exceptions;

/**
 * When the receiver is unmatched
 *
 * @author eason
 * @date 2021/3/10 16:39
 */
public class UnmatchedReceiverException extends Exception {

    private String message;

    public UnmatchedReceiverException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
