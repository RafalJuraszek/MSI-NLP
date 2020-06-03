package lab6;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private static final String FILMS_URL = "https://fdb.pl/filmy?genre%%5B%%5D=%d&page=%d";
    private static final int pageNumber = 100;
    private static final HashMap<Integer, String> filmTypeMap= new HashMap<>();
    private static final String filePath = "films.csv";

    static {

        filmTypeMap.put(11, "Dokument");
        filmTypeMap.put(23, "Fantasy");
    }

    public static void main(String[] args) throws Exception {
        File file = new File(filePath);

        // create FileWriter object with file as parameter
        FileWriter outputfile = new FileWriter(file);

        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile);

        // create a List which contains String array
        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] { "Title", "Type", "Description"});



        for(int i=1;i<=pageNumber;i++) {

            for(Integer type: filmTypeMap.keySet()){
                String url = String.format(FILMS_URL, type, i);
                System.out.println(url);
                Document doc = Jsoup.connect(url).get();
                Elements row = doc.getElementsByClass("media-body");

                for(Element el: row) {
                    String title = el.getElementsByClass("h4-xs h3-sm h3-md bold-xs media-heading").get(0).getElementsByTag("a").text();
                    String desc = el.getElementsByClass("truncate truncate-2-lines").get(0).text();
                    if(desc.isEmpty()) {
                        continue;
                    }
                    String kind = "";
//                    Elements filmsType = el.getElementsByClass("list-inline").get(0).getElementsByTag("a");
//                    for(Element filmType: filmsType) {
//                        System.out.println(filmType.text());
//
//                        if(filmType.text().equals(filmTypeMap.get(type))) {
//
//                            kind = filmType.text();
//                            break;
//                        }
//                    }
//                    if(kind.isEmpty()) {
//                        continue;
//                    }
                    kind = filmTypeMap.get(type);

                    data.add(new String[] { title, kind, desc});

                }
            }
        }

        writer.writeAll(data);

        // closing writer connection
        writer.close();

    }
}
