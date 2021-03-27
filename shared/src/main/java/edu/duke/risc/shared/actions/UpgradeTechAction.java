package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

/**
 * @author eason
 * @date 2021/3/26 10:12
 */
public class UpgradeTechAction extends AbstractAction {

    public UpgradeTechAction(Integer playerId, ActionType actionType) {
        super(playerId, actionType, null, null);
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);

        //already in the highest tech level
        if (player.isAtTopLevel()) {
            return "Player is already at the top tech level";
        }

        //not enough technology resources
        String error;
        if ((error = player.hasEnoughResourcesForTechUpgrade()) != null) {
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
        Player player = board.getPlayers().get(super.playerId);
        builder.append("SUCCESS: ").append(player.upgradeTechLevel(true));
        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(super.playerId);
        player.upgradeTechLevel(false);
        return null;
    }

}
