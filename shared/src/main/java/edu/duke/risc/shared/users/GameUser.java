package edu.duke.risc.shared.users;

/**
 * Represents all game users: master, player, possibly observer.
 *
 * @author yichen.hua
 * @date 2021/3/9 19:58
 */
public interface GameUser {

    /**
     * Check whether the user is a master -- server.
     *
     * @return true if the user is a master, false if not.
     */
    public boolean isMaster();

}
