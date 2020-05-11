package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * @author Nina
 */
public class TopTenMoviesDTO {

    private List<MovieDTO> movieDTOs = new ArrayList();

    
    public TopTenMoviesDTO(List<MovieDTO> mdto) {
        mdto.forEach((movieDTO) -> {
            movieDTOs.add(movieDTO);
        });
    }

    public List<MovieDTO> getMovieDTOs() {
        return movieDTOs;
    }

    public void setMovieDTOs(List<MovieDTO> movieDTOs) {
        this.movieDTOs = movieDTOs;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.movieDTOs);
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
        final TopTenMoviesDTO other = (TopTenMoviesDTO) obj;
        if (!Objects.equals(this.movieDTOs, other.movieDTOs)) {
            return false;
        }
        return true;
    }
}
