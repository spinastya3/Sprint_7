package models;

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

    public  Courier(String login, String password, String firstName){
        this.login=login;
        this.password=password;
        this.firstName=firstName;
    }
    public Courier(){
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
