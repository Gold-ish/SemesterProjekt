/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import dto.MovieDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.HttpUtils;

/**
 *
 * @author rando
 */
public class fetchtest {

    private static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        String searchString = "star";
        int page = 1;
        String searchAPIdata = HttpUtils.fetchData("https://omdbapi.com/?s=" + searchString + "&page=" + page + "&apikey=6b10a5de");
        System.out.println(searchAPIdata);
        
        String[] parts = searchAPIdata.split("\\{");
        System.out.println(parts[0]);
        System.out.println(parts[1]);
        System.out.println(parts[2].substring(0, parts[2].length()-1));
        List<MovieDTO> JsonData = new ArrayList();
        JsonData.add(gson.fromJson("{" + parts[2].substring(0, parts[2].length()-1) + "}"), MovieDTO.class));
        System.out.println(JsonData.get(0));
        
        
        
        
        
        /*StringTokenizer multiTokenizer = new StringTokenizer(searchAPIdata, "{}");
        while (multiTokenizer.hasMoreTokens()) {
            //if(!multiTokenizer.nextToken().contains("Search")){
                System.out.println("{" + multiTokenizer.nextToken() + "}");
        //        JsonData.add(gson.fromJson("{" + multiTokenizer.nextToken() + "}", MovieDTO.class));
            //}
        }*/
//        JsonData.forEach((_item) -> {
//            System.out.println(_item);
//        });
//        String[] arrOfStr = searchAPIdata.split("{");
//        for (String a: arrOfStr)
//            System.out.println(a);
//    }
        //MovieDTO a = gson.fromJson(searchAPIdata, MovieDTO.class);
        //System.out.println(a);
//MovieDTO mdto = new MovieDTO(searchAPIdata.);
    }
}
