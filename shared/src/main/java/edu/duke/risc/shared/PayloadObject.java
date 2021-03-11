package edu.duke.risc.shared;

import edu.duke.risc.shared.commons.PayloadType;

import java.io.Serializable;
import java.util.Map;

/**
 * @author eason
 * @date 2021/3/10 11:01
 */
public class PayloadObject implements Serializable {

    private Integer sender;

    private Integer receiver;

    private PayloadType messageType;

    private Map<String, Object> contents;

    public PayloadObject() {
    }

    public PayloadObject(Integer sender, Integer receiver, PayloadType messageType, Map<String, Object> contents) {
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

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public void setMessageType(PayloadType messageType) {
        this.messageType = messageType;
    }

    public void setContents(Map<String, Object> contents) {
        this.contents = contents;
    }


    public PayloadType getMessageType() {
        return messageType;
    }

    public Map<String, Object> getContents() {
        return contents;
    }

}
