package edu.duke.risc.shared;

import edu.duke.risc.shared.board.GameStage;
import edu.duke.risc.shared.commons.PayloadType;
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

    private PayloadType messageType;

    private Map<String, Object> contents;

    public PayloadObject() {
    }

    public PayloadObject(GameUser sender, GameUser receiver, PayloadType messageType, Map<String, Object> contents) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageType = messageType;
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "PayloadObject{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", messageType='" + messageType + '\'' +
                ", contents=" + contents +
                '}';
    }

    public void setSender(GameUser sender) {
        this.sender = sender;
    }

    public void setReceiver(GameUser receiver) {
        this.receiver = receiver;
    }

    public void setMessageType(PayloadType messageType) {
        this.messageType = messageType;
    }

    public void setContents(Map<String, Object> contents) {
        this.contents = contents;
    }

    public GameUser getSender() {
        return sender;
    }

    public GameUser getReceiver() {
        return receiver;
    }

    public PayloadType getMessageType() {
        return messageType;
    }

    public Map<String, Object> getContents() {
        return contents;
    }

}
