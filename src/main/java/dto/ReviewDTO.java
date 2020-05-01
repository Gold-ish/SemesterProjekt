package dto;

import entities.Review;
import java.util.Objects;

/*
 * @author Nina
 */
public class ReviewDTO {
    
    private int id;
    private String movieID;
    private String review;

    public ReviewDTO() {
    }
    
    public ReviewDTO(int id, String movieID, String review) {
        this.id = id;
        this.movieID = movieID;
        this.review = review;
    }

    public ReviewDTO(String movieID, String review) {
        this.movieID = movieID;
        this.review = review;
    }

  public ReviewDTO(Review r) {
        this.id = r.getId();
        this.movieID = r.getMovieID();
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.movieID);
        hash = 79 * hash + Objects.hashCode(this.review);
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
        if (!Objects.equals(this.review, other.review)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" + "id=" + id + ", movieID=" + movieID + ", review=" + review + '}';
    }
}