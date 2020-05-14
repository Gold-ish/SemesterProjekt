package dto;

import entities.Review;
import java.util.Objects;

/*
 * @author Nina
 */
public class ReviewDTO {
    
    private int id;
    private String movieID;
    private String user;
    private String review;
    private String role;

    public ReviewDTO() {
    }
    
    public ReviewDTO(Number id, String movieID, String user, String review, String role) {
        this.id = id.intValue();
        this.movieID = movieID;
        this.user = user;
        this.review = review;
        this.role = role;
    }

    public ReviewDTO(int id, String movieID, String user, String review) {
        this.id = id;
        this.movieID = movieID;
        this.user = user;
        this.review = review;
    }
    
    public ReviewDTO(String movieID, String user, String review) {
        this.movieID = movieID;
        this.user = user;
        this.review = review;
    }
    
    public ReviewDTO(String movieID, String user, String review, String Role) {
        this.movieID = movieID;
        this.user = user;
        this.review = review;
        this.role = role;
    }

  public ReviewDTO(Review r) {
        this.id = r.getId();
        this.movieID = r.getMovieID();
        this.user = r.getUser();
        this.review = r.getReview();
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
        hash = 89 * hash + Objects.hashCode(this.movieID);
        hash = 89 * hash + Objects.hashCode(this.user);
        hash = 89 * hash + Objects.hashCode(this.review);
        hash = 89 * hash + Objects.hashCode(this.role);
        return hash;
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
        final ReviewDTO other = (ReviewDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.movieID, other.movieID)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.review, other.review)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" + "id=" + id + ", movieID=" + movieID + ", user=" + user + ", review=" + review + ", role=" + role + '}';
    }
    
    
    

}
