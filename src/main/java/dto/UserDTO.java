package dto;

import entities.Rating;
import entities.Review;
import entities.User;
import java.util.ArrayList;
import java.util.List;
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
    private List<Review> reviews = new ArrayList();
    private List<Rating> ratings = new ArrayList();

    public UserDTO(String username, String password, String gender, String birthday) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
        this.gender = gender;
        this.birthday = birthday;
    }
    
    public UserDTO(User user) {
        this.username = user.getUserName();
        this.password = user.getUserPass();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.ratings = user.getRatings();
        this.reviews = user.getReviews();
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
    public String toString() {
        return "UserDTO{" + "username=" + username + ", password=" + password + ", gender=" + gender + ", birthday=" + birthday + '}';
    }

}
