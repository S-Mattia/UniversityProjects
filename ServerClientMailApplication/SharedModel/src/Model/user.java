package Model;

import java.io.Serializable;

public class user implements Serializable {
    private final String email;
    private int state;

    public user(String email) {
        this.state = 0;
        this.email = email;
    }

    public String getEmail() {
        return email;
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
        if (o instanceof user) {
            user user = (user) o;
            result = user.email.equals(email);
        }return result;
    }

    @Override
    public String toString() {
        return email;
    }
}
