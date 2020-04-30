package dto;

import entities.Review;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author carol
 */
public class MovieDTO {
    
    private final String Title;
    private final String Year;
    private final String Poster;
    private final String imdbID;
    private double avgRating;
    private List<Review> review;

    public MovieDTO(String Title, String Year, String Poster, String imdbID) {
        this.Title = Title;
        this.Year = Year;
        this.Poster = Poster;
        this.imdbID = imdbID;
        this.avgRating = 0.0;
    }

    public MovieDTO(MovieDTO movieDTO) {
        this.Title = movieDTO.getTitle();
        this.Year = movieDTO.getYear();
        this.Poster = movieDTO.getPoster();
        this.imdbID = movieDTO.getImdbID();
        this.avgRating = 0.0;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getPoster() {
        return Poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<Review> getReview() {
        return review;
    }
    
    public void setReviews(List<Review> review) {
        this.review = review;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.Title);
        hash = 23 * hash + Objects.hashCode(this.Year);
        hash = 53 * hash + Objects.hashCode(this.Poster);
        hash = 53 * hash + Objects.hashCode(this.imdbID);
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.avgRating) ^ (Double.doubleToLongBits(this.avgRating) >>> 32));
        hash = 53 * hash + Objects.hashCode(this.review);
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
        final MovieDTO other = (MovieDTO) obj;
        if (Double.doubleToLongBits(this.avgRating) != Double.doubleToLongBits(other.avgRating)) {
            return false;
        }
        if (!Objects.equals(this.Title, other.Title)) {
            return false;
        }
        if (!Objects.equals(this.Year, other.Year)) {
            return false;
        }
        if (!Objects.equals(this.Poster, other.Poster)) {
            return false;
        }
        if (!Objects.equals(this.imdbID, other.imdbID)) {
            return false;
        }
        if (!Objects.equals(this.review, other.review)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovieDTO{" + "Title=" + Title + ", Year=" + Year + ", Poster=" + Poster + ", imdbID=" + imdbID + ", avgRating=" + avgRating + ", review=" + review + '}';
    }
}
