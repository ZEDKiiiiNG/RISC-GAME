package edu.duke.risc.shared.exceptions;

/**
 * The input is invalid
 *
 * @author eason
 * @date 2021/3/13 16:02
 */
public class InvalidInputException extends Exception {

    private String message;

    public InvalidInputException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

}
