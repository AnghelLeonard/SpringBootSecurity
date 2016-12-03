package org.security.pojo;

import org.security.util.MessageType;

/**
 *
 * @author newlife
 */
public class GenericMessage {

    private MessageType type;
    private String sender;
    private String receiver;
    private String payload;

    public GenericMessage() {
    }

    public GenericMessage(String receiver, MessageType type) {
        this.receiver = receiver;
        this.type = type;
    }

    public GenericMessage(String receiver, String payload, MessageType type) {        
        this.receiver = receiver;
        this.payload = payload;
        this.type = type;
    }        

    public GenericMessage(MessageType type, String sender, String receiver, String payload) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.payload = payload;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "GenericMessage{" + "sender=" + sender + ", "
                + "receiver=" + receiver + ", payload=" + payload + '}';
    }
}
