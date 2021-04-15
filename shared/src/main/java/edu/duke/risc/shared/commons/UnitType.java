package edu.duke.risc.shared.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    SOLDIER("(S)oldiers(I)", 0, 3),

    /**
     * INFANTRY with 1 bonus, requires 8 resources to upgrade into CAVALRY
     */
    INFANTRY("(I)fantry(II)", 1, 8),

    /**
     * INFANTRY with 3 bonus, requires 19 resources to upgrade into KNIGHT
     */
    CAVALRY("(C)avalry(III)", 3, 19),

    /**
     * INFANTRY with 5 bonus, requires 25 resources to upgrade into ROOK
     */
    KNIGHT("(K)night(IV)", 5, 25),

    /**
     * INFANTRY with 8 bonus, requires 35 resources to upgrade into QUEEN
     */
    ROOK("(R)ook(V)", 8, 35),

    /**
     * INFANTRY with 11 bonus, requires 50 resources to upgrade into MASTER
     */
    QUEEN("(Q)ueen(VI)", 11, 50),

    /**
     * INFANTRY with 15 bonus, the highest level of units
     */
    MASTER("(M)aster(VII)", 15, Integer.MAX_VALUE),

    /**
     * Only for testing
     */
    TEST("(T)EST(VIII)", 100, Integer.MAX_VALUE);

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

    /**
     * Get resources required for upgrading
     *
     * @return resources required for upgrading
     */
    public int getUpgradeResources(int number) {
        return this.upgradePerUnit * number;
    }

    /**
     * Get top level, which cannot be upgraded
     *
     * @return top level
     */
    public static UnitType getTopLevel() {
        return MASTER;
    }

    /**
     * Get the next level of unit type
     *
     * @param currentUnit current level of unit type
     * @return next level of unit type
     */
    public static UnitType getNextLevelOfUnit(UnitType currentUnit) {
        switch (currentUnit) {
            case SOLDIER:
                return INFANTRY;
            case INFANTRY:
                return CAVALRY;
            case CAVALRY:
                return KNIGHT;
            case KNIGHT:
                return ROOK;
            case ROOK:
                return QUEEN;
            case QUEEN:
            case MASTER:
                return MASTER;
            default:
                return null;
        }
    }

    /**
     * getTechRequiredToUpgrade
     *
     * @return getTechRequiredToUpgrade
     */
    public static int getTechRequiredToUpgrade(UnitType currentUnit) {
        switch (currentUnit) {
            case SOLDIER:
                return 1;
            case INFANTRY:
                return 2;
            case CAVALRY:
                return 3;
            case KNIGHT:
                return 4;
            case ROOK:
                return 5;
            case QUEEN:
                return 6;
            case MASTER:
            default:
                return Integer.MAX_VALUE;
        }
    }

    /**
     * getUnitTypeMapper
     *
     * @return UnitTypeMapper
     */
    public static Map<String, UnitType> getUnitTypeMapper() {
        Map<String, UnitType> unitTypeMapper = new HashMap<>();
        unitTypeMapper.put("s", UnitType.SOLDIER);
        unitTypeMapper.put("S", UnitType.SOLDIER);

        unitTypeMapper.put("i", UnitType.INFANTRY);
        unitTypeMapper.put("I", UnitType.INFANTRY);

        unitTypeMapper.put("c", UnitType.CAVALRY);
        unitTypeMapper.put("C", UnitType.CAVALRY);

        unitTypeMapper.put("k", UnitType.KNIGHT);
        unitTypeMapper.put("K", UnitType.KNIGHT);

        unitTypeMapper.put("r", UnitType.ROOK);
        unitTypeMapper.put("R", UnitType.ROOK);

        unitTypeMapper.put("q", UnitType.QUEEN);
        unitTypeMapper.put("Q", UnitType.QUEEN);

        unitTypeMapper.put("m", UnitType.MASTER);
        unitTypeMapper.put("M", UnitType.MASTER);
        return unitTypeMapper;
    }

    /**
     * getHighestLevelUnitType
     *
     * @param map map
     * @return getHighestLevelUnitType
     */
    public static UnitType getHighestLevelUnitType(Map<UnitType, Integer> map) {
        if (map.containsKey(MASTER)) {
            return MASTER;
        } else if (map.containsKey(QUEEN)) {
            return QUEEN;
        } else if (map.containsKey(ROOK)) {
            return ROOK;
        } else if (map.containsKey(KNIGHT)) {
            return KNIGHT;
        } else if (map.containsKey(CAVALRY)) {
            return CAVALRY;
        } else if (map.containsKey(INFANTRY)) {
            return INFANTRY;
        } else if (map.containsKey(SOLDIER)) {
            return SOLDIER;
        } else {
            return null;
        }
    }

    /**
     * getLowestLevelUnitType
     *
     * @param map map
     * @return getLowestLevelUnitType
     */
    public static UnitType getLowestLevelUnitType(Map<UnitType, Integer> map) {
        if (map.containsKey(SOLDIER)) {
            return SOLDIER;
        } else if (map.containsKey(INFANTRY)) {
            return INFANTRY;
        } else if (map.containsKey(CAVALRY)) {
            return CAVALRY;
        } else if (map.containsKey(KNIGHT)) {
            return KNIGHT;
        } else if (map.containsKey(ROOK)) {
            return ROOK;
        } else if (map.containsKey(QUEEN)) {
            return QUEEN;
        } else if (map.containsKey(MASTER)) {
            return MASTER;
        } else {
            return null;
        }
    }

    public int getBonus() {
        return bonus;
    }
}
