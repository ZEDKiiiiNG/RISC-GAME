package edu.duke.risc.shared.commons;

import java.io.Serializable;

/**
 * Unit type: SOLDIER, TANK
 *
 * @author eason
 * @date 2021/3/9 20:12
 */
public enum UnitType implements Serializable {

    /**
     * soldier
     */
    SOLDIER("Soldiers");

    private final String name;

    UnitType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
