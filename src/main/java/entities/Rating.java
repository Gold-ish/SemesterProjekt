package entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author rando
 */
@Entity
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String movieID;
    private int rating;

    public Rating(String movieID, int rating) {
        this.movieID = movieID;
        this.rating = rating;
    }

    public Rating() {
    }

    public String getMovieID() {
        return movieID;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.movieID);
        hash = 53 * hash + this.rating;
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
        final Rating other = (Rating) obj;
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
        return "Rating{" + "id=" + id + ", movieID=" + movieID + ", rating=" + rating + '}';
    }

}
