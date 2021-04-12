package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.MissileType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.exceptions.InvalidInputException;
import edu.duke.risc.shared.users.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author eason
 * @date 2021/4/12 16:19
 */
public class MissileAttackAction implements Action {

    /**
     * Player id of the action conductor
     */
    private final Integer playerId;

    /**
     * The action type
     */
    private final ActionType actionType;

    /**
     * The destination territory id
     */
    private final Integer destinationId;

    /**
     * Missile type
     */
    private final MissileType missileType;

    /**
     * Constructor
     *
     * @param playerId      playerId conductor of the action
     * @param actionType    actionType always be MISSILE_ATTACK
     * @param destinationId destination territory to bomb the missile
     * @param missileType   type of the missile
     */
    public MissileAttackAction(Integer playerId, ActionType actionType, Integer destinationId, MissileType missileType) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.destinationId = destinationId;
        this.missileType = missileType;
    }

    @Override
    public String isValid(GameBoard board) {
        //whether board contains the player
        if (!board.getPlayers().containsKey(playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(playerId);
        Territory destTerritory = board.findTerritory(destinationId);

        //player cannot attack his own place with missile
        if (player.ownsTerritory(destinationId)) {
            return "You cannot attack with missile on your own territory " + destTerritory;
        }

        //player should own sufficient amount of missiles
        if (!player.hasEnoughMissiles(missileType, 1)) {
            return "You do not have enough " + missileType;
        }

        return null;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        StringBuilder builder = new StringBuilder();

        Player player = board.getPlayers().get(playerId);
        player.useMissiles(missileType, 1);

        //get units that this missile is able to kill
        Set<UnitType> unitsShouldKill;
        try {
            unitsShouldKill = MissileType.getMissileKilledUnits(missileType);
        } catch (InvalidInputException e) {
            throw new InvalidActionException(e.getMessage());
        }

        //get the target player of the territory
        Territory targetTerritory = board.findTerritory(destinationId);
        Integer targetPlayerId = board.findPlayerOwnsTerritory(destinationId);
        Player targetPlayer = board.findPlayer(targetPlayerId);

        Map<UnitType, Integer> unitsKilled = new HashMap<>(10);
        //act the missile on the target territory
        for (UnitType unitToKill : unitsShouldKill) {
            if (targetTerritory.containsUnitType(unitToKill)) {
                int number = targetTerritory.getUnitsNumber(unitToKill);
                //reduce units number in both player and territory
                targetTerritory.updateUnitsMap(unitToKill, -number);
                targetPlayer.updateTotalUnitMap(unitToKill, -number);
                unitsKilled.put(unitToKill, number);
            }
        }

        builder.append("SUCCESS: ").append(printInfo(unitsKilled));
        return builder.toString();
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(playerId);
        //only reduce missiles on
        player.useMissiles(missileType, 1);
        return null;
    }

    /**
     * Print action information
     *
     * @return action information in string
     */
    public String printInfo(Map<UnitType, Integer> unitsKilled) {
        StringBuilder builder = new StringBuilder();
        builder.append("MISSILE ATTACK ACTION { ")
                .append(" conducted by player ").append(playerId)
                .append(", onto ").append(destinationId)
                .append(" with ").append(missileType)
                .append(" and killed: ");
        for (Map.Entry<UnitType, Integer> entry : unitsKilled.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            builder.append(number).append(" ").append(unitType);
        }
        return builder.toString();
    }

}
