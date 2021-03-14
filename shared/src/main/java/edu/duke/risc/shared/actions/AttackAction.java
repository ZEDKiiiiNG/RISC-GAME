package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

/**
 *
 */
public class AttackAction extends AbstractSourceAction implements TwoStepsAction {

    /**
     * -1 if the target place is not occupied
     */
    private Integer attackedPlayerId;

    public AttackAction(Integer sourceTerritoryId, Integer destinationId, UnitType unitType,
                        Integer number, Integer player) {
        super(player, ActionType.ATTACK, destinationId, unitType, number, sourceTerritoryId);
    }

    /**
     * Find the player who owns the destination territory
     *
     * @return player or -1 if not owned
     */
    private int findPlayerOwnsTerritory(GameBoard board) {
        for (Player player : board.getPlayers().values()) {
            if (player.ownsTerritory(destinationId)) {
                return player.getId();
            }
        }
        return -1;
    }

    @Override
    public String isValid(GameBoard board) {
        if (number <= 0) {
            return "Invalid or unnecessary number " + number;
        }
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);
        if (!player.ownsTerritory(sourceTerritoryId)) {
            return "You do not own territory " + sourceTerritoryId;
        }
        if (player.ownsTerritory(destinationId)) {
            return "You cannot attack owned " + sourceTerritoryId + ", please try move";
        }
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        Territory destTerritory = board.getTerritories().get(destinationId);
        if (!sourceTerritory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type.";
        }
        if (sourceTerritory.getUnitsMap().get(unitType) < number) {
            return "The source territory does not contain enough unit type.";
        }
        if (!board.isReachable(sourceTerritoryId, destinationId, playerId)) {
            return "Not reachable from source " + sourceTerritory + " to destination" + destTerritory;
        }
        return null;
    }

    @Override
    public String apply(GameBoard board) throws InvalidActionException {
        return "";
    }

    @Override
    public String simulateApply(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        Territory destTerritory = board.getTerritories().get(destinationId);
        sourceTerritory.updateUnitsMap(unitType, -number);
        destTerritory.updateVirtualUnitsMap(unitType, number);
        return "";
    }

    @Override
    public String applyBefore(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        board.playerMoveFromTerritory(playerId, sourceTerritoryId, unitType, number);
        this.attackedPlayerId = findPlayerOwnsTerritory(board);
        return "";
    }

    @Override
    public String applyAfter(GameBoard board) throws InvalidActionException {
        StringBuilder builder = new StringBuilder();

        Player player = board.getPlayers().get(super.playerId);
        Player attackedPlayer = board.getPlayers().get(this.attackedPlayerId);
        Territory desTerritory = board.getTerritories().get(destinationId);
        Integer attacker = number;

        int attackerLost = 0, defenderLost = 0;
        while (!desTerritory.isEmptyTerritory()
                && desTerritory.getUnitsMap().get(unitType) > 0 && attacker != 0) {
            Integer random = randomWin();
            if (random == 0) {
                //attacked(defender) win, attacker lost
                attacker -= 1;
                attackerLost += 1;
            } else {
                //attacker win, attacked(defender) lost
                desTerritory.updateUnitsMap(unitType, -1);
                attackedPlayer.updateTotalUnitMap(unitType, -1);
                defenderLost += 1;
            }
        }

        builder.append(this).append(" with results: attacker lost ").append(attackerLost)
                .append(" defender lost ").append(defenderLost).append(" : ");
        if (attacker == 0) {
            //attacker lost
            player.updateTotalUnitMap(unitType, -number);
            builder.append(" Attacker lost.");
        } else {
            //attacked win, take the place
            desTerritory.getUnitsMap().put(unitType, attacker);
            player.updateTotalUnitMap(unitType, attacker - number);

            player.getOwnedTerritories().add(destinationId);
            attackedPlayer.getOwnedTerritories().remove(destinationId);
            builder.append("defender lost territory ").append(board.findTerritory(destinationId).getTerritoryName());
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    @Override
    public String toString() {
        return "ATTACK {" +
                " Attacker: PLAYER " + playerId +
                " with " + number + " " + unitType +
                " Defender: PLAYER " + attackedPlayerId +
                " }";
    }

    /***
     * roll a dice
     * @return 0 for defender win, 1 for attacked win
     */
    public Integer randomWin() {
        if (Math.random() > 0.5) {
            return 0;
        } else {
            return 1;
        }
    }

}
