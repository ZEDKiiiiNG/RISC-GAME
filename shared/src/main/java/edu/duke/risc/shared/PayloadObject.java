package edu.duke.risc.shared;

import edu.duke.risc.shared.board.GameStage;
import edu.duke.risc.shared.users.GameUser;

import java.io.Serializable;
import java.util.Map;

/**
 * @author eason
 * @date 2021/3/10 11:01
 */
public class PayloadObject implements Serializable {

    private GameUser sender;

    private GameUser receiver;

    private String messageType;

    private Map<String, Object> contents;

    private GameStage gameStage;

    @Override
    public String toString() {
        return "PayloadObject{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", messageType='" + messageType + '\'' +
                ", contents=" + contents +
                ", gameStage=" + gameStage +
                '}';
    }
}
