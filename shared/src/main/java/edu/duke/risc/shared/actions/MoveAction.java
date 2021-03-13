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

/**
 *
 */
public class MoveAction extends AbstractAction {

    /**
     * Source territory id
     */
    private Integer sourceTerritoryId;

    private UnitType unitType;

    private Integer number;

    public MoveAction(Integer sourceTerritoryId, Integer destinationId, UnitType unitType, Integer number, Integer player) {
        super(player, ActionType.MOVE, destinationId, unitType, number);
        this.sourceTerritoryId = sourceTerritoryId;
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
        Territory territory = board.getTerritories().get(sourceTerritoryId);
        if (!territory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type.";
        }
        if (territory.getUnitsMap().get(unitType) < number) {
            return "The source territory does not contain enough unit type.";
        }
        if (!player.getOwnedTerritories().contains(sourceTerritoryId) || !player.getOwnedTerritories().contains(destinationId)) {
            return "The player does not contain source territory or destination territory.";
        }
        if (!hasPath(board.getTerritories().get(sourceTerritoryId), board.getTerritories().get(destinationId))) {
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
        Territory source_territory = board.getTerritories().get(sourceTerritoryId);
        Territory des_territory = board.getTerritories().get(destinationId);
        source_territory.updateUnitsMap(unitType, number);
        des_territory.updateUnitsMap(unitType, number);


    }
}
