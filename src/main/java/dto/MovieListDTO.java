package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rando
 */
public class MovieListDTO {

    private List<MovieDTO> movieDTOs = new ArrayList();

    public MovieListDTO(List<MovieDTO> m) {
        m.forEach((movieDTO) -> {
            movieDTOs.add(new MovieDTO(movieDTO));
        });
    }

    public List<MovieDTO> getMovieDTOs() {
        return movieDTOs;
    }

}
