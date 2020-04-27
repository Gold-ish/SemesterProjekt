package fetcher;

import com.google.gson.Gson;
import dto.KanyeRestDTO;
import java.io.IOException;
import utils.HttpUtils;

/**
 *
 * @author rando
 */
public class KanyeRestFetcher implements FetcherInterface {
    private String url;
    private KanyeRestDTO kanyeRestDto;

    public KanyeRestFetcher(String url) {
        this.url = url;
    }
    
    @Override
    public void doWork() throws IOException {
        Gson gson = new Gson();
        String kanyeRestAPI = HttpUtils.fetchData(url);
        kanyeRestDto = gson.fromJson(kanyeRestAPI, KanyeRestDTO.class);
        //System.out.println(kanyeRestDTO);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public KanyeRestDTO getKanyeRestDto() {
        return kanyeRestDto;
    }

    public void setKanyeRestDto(KanyeRestDTO kanyeRestDto) {
        this.kanyeRestDto = kanyeRestDto;
    }
    
}
