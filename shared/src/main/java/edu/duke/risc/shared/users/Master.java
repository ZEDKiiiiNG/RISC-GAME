package edu.duke.risc.shared.users;

import edu.duke.risc.shared.Configurations;

import java.io.Serializable;

/**
 * @author eason
 * @date 2021/3/10 19:31
 */
public class Master implements GameUser, Serializable {

    @Override
    public boolean isMaster() {
        return true;
    }

    @Override
    public int getId() {
        return Configurations.MASTER_ID;
    }


}
