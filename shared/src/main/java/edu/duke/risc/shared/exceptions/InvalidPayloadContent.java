package edu.duke.risc.shared.exceptions;

/**
 * @author eason
 * @date 2021/3/10 17:01
 */
public class InvalidPayloadContent extends Exception{

    private String message;

    public InvalidPayloadContent(String message) {
        this.message = message;
    }

}
