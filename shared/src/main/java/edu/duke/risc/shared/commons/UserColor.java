package edu.duke.risc.shared.commons;

import java.io.Serializable;

/**
 * Colors of the users
 *
 * @author yichen.hua
 * @date 2021/3/9 20:03
 */
public enum UserColor implements Serializable {

    /**
     * Stands for blue user
     */
    BLUE("Blue"),

    /**
     * Stands for green user
     */
    GREEN("Green"),

    /**
     * Stands for red user
     */
    RED("Red"),

    /**
     * Stands for YELLOW user
     */
    YELLOW("Yellow"),


    /**
     * Stands for PURPLE user
     */
    PURPLE("Purple");

    /**
     * String representation of the colors
     */
    private final String name;

    UserColor(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
