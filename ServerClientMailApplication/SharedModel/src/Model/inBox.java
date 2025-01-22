package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class inBox implements Serializable {
    private ArrayList<email> emailist;
    private user user;

    public inBox(ArrayList<email> emailist,user user) {
        this.emailist = emailist;
        this.user = user;
    }

    public user getUser() {
        return user;
    }

    public ArrayList<email> getEmailist() {
        for (email email : emailist) {
            if(email.getState()==0){
                email.setState(1);
            }
        }
        return emailist;
    }

    public void setEmailist(ArrayList<email> emailist) {
        this.emailist = emailist;
    }

    public ArrayList<email> newEmail(){
        ArrayList<email> result=new ArrayList<>();
        for (email email : emailist) {
            if(email.getState()==0){
                email.setState(1);
                result.add(email);
            }
        }
        return result;
    }

    public boolean removeEmail(email email){
        return emailist.remove(email);
    }

    public void addEmail(email email){
        emailist.add(email);
    }

}
