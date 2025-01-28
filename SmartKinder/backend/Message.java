package androidArmy.SmartKinder.backend;

import java.util.Date;

public class Message {
    private int messageId;
    private Date date;
    private String content;
    private int senderId;
    private int receiverId;

    public Message(int messageId, Date date, String content, int senderId, int receiverId) {
        this.messageId = messageId;
        this.date = date;
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
}