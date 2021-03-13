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
public class AttackAction extends AbstractAction {

    /**
     * Source territory id
     */
    private Integer sourceTerritoryId;

    private UnitType unitType;

    private Integer number;
    private Integer attackedPlayer;

    public AttackAction(Integer sourceTerritoryId, Integer destinationId, UnitType unitType,
                        Integer number, Integer player, Integer attackedPlayer) {
        super(player, ActionType.ATTACK, destinationId, unitType, number);
        //todo search attacked player on the board
        this.sourceTerritoryId = sourceTerritoryId;
        this.unitType = unitType;
        this.number = number;
        this.attackedPlayer = attackedPlayer;
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        if (!board.getPlayers().containsKey(attackedPlayer)) {
            return "Does not contain user: " + attackedPlayer;
        }
        Player player = board.getPlayers().get(super.playerId);
        Player attackedPlayer = board.getPlayers().get(this.attackedPlayer);
        if (!player.getInitUnitsMap().containsKey(unitType)) {
            return playerId + "Does not contain unit type.";
        }
        if (!attackedPlayer.getInitUnitsMap().containsKey(unitType)) {
            return attackedPlayer + "Does not contain unit type.";
        }
        Territory territory = board.getTerritories().get(sourceTerritoryId);
        if (!territory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type.";
        }
        if (territory.getUnitsMap().get(unitType) <= number) {
            return "The source territory does not contain enough unit type.";
        }
        if (!player.getOwnedTerritories().contains(sourceTerritoryId)) {
            return "The player does not contain source territory.";
        }
        if (!attackedPlayer.getOwnedTerritories().contains(destinationId)) {
            return "The attacked player does not contain destination territory.";
        }
        Territory desTerritory = board.getTerritories().get(destinationId);
        if (!territory.getAdjacentTerritories().contains(desTerritory)) {
            return "The source territory is not adjacent to destination territory.";
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
        Player attackedPlayer = board.getPlayers().get(this.attackedPlayer);
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        sourceTerritory.updateUnitsMap(unitType, -number);
        Territory desTerritory = board.getTerritories().get(destinationId);
        Integer attacker = number;
        while (desTerritory.getUnitsMap().get(unitType) != 0 && attacker != 0) {
            Integer random = randomWin();
            if (random == 0) {
                attacker--;
            } else {
                desTerritory.updateUnitsMap(unitType, -1);
                attackedPlayer.updateTotalUnitMap(unitType, -1);
            }
        }
        if (attacker == 0) {
            player.updateTotalUnitMap(unitType, -number);
        } else {
            desTerritory.getUnitsMap().put(unitType, attacker);
            player.updateTotalUnitMap(unitType, attacker - number);
        }
        return builder.toString();
    }

    @Override
    public void applyBefore(GameBoard board) throws InvalidActionException {

    }

    @Override
    public void applyAfter(GameBoard board) throws InvalidActionException {

    }

    public Integer randomWin() {
        if (Math.random() > 0.5) {
            return 0;
        } else {
            return 1;
        }
    }

}
