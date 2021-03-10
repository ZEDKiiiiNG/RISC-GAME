package edu.duke.risc.shared.commons;

/**
 * Colors of the users
 *
 * @author yichen.hua
 * @date 2021/3/9 20:03
 */
public enum UserColor {
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
    RED("Red");

    private final String name;

    UserColor(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
