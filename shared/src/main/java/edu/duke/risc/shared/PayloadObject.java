package edu.duke.risc.shared;

import com.google.gson.annotations.SerializedName;
import edu.duke.risc.shared.commons.PayloadType;

import java.io.Serializable;
import java.util.Map;

/**
 * Abstract the payload object, with basic information sender, receiver and contents as a map
 *
 * @author eason
 * @date 2021/3/10 11:01
 */
public class PayloadObject implements Serializable {


    @SerializedName("sender")
    private Integer sender;

    @SerializedName("receiver")
    private Integer receiver;

    @SerializedName("messageType")
    private PayloadType messageType;

    @SerializedName("contents")
    private Map<String, Object> contents;

    /**
     * Constructor for payload object
     *
     * @param sender sender id
     * @param receiver receiver id
     * @param messageType type of message, for detailed information, look at corresponding enum
     */
    public PayloadObject(Integer sender, Integer receiver, PayloadType messageType) {
        this(sender, receiver, messageType, null);
    }

    /**
     * Constructor for payload object
     *
     * @param sender sender id
     * @param receiver receiver id
     * @param messageType type of message, for detailed information, look at corresponding enum
     * @param contents contents as a map
     */
    public PayloadObject(Integer sender, Integer receiver, PayloadType messageType, Map<String, Object> contents) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageType = messageType;
        this.contents = contents;
    }

//    public PayloadObject(int defaultPlayerId, int masterId, PayloadType login, Map<String, String> userInfo) {
//    }

    @Override
    public String toString() {
        return "PayloadObject{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", messageType='" + messageType + '\'' +
                ", contents=" + contents +
                '}';
    }

    /**
     * getSender
     * @return sender id
     */
    public Integer getSender() {
        return sender;
    }

    /**
     * getReceiver
     * @return receiver id
     */
    public Integer getReceiver() {
        return receiver;
    }

    /**
     * getMessageType
     * @return getMessageType
     */
    public PayloadType getMessageType() {
        return messageType;
    }

    /**
     * getContents
     * @return contents
     */
    public Map<String, Object> getContents() {
        return contents;
    }

}
