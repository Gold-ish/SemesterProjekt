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

    public MovieDTO(String Title, String Year, String Poster, String imdbID) {
        this.Title = Title;
        this.Year = Year;
        this.Poster = Poster;
        this.imdbID = imdbID;
    }

    public MovieDTO(MovieDTO movieDTO) {
        this.Title = movieDTO.getTitle();
        this.Year = movieDTO.getYear();
        this.Poster = movieDTO.getPoster();
        this.imdbID = movieDTO.getImdbID();
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.Title);
        hash = 89 * hash + Objects.hashCode(this.Year);
        hash = 89 * hash + Objects.hashCode(this.Poster);
        hash = 89 * hash + Objects.hashCode(this.imdbID);
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
        return true;
    }

    @Override
    public String toString() {
        return "MovieDTO{" + "Title=" + Title + ", Year=" + Year + ", Poster=" + Poster + ", imdbID=" + imdbID + '}';
    }
    
}
