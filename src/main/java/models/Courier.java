package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courier {

    private String login;
    private String password;
    private String firstName;


    public Courier withLogin(String login){
        this.login = login;
        return this;
    }

    public Courier withPassword(String password){
        this.password = password;
        return this;
    }

    public Courier withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }
}
