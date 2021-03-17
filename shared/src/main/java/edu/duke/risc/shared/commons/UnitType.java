package edu.duke.risc.shared.commons;

import java.io.Serializable;

/**
 * Unit type: SOLDIER, TANK
 *
 * @author eason
 * @date 2021/3/9 20:12
 */
public enum UnitType implements Serializable {

    //when adding new unit type, please do not forget to update the unitTypeMapper in the game board class

    /**
     * soldier
     */
    SOLDIER("(S)oldiers");

    /**
     * String representation of the unit type
     */
    private final String name;

    UnitType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
