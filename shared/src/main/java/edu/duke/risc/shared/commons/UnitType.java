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
     * soldier with 0 bonus, requires 3 resources to upgrade into INFANTRY
     */
    SOLDIER("(S)oldiers(0)", 0, 3),

    /**
     * INFANTRY with 1 bonus, requires 8 resources to upgrade into CAVALRY
     */
    INFANTRY("(I)fantry(1)", 1, 8),

    /**
     * INFANTRY with 3 bonus, requires 19 resources to upgrade into KNIGHT
     */
    CAVALRY("(C)avalry(2)", 3, 19),

    /**
     * INFANTRY with 5 bonus, requires 25 resources to upgrade into ROOK
     */
    KNIGHT("(K)night(3)", 5, 25),

    /**
     * INFANTRY with 8 bonus, requires 35 resources to upgrade into QUEEN
     */
    ROOK("(R)ook(4)", 8, 35),

    /**
     * INFANTRY with 11 bonus, requires 50 resources to upgrade into MASTER
     */
    QUEEN("(Q)ueen(5)", 11, 50),

    /**
     * INFANTRY with 15 bonus, the highest level of units
     */
    MASTER("(M)aster(6)", 15, Integer.MAX_VALUE);

    /**
     * String representation of the unit type
     */
    private final String name;

    /**
     * Combat bonus
     */
    private final int bonus;

    /**
     * Number of resources required for each unit to upgrade into the next level
     */
    private final int upgradePerUnit;

    UnitType(String name, int bonus, int upgradePerUnit) {
        this.name = name;
        this.bonus = bonus;
        this.upgradePerUnit = upgradePerUnit;
    }

    @Override
    public String toString() {
        return name;
    }

}
