package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class MoveAction extends AbstractAction {
    private Integer source_territoryId;
    private Integer des_territoryId;

    private UnitType unitType;

    private Integer number;

    public MoveAction(Integer source_territoryId, Integer des_territoryId, UnitType unitType, Integer number, Integer player) {
        super(player, ActionType.MOVE);
        this.source_territoryId = source_territoryId;
        this.des_territoryId = des_territoryId;
        this.unitType = unitType;
        this.number = number;
    }

    public Set<Territory> getPlayerTerritory(GameBoard board) {
        Set<Territory> ans = new HashSet<>();
        Player player = board.getPlayers().get(super.playerId);
        Set<Integer> terr_ids = player.getOwnedTerritories();
        for (Integer t : terr_ids) {
            ans.add(board.getTerritories().get(t));
        }
        return ans;
    }

    public Set<Territory> getAccessedTerritory(Territory t) {
        Set<Territory> ans = new HashSet<>();
        Queue<Territory> temp = new ArrayDeque<Territory>();
        for (Territory t1 : t.getAdjacentTerritories()) {
            ans.add(t1);
            temp.add(t1);
        }
        while (!temp.isEmpty()) {
            Territory t2 = temp.remove();
            for (Territory t3 : t2.getAdjacentTerritories()) {
                if (!ans.contains(t3)) {
                    ans.add(t3);
                    temp.add(t3);
                }
            }
        }
        return ans;
    }

    public boolean hasPath(Territory source, Territory des) {
        Set<Territory> accessTerritory = getAccessedTerritory(source);
        if (accessTerritory.contains(des)) {
            return true;
        }
        return false;
    }

    @Override
    public String isValid(GameBoard board) {
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);
        if (!player.getInitUnitsMap().containsKey(unitType)) {
            return "Does not contain unit type.";
        }
        Territory territory = board.getTerritories().get(source_territoryId);
        if (!territory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type.";
        }
        if (territory.getUnitsMap().get(unitType) < number) {
            return "The source territory does not contain enough unit type.";
        }
        if (!player.getOwnedTerritories().contains(source_territoryId) || !player.getOwnedTerritories().contains(des_territoryId)) {
            return "The player does not contain source territory or destination territory.";
        }
        if (!hasPath(board.getTerritories().get(source_territoryId), board.getTerritories().get(des_territoryId))) {
            return "The source territory has no path to destination territory.";
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
        Territory source_territory = board.getTerritories().get(source_territoryId);
        Territory des_territory = board.getTerritories().get(des_territoryId);
        source_territory.updateUnitsMap(unitType, number);
        des_territory.updateUnitsMap(unitType, number);


    }
}
