package entities;

import dto.UserDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@NamedQuery(name = "User.deleteAllRows", query = "DELETE FROM User")
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_name", length = 25)
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String userPass;
    @JoinTable(name = "user_roles", joinColumns = {
        @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
        @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<Role> roleList = new ArrayList();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList();
    private String gender;
    private String birthday;
    
    public User() {
    }

    public User(String userName, String userPass) {
        this.userName = userName;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(10));
    }
    
    public User(String userName, String userPass, String gender, String birthday) {
        this.userName = userName;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(10));
        this.gender = gender;
        this.birthday = birthday;
    }

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList();
        for (Role role : roleList) {
            rolesAsStrings.add(role.getRoleName());
        }
        return rolesAsStrings;
    }

    public String getRolesAsString() {
        if (roleList.isEmpty()) {
            return null;
        }
        StringBuilder rolesAsString = new StringBuilder();
        for (Role role : roleList) {
            rolesAsString.append(role.getRoleName()).append(", ");
        }
        return rolesAsString.toString().substring(0, rolesAsString.length() - 2);
    }

    //TODO Change when password is hashed
    public boolean verifyPassword(String pw) {
        return (BCrypt.checkpw(pw, userPass));
    }

    public User(UserDTO userDTO) {
        this.userName = userDTO.getUsername();
        this.userPass = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(10));
        this.gender = userDTO.getGender();
        this.birthday = userDTO.getBirthday();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(10));
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setUser(this);
    }
    
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    public void addRating(Rating rating) {
        ratings.add(rating);
        rating.setUser(this);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

}
