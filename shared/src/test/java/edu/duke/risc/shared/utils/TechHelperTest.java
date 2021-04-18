package edu.duke.risc.shared.utils;

import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.exceptions.InvalidInputException;
import edu.duke.risc.shared.util.TechHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author eason
 * @date 2021/4/15 13:34
 */
public class TechHelperTest {

    @Test
    public void testGetTopTechLevel() {
        Assertions.assertEquals(6, TechHelper.getTopTechLevel());
    }

    @Test
    public void testGetNextTechLevel() {
        Assertions.assertEquals(2, TechHelper.getNextTechLevel(1));
    }


    @Test
    public void testGetRequiredForTechUpgrade() throws InvalidInputException {
        Map<ResourceType, Integer> resource1 = TechHelper.getRequiredForTechUpgrade(1);
        Map<ResourceType, Integer> resource2 = TechHelper.getRequiredForTechUpgrade(2);
        Map<ResourceType, Integer> resource3 = TechHelper.getRequiredForTechUpgrade(3);
        Map<ResourceType, Integer> resource4 = TechHelper.getRequiredForTechUpgrade(4);
        Map<ResourceType, Integer> resource5 = TechHelper.getRequiredForTechUpgrade(5);

        Assertions.assertThrows(InvalidInputException.class, () -> TechHelper.getRequiredForTechUpgrade(6));
        Assertions.assertThrows(InvalidInputException.class, () -> TechHelper.getRequiredForTechUpgrade(10));

        Assertions.assertEquals(50, resource1.get(ResourceType.TECH));
        Assertions.assertEquals(75, resource2.get(ResourceType.TECH));
        Assertions.assertEquals(125, resource3.get(ResourceType.TECH));
        Assertions.assertEquals(200, resource4.get(ResourceType.TECH));
        Assertions.assertEquals(300, resource5.get(ResourceType.TECH));

    }


}
