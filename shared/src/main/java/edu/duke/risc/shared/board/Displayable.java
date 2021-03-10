package edu.duke.risc.shared.board;

import edu.duke.risc.shared.users.Player;

import java.util.Set;

/**
 * @author eason
 * @date 2021/3/10 21:27
 */
public interface Displayable {

    /**
     * Display the current board
     */
    public void display(Set<Player> players);

}
