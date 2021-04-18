package edu.duke.risc.shared.commons;

import edu.duke.risc.shared.exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Biomedical missile which kills all units under specific level in a single territory
 *
 * @author eason
 * @date 2021/4/12 15:38
 */
public enum MissileType implements Serializable {

    /**
     * MISSILE_LV1, kills SOLDIER(I)
     */
    MISSILE_LV1(1, "MISSILE LV1", 1),

    /**
     * MISSILE_LV2, kills SOLDIER(I), Infantry(II)
     */
    MISSILE_LV2(2, "MISSILE LV2", 2),

    /**
     * MISSILE_LV3, kills SOLDIER(I), Infantry(II),  Cavalry(III)
     */
    MISSILE_LV3(3, "MISSILE LV3", 3),

    /**
     * MISSILE_LV4, kills SOLDIER(I), Infantry(II),  Cavalry(III), Knight(IV)
     */
    MISSILE_LV4(4, "MISSILE LV4", 4),

    /**
     * MISSILE_LV5, kills SOLDIER(I), Infantry(II),  Cavalry(III), Knight(IV), Rook(V)
     */
    MISSILE_LV5(5, "MISSILE LV5", 5),

    /**
     * MISSILE_LV6, kills SOLDIER(I), Infantry(II),  Cavalry(III), Knight(IV), Rook(V), Queen(VI)
     */
    MISSILE_LV6(6, "MISSILE LV6", 6),

    /**
     * For testing of exception thrown
     */
    MISSILE_TEST(7, "MISSILE_TEST", 7);

    /**
     * Technology level required to get this missile
     */
    private int requiredTech;

    /**
     * Missile name
     */
    private String name;

    /**
     * Missile level - kills all unit under or equals to the missile level
     */
    private int level;

    MissileType(int requiredTech, String name, int level) {
        this.requiredTech = requiredTech;
        this.name = name;
        this.level = level;
    }

    /**
     * Get all the units that specific missile kills
     *
     * @param missileType specific missile
     * @return all the units that specific missile kills
     */
    public static Set<UnitType> getMissileKilledUnits(MissileType missileType) throws InvalidInputException {
        Set<UnitType> result = new HashSet<>();
        switch (missileType) {
            case MISSILE_LV1:
                result.add(UnitType.SOLDIER);
                return result;
            case MISSILE_LV2:
                result.add(UnitType.SOLDIER);
                result.add(UnitType.INFANTRY);
                return result;
            case MISSILE_LV3:
                result.add(UnitType.SOLDIER);
                result.add(UnitType.INFANTRY);
                result.add(UnitType.CAVALRY);
                return result;
            case MISSILE_LV4:
                result.add(UnitType.SOLDIER);
                result.add(UnitType.INFANTRY);
                result.add(UnitType.CAVALRY);
                result.add(UnitType.KNIGHT);
                return result;
            case MISSILE_LV5:
                result.add(UnitType.SOLDIER);
                result.add(UnitType.INFANTRY);
                result.add(UnitType.CAVALRY);
                result.add(UnitType.KNIGHT);
                result.add(UnitType.ROOK);
                return result;
            case MISSILE_LV6:
                result.add(UnitType.SOLDIER);
                result.add(UnitType.INFANTRY);
                result.add(UnitType.CAVALRY);
                result.add(UnitType.KNIGHT);
                result.add(UnitType.ROOK);
                result.add(UnitType.QUEEN);
                return result;
            default:
                throw new InvalidInputException("Invalid tech level input " + missileType);
        }
    }

    /**
     * Get the missile with technology level
     *
     * @return missile
     */
    public static MissileType getMissileTypeWithTechLevel(int techLevel) throws InvalidInputException {
        switch (techLevel) {
            case 1:
                return MISSILE_LV1;
            case 2:
                return MISSILE_LV2;
            case 3:
                return MISSILE_LV3;
            case 4:
                return MISSILE_LV4;
            case 5:
                return MISSILE_LV5;
            case 6:
                return MISSILE_LV6;
            default:
                throw new InvalidInputException("Invalid tech level input " + techLevel);
        }
    }

    @Override
    public String toString() {
        return name;
    }

}
