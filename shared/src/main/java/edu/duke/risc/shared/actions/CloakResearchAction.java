package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

/**
 * Research cloaking for a player
 *
 * @author eason
 * @date 2021/4/13 15:16
 */
public class CloakResearchAction implements Action {

    /**
     * tech level required to research cloaking
     */
    private final Integer TECH_REQUIRED = 3;

    /**
     * tech resource required to research cloaking
     */
    private final Integer TECH_RESOURCE_REQUIRED = 100;

    /**
     * ID of the player
     */
    private final Integer playerId;

    /**
     * Constructor
     *
     * @param playerId id of the player
     */
    public CloakResearchAction(Integer playerId) {
        this.playerId = playerId;
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(playerId);
        String error;
        //have already researched cloaking
        if (player.isCloakingResearched()) {
            return "You have already researched cloaking";
        }
        //do not have enough tech for researching
        if ((error = player.hasEnoughTechLevel(TECH_REQUIRED)) != null) {
            return error;
        }
        //not enough technology resources
        if ((error = player.hasEnoughResources(ResourceType.TECH, TECH_RESOURCE_REQUIRED)) != null) {
            return error;
        }
        return null;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        String error;
        StringBuilder builder = new StringBuilder();
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(playerId);
        player.updateResourceMap(ResourceType.TECH, -TECH_RESOURCE_REQUIRED);
        builder.append("SUCCESS: ").append(player.doResearchCloaking());
        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

}
