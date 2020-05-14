package dto;

import entities.Rating;
import entities.Review;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private String role;
    private List<Review> reviews = new ArrayList();
    private List<Rating> ratings = new ArrayList();

    public UserDTO(String username, String password, String gender, String birthday, String role) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
        this.gender = gender;
        this.birthday = birthday;
        this.role = role;
    }

    //Only used for find opperations. We don't want to show the password.
    public UserDTO(User user) {
        this.username = user.getUserName();
        this.password = null;
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.role = user.getRolesAsString();
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserDTO other = (UserDTO) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.gender, other.gender)) {
            return false;
        }
        if (!Objects.equals(this.birthday, other.birthday)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        if (!Objects.equals(this.reviews, other.reviews)) {
            return false;
        }
        if (!Objects.equals(this.ratings, other.ratings)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.username);
        hash = 73 * hash + Objects.hashCode(this.password);
        hash = 73 * hash + Objects.hashCode(this.gender);
        hash = 73 * hash + Objects.hashCode(this.birthday);
        hash = 73 * hash + Objects.hashCode(this.role);
        hash = 73 * hash + Objects.hashCode(this.reviews);
        hash = 73 * hash + Objects.hashCode(this.ratings);
        return hash;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", password=" + password + ", gender=" + gender + ", birthday=" + birthday + '}';
    }

}
