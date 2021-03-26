package edu.duke.risc.shared.commons;

/**
 * @author eason
 * @date 2021/3/25 20:23
 */
public enum ResourceType {

    /**
     * technology resource
     */
    TECH("tech"),

    /**
     * food resource
     */
    FOOD("food");

    private final String name;

    ResourceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
