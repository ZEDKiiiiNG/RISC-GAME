package edu.duke.risc.shared.util;

import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.exceptions.InvalidInputException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eason
 * @date 2021/3/26 10:18
 */
public class TechHelper {

    public static int getTopTechLevel() {
        return 6;
    }

    public static int getNextTechLevel(int curLevel) {
        return curLevel + 1;
    }

    public static Map<ResourceType, Integer> getRequiredForTechUpgrade(int currentTech)
            throws InvalidInputException {
        Map<ResourceType, Integer> result = new HashMap<>(10);
        switch (currentTech) {
            case 1:
                result.put(ResourceType.TECH, 50);
                break;
            case 2:
                result.put(ResourceType.TECH, 75);
                break;
            case 3:
                result.put(ResourceType.TECH, 125);
                break;
            case 4:
                result.put(ResourceType.TECH, 200);
                break;
            case 5:
                result.put(ResourceType.TECH, 300);
                break;
            case 6:
                throw new InvalidInputException("Cannot upgrade tech " + currentTech + ", already at top level");
            default:
                throw new InvalidInputException("Invalid tech level input " + currentTech);
        }
        return result;
    }

}
