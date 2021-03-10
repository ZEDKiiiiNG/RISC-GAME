package edu.duke.risc.shared.exceptions;

/**
 * @author eason
 * @date 2021/3/10 16:39
 */
public class UnmatchedReceiverException extends Exception {

    private String message;

    public UnmatchedReceiverException(String message) {
        this.message = message;
    }

}
