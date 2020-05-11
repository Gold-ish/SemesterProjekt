package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author rando
 */
public class MovieListDTO {

    private List<MovieDTO> movieDTOs = new ArrayList();
    private int totalResults;

    public MovieListDTO(List<MovieDTO> m , int totalResults) {
        this.totalResults = totalResults;
        m.forEach((movieDTO) -> {
            movieDTOs.add(movieDTO);
        });
    }

    public List<MovieDTO> getMovieDTOs() {
        return movieDTOs;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.movieDTOs);
        hash = 37 * hash + this.totalResults;
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
        final MovieListDTO other = (MovieListDTO) obj;
        if (this.totalResults != other.totalResults) {
            return false;
        }
        if (!Objects.equals(this.movieDTOs, other.movieDTOs)) {
            return false;
        }
        return true;
    }
    
    

}
