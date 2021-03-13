package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

public class AttackAction extends AbstractAction {
    private Integer source_territoryId;
    private Integer des_territoryId;

    private UnitType unitType;

    private Integer number;
    private Integer attackedPlayer;

    public AttackAction(Integer source_territoryId, Integer des_territoryId, UnitType unitType, Integer number, Integer player, Integer attackedPlayer) {
        super(player, ActionType.ATTACK);
        this.source_territoryId = source_territoryId;
        this.des_territoryId = des_territoryId;
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
        Player att_player = board.getPlayers().get(attackedPlayer);
        if (!player.getInitUnitsMap().containsKey(unitType)) {
            return playerId + "Does not contain unit type.";
        }
        if (!att_player.getInitUnitsMap().containsKey(unitType)) {
            return att_player + "Does not contain unit type.";
        }
        Territory territory = board.getTerritories().get(source_territoryId);
        if (!territory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type.";
        }
        if (territory.getUnitsMap().get(unitType) <= number) {
            return "The source territory does not contain enough unit type.";
        }
        if (!player.getOwnedTerritories().contains(source_territoryId)) {
            return "The player does not contain source territory.";
        }
        if (!att_player.getOwnedTerritories().contains(des_territoryId)) {
            return "The attacked player does not contain destination territory.";
        }
        Territory des_territory = board.getTerritories().get(des_territoryId);
        if (!territory.getAdjacentTerritories().contains(des_territory)) {
            return "The source territory is not adjacent to destination territory.";
        }
        return null;
    }

    @Override
    public void apply(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        Player player = board.getPlayers().get(super.playerId);
        Player att_player = board.getPlayers().get(attackedPlayer);
        Territory source_territory = board.getTerritories().get(source_territoryId);
        source_territory.updateUnitsMap(unitType, -number);
        Territory des_territory = board.getTerritories().get(des_territoryId);
        Integer attacker = number;
        while (des_territory.getUnitsMap().get(unitType) != 0 && attacker != 0) {
            Integer random = randomWin();
            if (random == 0) {
                attacker--;
            } else {
                des_territory.updateUnitsMap(unitType, -1);
                att_player.updateTotalUnitsMap(unitType, -1);
            }
        }
        if (attacker == 0) {
            player.updateTotalUnitsMap(unitType, -number);
        } else {
            des_territory.getUnitsMap().put(unitType, attacker);
            player.updateTotalUnitsMap(unitType, attacker - number);
        }
    }

    public Integer randomWin() {
        if (Math.random() > 0.5) {
            return 0;
        } else {
            return 1;
        }
    }

}
