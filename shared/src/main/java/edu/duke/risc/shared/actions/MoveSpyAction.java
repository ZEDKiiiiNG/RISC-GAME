package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

/**
 * Action that moves spies from source to the destination territory, which should be adjacent to each other
 *
 * @author eason
 * @date 2021/4/14 15:20
 */
public class MoveSpyAction implements Action {

    /**
     * Player id of the action conductor
     */
    private final Integer playerId;

    /**
     * The action type
     */
    private final ActionType actionType;
    /**
     * The source territory id
     */
    private final Integer sourceId;

    /**
     * The destination territory id
     */
    private final Integer destinationId;

    /**
     * amount of units to upgrade into spy
     */
    private final Integer amount;

    /**
     * Constructor
     *
     * @param playerId      playerId
     * @param actionType    actionType
     * @param destinationId destination territory id
     * @param amount        amount of spies
     * @param sourceId      source territory id
     */
    public MoveSpyAction(Integer playerId, ActionType actionType, Integer sourceId, Integer destinationId, Integer amount) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.amount = amount;
    }


    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(playerId);
        Territory sourceTerritory = board.findTerritory(sourceId);

        //source and destination can only be neighbors
        if (!sourceTerritory.isAdjacentTo(destinationId)) {
            return "Source and destination should be adjacent to each other";
        }

        //player should have enough spies in that territory
        int numSpies = sourceTerritory.getNumberOfSpies(playerId);
        if (numSpies < amount) {
            return "Player " + playerId + " does not has enough spies (" + numSpies + " < " + amount
                    + ") in territory " + sourceTerritory;
        }

        return null;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        return moveActionApply(board, false);
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        return moveActionApply(board, true);
    }

    /**
     * Apply move actions
     *
     * @param board      board
     * @param isSimulate isSimulate
     * @return error if exceptions happens, action log if succeed
     * @throws InvalidActionException InvalidActionException
     */
    private String moveActionApply(GameBoard board, boolean isSimulate)
            throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Territory destTerritory = board.findTerritory(destinationId);
        Territory srcTerritory = board.findTerritory(sourceId);
        Player player = board.getPlayers().get(playerId);
        StringBuilder builder = new StringBuilder();

        //reduce spies in the source territory
        srcTerritory.updateSpiesMap(playerId, -amount);
        if (!isSimulate) {
            destTerritory.updateSpiesMap(playerId, amount);
        }

        //update spies map in the player
        player.updateSpiesMap(sourceId, -amount);
        player.updateSpiesMap(destinationId, amount);

        //logging
        builder.append("SUCCESS: player ").append(playerId).append(" moves ")
                .append(amount).append(" of spies from ")
                .append(srcTerritory).append(" to ").append(destTerritory).append(System.lineSeparator());

        return builder.toString();
    }

}
