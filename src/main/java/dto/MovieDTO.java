package dto;

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
    private final String Plot;
    private final String Rated;
    private final String Released;
    private final String Runtime;
    private final String Genre;
    private double avgRating;

    public MovieDTO(String Title, String Year, String Poster, String imdbID, String Plot, String Rated, String Released, String Runtime, String Genre) {
        this.Title = Title;
        this.Year = Year;
        this.Poster = Poster;
        this.imdbID = imdbID;
        this.Plot = Plot;
        this.Rated = Rated;
        this.Released = Released;
        this.Runtime = Runtime;
        this.Genre = Genre;
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
    
    
    public String getPlot() {
        return Plot;
    }

    public String getRated() {
        return Rated;
    }

    public String getReleased() {
        return Released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public String getGenre() {
        return Genre;
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
        hash = 13 * hash + Objects.hashCode(this.Title);
        hash = 13 * hash + Objects.hashCode(this.Year);
        hash = 13 * hash + Objects.hashCode(this.Poster);
        hash = 13 * hash + Objects.hashCode(this.imdbID);
        hash = 13 * hash + Objects.hashCode(this.Plot);
        hash = 13 * hash + Objects.hashCode(this.Rated);
        hash = 13 * hash + Objects.hashCode(this.Released);
        hash = 13 * hash + Objects.hashCode(this.Runtime);
        hash = 13 * hash + Objects.hashCode(this.Genre);
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.avgRating) ^ (Double.doubleToLongBits(this.avgRating) >>> 32));
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
        if (!Objects.equals(this.Plot, other.Plot)) {
            return false;
        }
        if (!Objects.equals(this.Rated, other.Rated)) {
            return false;
        }
        if (!Objects.equals(this.Released, other.Released)) {
            return false;
        }
        if (!Objects.equals(this.Runtime, other.Runtime)) {
            return false;
        }
        if (!Objects.equals(this.Genre, other.Genre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovieDTO{" + "Title=" + Title + ", Year=" + Year + ", Poster=" + Poster + ", imdbID=" + imdbID + ", Plot=" + Plot + ", Rated=" + Rated + ", Released=" + Released + ", Runtime=" + Runtime + ", Genre=" + Genre + ", avgRating=" + avgRating + '}';
    }

    
    
}
