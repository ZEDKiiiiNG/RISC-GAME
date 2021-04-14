package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;
import edu.duke.risc.shared.util.MapHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Attack Action
 *
 * @author eason
 */
public class AttackAction extends AbstractSourceAction implements TwoStepsAction {

    /**
     * Constructor
     *
     * @param sourceTerritoryId sourceTerritoryId
     * @param destinationId     destinationId
     * @param unitMap           map of units
     * @param player            player
     */
    public AttackAction(Integer sourceTerritoryId, Integer destinationId,
                        Map<UnitType, Integer> unitMap, Integer player) {
        super(player, ActionType.ATTACK, destinationId, unitMap, sourceTerritoryId);
    }

    @Override
    public String isValid(GameBoard board) {
        //check whether input has valid number
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            if (entry.getValue() <= 0) {
                return "Invalid number " + entry.getValue() + " for unit type " + entry.getKey();
            }
        }
        if (!board.getPlayers().containsKey(super.playerId)) {
            return "Does not contain user: " + playerId;
        }
        Player player = board.getPlayers().get(super.playerId);
        if (!player.ownsTerritory(sourceTerritoryId)) {
            return "You do not own territory " + sourceTerritoryId;
        }
        if (player.ownsTerritory(destinationId)) {
            return "You cannot attack owned " + destinationId + ", please try move";
        }
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        Territory destTerritory = board.getTerritories().get(destinationId);

        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            if (!sourceTerritory.getUnitsMap().containsKey(unitType)) {
                return "The source territory does not contain the unit type.";
            }
            if (sourceTerritory.getUnitsMap().get(unitType) < number) {
                return "The source territory does not contain enough unit type.";
            }
        }

        int moveCostPerUnit;
        if ((moveCostPerUnit = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId)) == Integer.MAX_VALUE) {
            return "Not reachable from source " + sourceTerritory + " to destination" + destTerritory;
        }
        int totalCost = getResourceRequired(moveCostPerUnit, 1);

        //whether current player has enough resource to move units
        String error;
        if ((error = player.hasEnoughResources(ResourceType.FOOD, totalCost)) != null) {
            return error;
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

        //calculate and deduct costs and update attacker's resources map
        Player attackingPlayer = board.findPlayer(playerId);
        int moveCostPerUnit = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId);
        int totalCost = getResourceRequired(moveCostPerUnit, 1);
        attackingPlayer.updateResourceMap(ResourceType.FOOD, -totalCost);

        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        Territory destTerritory = board.getTerritories().get(destinationId);

        //update virtual units map in the front
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            sourceTerritory.updateUnitsMap(unitType, -number);
            destTerritory.updateVirtualUnitsMap(unitType, number);
        }
        return "";
    }

    @Override
    public String applyBefore(GameBoard board) throws InvalidActionException {
        String error;
        if ((error = isValid(board)) != null) {
            throw new InvalidActionException(error);
        }
        board.playerMoveFromTerritory(sourceTerritoryId, unitMap);
        return "";
    }

    @Override
    public String applyAfter(GameBoard board) throws InvalidActionException {
        StringBuilder builder = new StringBuilder();

        Player attackerPlayer = board.getPlayers().get(super.playerId);

        /* -1 if the target place is not occupied */
        Integer defenderPlayerId = board.findPlayerOwnsTerritory(destinationId);
        Player defenderPlayer = board.findPlayer(defenderPlayerId);
        Territory sourceTerritory = board.findTerritory(sourceTerritoryId);
        Territory desTerritory = board.findTerritory(destinationId);
        //calculate and deduct costs
        int moveCostPerUnit = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId);
        int totalCost = getResourceRequired(moveCostPerUnit, 1);
        attackerPlayer.updateResourceMap(ResourceType.FOOD, -totalCost);

        Map<UnitType, Integer> attackerMap = unitMap;
        Map<UnitType, Integer> defenderMap = desTerritory.getUnitsMap();

        builder.append("ATTACK { Attacker: ").append(attackerPlayer.getColor()).append(" PLAYER")
                .append(", costs ").append(totalCost).append(" ").append(ResourceType.FOOD);
        for (Map.Entry<UnitType, Integer> entry : attackerMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            builder.append(" with ").append(number).append(" ").append(unitType);
        }
        builder.append(System.lineSeparator()).append(", Defender: PLAYER ")
                .append(defenderPlayer.getColor())
                .append(" from place ").append(sourceTerritory.getBasicInfo()).append(" to place ")
                .append(desTerritory.getBasicInfo());
        for (Map.Entry<UnitType, Integer> entry : defenderMap.entrySet()) {
            UnitType unitType = entry.getKey();
            int number = entry.getValue();
            builder.append(" with ").append(number).append(" ").append(unitType);
        }
        builder.append(" }");

        if (!defenderPlayerId.equals(playerId)) {
            Map<UnitType, Integer> attackerLost = new HashMap<>(), defenderLost = new HashMap<>();
            //attack phase
            boolean isReversed = true;
            while (!defenderMap.isEmpty() && !attackerMap.isEmpty()) {
                UnitType attackerType, defenderType;
                if (isReversed) {
                    attackerType = UnitType.getHighestLevelUnitType(attackerMap);
                    defenderType = UnitType.getLowestLevelUnitType(defenderMap);
                } else {
                    attackerType = UnitType.getLowestLevelUnitType(attackerMap);
                    defenderType = UnitType.getHighestLevelUnitType(defenderMap);
                }
                System.out.println("Attacker: " + attackerType + " V.S Defender: " + defenderType);
                assert attackerType != null;
                assert defenderType != null;
                Integer random = randomWin(attackerType, defenderType);
                if (random == 0) {
                    //attacked(defender) win, attacker lost
                    MapHelper.updateMap(attackerMap, attackerType, -1);
                    attackerPlayer.updateTotalUnitMap(attackerType, -1);
                    MapHelper.updateMap(attackerLost, attackerType, 1);
                    System.out.println("Attacker lost 1 " + attackerType);
                } else {
                    //attacker win, attacked(defender) lost
                    MapHelper.updateMap(defenderLost, defenderType, 1);
                    desTerritory.updateUnitsMap(defenderType, -1);
                    defenderPlayer.updateTotalUnitMap(defenderType, -1);
                    System.out.println("Defender lost 1 " + defenderType);
                }
                isReversed = !isReversed;
            }

            builder.append(" with results: attacker lost ");
            for (Map.Entry<UnitType, Integer> entry : attackerLost.entrySet()) {
                builder.append(entry.getValue()).append(" ").append(entry.getKey());
            }
            builder.append(" defender lost: ");
            for (Map.Entry<UnitType, Integer> entry : defenderLost.entrySet()) {
                builder.append(entry.getValue()).append(" ").append(entry.getKey());
            }

            if (attackerMap.isEmpty()) {
                //attacker lost
                builder.append(" Attacker lost.");
            } else {
                attackerPlayer.getOwnedTerritories().add(destinationId);
                defenderPlayer.removeOwnedTerritory(destinationId);
                //attacker puts units into this territory when win
                for (Map.Entry<UnitType, Integer> entry : attackerMap.entrySet()) {
                    desTerritory.updateUnitsMap(entry.getKey(), entry.getValue());
                }
                builder.append("defender lost territory ").append(board.findTerritory(destinationId).getTerritoryName());
                // when attacker takes the player, remove all cloaking counts
                desTerritory.resetCloak();
            }
        } else {
            for (Map.Entry<UnitType, Integer> entry : attackerMap.entrySet()) {
                desTerritory.updateUnitsMap(entry.getKey(), entry.getValue());
            }
            builder.append("Player ").append(playerId).append(" has already occupied this place");
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    /**
     * Get resources required for moving
     *
     * @param moveCostPerUnit moveCostPerUnit
     * @return resources required for moving
     */
    private int getResourceRequired(int moveCostPerUnit, int attackCostPerUnit) {
        int totalCost = 0;
        for (Map.Entry<UnitType, Integer> entry : unitMap.entrySet()) {
            int number = entry.getValue();
            totalCost += (moveCostPerUnit + attackCostPerUnit) * number;
        }
        return totalCost;
    }

    /**
     * @return 0 for defender win, 1 for attacker win
     */
    public Integer randomWin(UnitType attackerType, UnitType defenderType) {
        Integer defender = rollADice(defenderType.getBonus());
        Integer attacker = rollADice(attackerType.getBonus());
        if (defender >= attacker) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * roll a 20-sided dice
     *
     * @return the answer of the dice
     */
    public Integer rollADice(int bonus) {
        return (int) (Math.random() * 20) + 1 + bonus;
    }


    @Override
    public String toString() {
        return "Attack by player " + playerId + " from territory " + sourceTerritoryId + " to " + destinationId;
    }

}
