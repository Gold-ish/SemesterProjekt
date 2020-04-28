package dto;

import java.util.Objects;

/**
 *
 * @author carol
 */
public class MovieDTO {
    
    private final String title;
    private final String releaseYear;
    private final String poster;
    private final String imdbId;

    public MovieDTO(String title, String releaseYear, String poster, String imdbId) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.poster = poster;
        this.imdbId = imdbId;
    }

    public MovieDTO(MovieDTO movieDTO) {
        this.title = movieDTO.getTitle();
        this.releaseYear = movieDTO.getReleaseYear();
        this.poster = movieDTO.getPoster();
        this.imdbId = movieDTO.getImdbId();
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getPoster() {
        return poster;
    }

    public String getImdbId() {
        return imdbId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.title);
        hash = 23 * hash + Objects.hashCode(this.releaseYear);
        hash = 23 * hash + Objects.hashCode(this.poster);
        hash = 23 * hash + Objects.hashCode(this.imdbId);
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
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.releaseYear, other.releaseYear)) {
            return false;
        }
        if (!Objects.equals(this.poster, other.poster)) {
            return false;
        }
        if (!Objects.equals(this.imdbId, other.imdbId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovieDTO{" + "title=" + title + ", releaseYear=" + releaseYear + ", poster=" + poster + ", imdbId=" + imdbId + '}';
    }
    
    
}
