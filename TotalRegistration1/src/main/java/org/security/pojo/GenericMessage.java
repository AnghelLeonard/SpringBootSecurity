package org.security.pojo;

/**
 *
 * @author newlife
 */
public class GenericMessage {

    private String sender;
    private String receiver;
    private String payload;

    public GenericMessage() {
    }

    public GenericMessage(String sender, String receiver, String payload) {
        this.sender = sender;
        this.receiver = receiver;
        this.payload = payload;
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
        return "GenericMessage{" + "sender=" + sender + ", receiver=" + receiver + ", payload=" + payload + '}';
    }
}
