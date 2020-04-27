package fetcher;

import com.google.gson.Gson;
import dto.ChuckDTO;
import java.io.IOException;
import utils.HttpUtils;

/**
 *
 * @author rando
 */
public class ChuckFetcher implements FetcherInterface {

    private String url;
    private ChuckDTO chuckDTO;

    public ChuckFetcher(String url) {
        this.url = url;
    }

    @Override
    public void doWork() throws IOException {
        Gson gson = new Gson();
        String icndb = HttpUtils.fetchData(url);
        chuckDTO = gson.fromJson(icndb, ChuckDTO.class);
        //System.out.println(icndbDTO);
    }

    public String getUrl() {
        return url;
    }

    public ChuckDTO getChuckDTO() {
        return chuckDTO;
    }

}
