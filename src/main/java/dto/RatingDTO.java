package dto;

import entities.Rating;
import java.util.Objects;

/**
 *
 * @author allan
 */
public class RatingDTO {

    private int id;
    private String movieID;
    private String user;
    private int rating;

    public RatingDTO() {
    }

    public RatingDTO(int id, String movieID, String user, int rating) {
        this.id = id;
        this.movieID = movieID;
        this.user = user;
        this.rating = rating;
    }

    public RatingDTO(String movieID, String user, int rating) {
        this.movieID = movieID;
        this.user = user;
        this.rating = rating;
    }

    public RatingDTO(Rating r) {
        this.id = r.getId();
        this.movieID = r.getMovieID();
        this.user = r.getUser();
        this.rating = r.getRating();
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserName() {
        return user;
    }

    public void setUserName(String user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.movieID);
        hash = 97 * hash + Objects.hashCode(this.user);
        hash = 97 * hash + this.rating;
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
        final RatingDTO other = (RatingDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.rating != other.rating) {
            return false;
        }
        if (!Objects.equals(this.movieID, other.movieID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RatingDTO{" + "id=" + id + ", movieID=" + movieID + ", userName=" + user + ", rating=" + rating + '}';
    }

    
}
