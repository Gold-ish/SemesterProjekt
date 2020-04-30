package dto;

import java.util.Objects;

/**
 *
 * @author carol
 */
public class MovieDTO {
    
    private final String Title;
    private final int Year;
    private final String Poster;
    private final String imdbID;
    private double avgRating;

    public MovieDTO(String Title, int Year, String Poster, String imdbID) {
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

    public int getYear() {
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.Title);
        hash = 79 * hash + this.Year;
        hash = 79 * hash + Objects.hashCode(this.Poster);
        hash = 79 * hash + Objects.hashCode(this.imdbID);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.avgRating) ^ (Double.doubleToLongBits(this.avgRating) >>> 32));
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
        if (this.Year != other.Year) {
            return false;
        }
        if (Double.doubleToLongBits(this.avgRating) != Double.doubleToLongBits(other.avgRating)) {
            return false;
        }
        if (!Objects.equals(this.Title, other.Title)) {
            return false;
        }
        if (!Objects.equals(this.Poster, other.Poster)) {
            return false;
        }
        if (!Objects.equals(this.imdbID, other.imdbID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovieDTO{" + "Title=" + Title + ", Year=" + Year + ", Poster=" + Poster + ", imdbID=" + imdbID + ", avgRating=" + avgRating + '}';
    }

}
