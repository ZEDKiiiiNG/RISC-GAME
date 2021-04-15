package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author eason
 * @date 2021/4/15 19:37
 */
public class ActionTest {

    @Test
    public void applyTest() throws InvalidActionException {
        UpgradeTechAction upgradeTechAction = new UpgradeTechAction(1, ActionType.UPGRADE_TECH);
        Assertions.assertNull(upgradeTechAction.applyBefore(null));
        Assertions.assertNull(upgradeTechAction.applyAfter(null));
    }

}
