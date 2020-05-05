package dto;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author rando
 */
public class UserDTO {

    private final String username;
    private final String password;
    private final String gender;
    private final String birthday;

    public UserDTO(String username, String password, String gender, String birthday) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", password=" + password + ", gender=" + gender + ", birthday=" + birthday + '}';
    }

}
