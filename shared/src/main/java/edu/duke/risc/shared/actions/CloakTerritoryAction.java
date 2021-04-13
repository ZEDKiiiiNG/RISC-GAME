package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.Map;

/**
 * Cloak a specific territory action
 *
 * @author eason
 * @date 2021/4/13 15:34
 */
public class CloakTerritoryAction extends AbstractAction {

    /**
     * Tech resource required to conduct cloak territory action
     */
    private final Integer TECH_RESOURCE_REQUIRED = 20;

    /**
     * Constructor
     *
     * @param playerId      playerId
     * @param actionType    actionType
     * @param destinationId destinationId to conduct cloaking actions on
     * @param unitMap       unitMap, here set it to null
     */
    public CloakTerritoryAction(Integer playerId, ActionType actionType,
                                Integer destinationId, Map<UnitType, Integer> unitMap) {
        super(playerId, actionType, destinationId, unitMap);
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(playerId);
        String error;
        //has not yet researched cloaking
        if (!player.isCloakingResearched()) {
            return "Player has not yet research cloaking";
        }
        //cloak territory not owned by player
        if (!player.ownsTerritory(destinationId)) {
            return "The player does not own the destination territory " + board.findTerritory(destinationId);
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
        Territory territory = board.findTerritory(destinationId);
        //reduce tech resource and enable cloaking on that territory
        player.updateResourceMap(ResourceType.TECH, -TECH_RESOURCE_REQUIRED);
        territory.enableCloaking();
        builder.append("SUCCESS CLOAKING: by player ").append(player.getColor())
                .append(" on territory ").append(territory)
                .append(", uses ").append(TECH_RESOURCE_REQUIRED).append(" ").append(ResourceType.TECH);
        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return this.apply(board);
    }

}
