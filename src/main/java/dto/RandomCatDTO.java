package dto;

/**
 *
 * @author rando
 */
public class RandomCatDTO {

    private final String file;

    public RandomCatDTO(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "RandomCatDTO{" + "file=" + file + '}';
    }

}
