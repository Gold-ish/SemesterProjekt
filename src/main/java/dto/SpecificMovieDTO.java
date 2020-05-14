package dto;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author rando
 */
public class SpecificMovieDTO {

    private final String Title;
    private final String Year;
    private final String Rated;
    private final String Released;
    private final String Runtime;
    private final String Genre;
    private final String Director;
    private final String Actors;
    private final String Plot;
    private final String Language;
    private final String Awards;
    private final String Poster;
    private final String imdbID;
    private final String Type;
    private final String DVD;
    private final String Production;
    private double avgRating;
    private double avgRatingCritic;
    private List<ReviewDTO> review;
    private List<RatingDTO> rating;

    public SpecificMovieDTO(String Title, String Year, String Rated, String Released, String Runtime, String Genre, String Director, String Actors, String Plot, String Language, String Awards, String Poster, String imdbID, String Type, String DVD, String Production) {
        this.Title = Title;
        this.Year = Year;
        this.Rated = Rated;
        this.Released = Released;
        this.Runtime = Runtime;
        this.Genre = Genre;
        this.Director = Director;
        this.Actors = Actors;
        this.Plot = Plot;
        this.Language = Language;
        this.Awards = Awards;
        this.Poster = Poster;
        this.imdbID = imdbID;
        this.Type = Type;
        this.DVD = DVD;
        this.Production = Production;
        this.avgRating = 0.0;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
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

    public String getDirector() {
        return Director;
    }

    public String getActors() {
        return Actors;
    }

    public String getPlot() {
        return Plot;
    }

    public String getLanguage() {
        return Language;
    }

    public String getAwards() {
        return Awards;
    }

    public String getPoster() {
        return Poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getDVD() {
        return DVD;
    }

    public String getProduction() {
        return Production;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<ReviewDTO> getReviews() {
        return review;
    }

    public void setReviews(List<ReviewDTO> review) {
        this.review = review;
    }

    public List<RatingDTO> getRatings() {
        return rating;
    }

    public void setRatings(List<RatingDTO> rating) {
        this.rating = rating;
    }

    public double getAvgRatingCritic() {
        return avgRatingCritic;
    }

    public void setAvgRatingCritic(double avgRatingCritic) {
        this.avgRatingCritic = avgRatingCritic;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.Title);
        hash = 43 * hash + Objects.hashCode(this.Year);
        hash = 43 * hash + Objects.hashCode(this.Rated);
        hash = 43 * hash + Objects.hashCode(this.Released);
        hash = 43 * hash + Objects.hashCode(this.Runtime);
        hash = 43 * hash + Objects.hashCode(this.Genre);
        hash = 43 * hash + Objects.hashCode(this.Director);
        hash = 43 * hash + Objects.hashCode(this.Actors);
        hash = 43 * hash + Objects.hashCode(this.Plot);
        hash = 43 * hash + Objects.hashCode(this.Language);
        hash = 43 * hash + Objects.hashCode(this.Awards);
        hash = 43 * hash + Objects.hashCode(this.Poster);
        hash = 43 * hash + Objects.hashCode(this.imdbID);
        hash = 43 * hash + Objects.hashCode(this.Type);
        hash = 43 * hash + Objects.hashCode(this.DVD);
        hash = 43 * hash + Objects.hashCode(this.Production);
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.avgRating) ^ (Double.doubleToLongBits(this.avgRating) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.avgRatingCritic) ^ (Double.doubleToLongBits(this.avgRatingCritic) >>> 32));
        hash = 43 * hash + Objects.hashCode(this.review);
        hash = 43 * hash + Objects.hashCode(this.rating);
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
        final SpecificMovieDTO other = (SpecificMovieDTO) obj;
        if (Double.doubleToLongBits(this.avgRating) != Double.doubleToLongBits(other.avgRating)) {
            return false;
        }
        if (Double.doubleToLongBits(this.avgRatingCritic) != Double.doubleToLongBits(other.avgRatingCritic)) {
            return false;
        }
        if (!Objects.equals(this.Title, other.Title)) {
            return false;
        }
        if (!Objects.equals(this.Year, other.Year)) {
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
        if (!Objects.equals(this.Director, other.Director)) {
            return false;
        }
        if (!Objects.equals(this.Actors, other.Actors)) {
            return false;
        }
        if (!Objects.equals(this.Plot, other.Plot)) {
            return false;
        }
        if (!Objects.equals(this.Language, other.Language)) {
            return false;
        }
        if (!Objects.equals(this.Awards, other.Awards)) {
            return false;
        }
        if (!Objects.equals(this.Poster, other.Poster)) {
            return false;
        }
        if (!Objects.equals(this.imdbID, other.imdbID)) {
            return false;
        }
        if (!Objects.equals(this.Type, other.Type)) {
            return false;
        }
        if (!Objects.equals(this.DVD, other.DVD)) {
            return false;
        }
        if (!Objects.equals(this.Production, other.Production)) {
            return false;
        }
        if (!Objects.equals(this.review, other.review)) {
            return false;
        }
        if (!Objects.equals(this.rating, other.rating)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SpecificMovieDTO{" + "Title=" + Title + ", Year=" + Year + ", Rated=" + Rated + ", Released=" + Released + ", Runtime=" + Runtime + ", Genre=" + Genre + ", Director=" + Director + ", Actors=" + Actors + ", Plot=" + Plot + ", Language=" + Language + ", Awards=" + Awards + ", Poster=" + Poster + ", imdbID=" + imdbID + ", Type=" + Type + ", DVD=" + DVD + ", Production=" + Production + ", avgRating=" + avgRating + ", avgRatingCritic=" + avgRatingCritic + ", review=" + review + ", rating=" + rating + '}';
    }
    
    
}
