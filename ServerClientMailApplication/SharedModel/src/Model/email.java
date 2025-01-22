package Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class email implements Serializable {

    private final user sender;

    private final ArrayList<user> receivers;

    private final String object;

    private final String content;

    private final String date;

    private int state;

    private user owner = null;


    public email(user sender, ArrayList<user> recivers, String object, String content) {
        this.sender = sender;
        this.receivers = recivers;
        this.object = object;
        this.content = content;
        this.date =  DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
        this.state = 0;
    }

    public email(user sender, ArrayList<user> recivers, String object, String content, String date) {
        this.sender = sender;
        this.receivers = recivers;
        this.object = object;
        this.content = content;
        this.date = date;
        this.state = 0;
    }

    public user getSender() {
        return sender;
    }

    public ArrayList<user> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<user> newReceivers) {
        receivers.clear();
        receivers.addAll(newReceivers);
    }

    public String getObject() {
        return object;
    }

    public String getContent() {
        return content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof email) {
            email email = (email) o;
            result = sender.equals(email.getSender()) && receivers.equals(email.getReceivers())
                    && object.equals(email.getObject()) && content.equals(email.getContent()) && date.equals(email.getDate());
        }return result;
    }

   @Override
    public String toString() {
        String preview;
        if(object.length()>4){
            preview = object.substring(0,3).concat("...");
        }else{
            preview = object;
        }
       return date + " | From: " + sender + " | Object: " + preview;
    }

    public String getDate() {
        return date;
    }

    public user getOwner() {
        return owner;
    }

    public void setOwner(user owner) {
        this.owner = owner;
    }
}
