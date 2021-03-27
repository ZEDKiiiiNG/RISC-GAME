package edu.duke.risc.shared.actions;

import edu.duke.risc.shared.board.GameBoard;
import edu.duke.risc.shared.board.Territory;
import edu.duke.risc.shared.commons.ActionType;
import edu.duke.risc.shared.commons.ResourceType;
import edu.duke.risc.shared.commons.UnitType;
import edu.duke.risc.shared.exceptions.InvalidActionException;
import edu.duke.risc.shared.users.Player;

/**
 * Attack Action
 *
 * @author eason
 */
public class AttackAction extends AbstractSourceAction implements TwoStepsAction {

    /**
     * Constructor
     * @param sourceTerritoryId sourceTerritoryId
     * @param destinationId destinationId
     * @param unitType unitType
     * @param number number
     * @param player player
     */
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
            return "You cannot attack owned " + destinationId + ", please try move";
        }
        Territory sourceTerritory = board.getTerritories().get(sourceTerritoryId);
        Territory destTerritory = board.getTerritories().get(destinationId);
        if (!sourceTerritory.getUnitsMap().containsKey(unitType)) {
            return "The source territory does not contain the unit type.";
        }
        if (sourceTerritory.getUnitsMap().get(unitType) < number) {
            return "The source territory does not contain enough unit type.";
        }
        int moveCost;
        if ((moveCost = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId)) == Integer.MAX_VALUE) {
            return "Not reachable from source " + sourceTerritory + " to destination" + destTerritory;
        }
        //whether current player has enough resource to move units
        String error;
        int attackCost = number;
        int totalCost = moveCost + attackCost;
        if ((error = player.hasEnoughResources(ResourceType.FOOD, totalCost)) != null){
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

        //calculate and deduct costs
        Player attackingPlayer = board.findPlayer(playerId);
        int moveCost = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId);
        int attackCost = number;
        int totalCost = moveCost + attackCost;
        attackingPlayer.updateResourceMap(ResourceType.FOOD, -totalCost);

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
        return "";
    }

    @Override
    public String applyAfter(GameBoard board) throws InvalidActionException {
        StringBuilder builder = new StringBuilder();

        Player attackingPlayer = board.getPlayers().get(super.playerId);

        /**
         * -1 if the target place is not occupied
         */
        Integer attackedPlayerId = findPlayerOwnsTerritory(board);
        Player attackedPlayer = board.findPlayer(attackedPlayerId);
        Territory sourceTerritory = board.findTerritory(sourceTerritoryId);
        Territory desTerritory = board.findTerritory(destinationId);
        Integer attackerNumber = number;

        //calculate and deduct costs
        int moveCost = board.calculateMoveCost(sourceTerritoryId, destinationId, playerId);
        int attackCost = number;
        int totalCost = moveCost + attackCost;
        attackingPlayer.updateResourceMap(ResourceType.FOOD, -totalCost);

        builder.append("ATTACK { Attacker: ").append(attackingPlayer.getColor()).append(" PLAYER")
                .append(" with ").append(number).append(" ").append(unitType)
                .append(", costs ").append(totalCost).append(" ").append(ResourceType.FOOD)
                .append(", Defender: PLAYER ").append(attackedPlayer.getColor()).append(" PLAYER")
                .append(" from place ").append(sourceTerritory.getBasicInfo()).append(" to place ")
                .append(desTerritory.getBasicInfo()).append(" }");

        if (!attackedPlayerId.equals(playerId)) {
            int attackerLost = 0, defenderLost = 0;
            while (!desTerritory.isEmptyTerritory()
                    && desTerritory.getUnitsMap().get(unitType) > 0 && attackerNumber != 0) {
                Integer random = randomWin();
                if (random == 0) {
                    //attacked(defender) win, attacker lost
                    attackerNumber -= 1;
                    attackerLost += 1;
                } else {
                    //attacker win, attacked(defender) lost
                    desTerritory.updateUnitsMap(unitType, -1);
                    attackedPlayer.updateTotalUnitMap(unitType, -1);
                    defenderLost += 1;
                }
            }

            builder.append(" with results: attacker lost ").append(attackerLost)
                    .append(" defender lost ").append(defenderLost).append(" : ");

            if (attackerNumber == 0) {
                //attacker lost
                attackingPlayer.updateTotalUnitMap(unitType, -number);
                builder.append(" Attacker lost.");
            } else {
                //attacked win, take the place
                desTerritory.updateUnitsMap(unitType, attackerNumber);
                attackingPlayer.updateTotalUnitMap(unitType, attackerNumber - number);

                attackingPlayer.getOwnedTerritories().add(destinationId);
                attackedPlayer.removeOwnedTerritory(destinationId);
                builder.append("defender lost territory ").append(board.findTerritory(destinationId).getTerritoryName());
            }
        } else {
            desTerritory.updateUnitsMap(unitType, playerId);
            builder.append("Player ").append(playerId).append(" has already occupied this place");
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    /**
     * @return 0 for defender win, 1 for attacker win
     */
    public Integer randomWin() {
        Integer defender = rollADice();
        Integer attacker = rollADice();
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
    public Integer rollADice() {
        return (int) (Math.random() * 20) + 1;
    }


    @Override
    public String toString() {
        return "Attack by player " + playerId + " from territory " + sourceTerritoryId + " to " + destinationId
                 + " with " + number + " " + unitType;
    }

}
