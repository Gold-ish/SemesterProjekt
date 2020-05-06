package entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/*
 * @author Nina
 */
@Entity
@NamedQueries(
    @NamedQuery(name = "Review.deleteAllRows", query = "DELETE FROM Review"))
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String movieID;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_name")
    private User user;
    private String review;

    public Review() {
    }

    public Review(String movieID, User user, String review) {
        this.movieID = movieID;
        this.user = user;
        this.review = review;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.id;
        hash = 19 * hash + Objects.hashCode(this.movieID);
        hash = 19 * hash + Objects.hashCode(this.user);
        hash = 19 * hash + Objects.hashCode(this.review);
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
        final Review other = (Review) obj;
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
        return true;
    }

    @Override
    public String toString() {
        return "Review{" + "id=" + id + ", movieID=" + movieID + ", user=" + user + ", review=" + review + '}';
    }

}
