import data.DBConnection;
import util.SiteMap;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
//        String SITE_URL = "http://www.playback.ru/";
        String SITE_URL = "https://volochek.life/";
//        String SITE_URL = "http://radiomv.ru/";
//        String SITE_URL = "https://ipfran.ru";
//        String SITE_URL = "https://dimonvideo.ru/";
//        String SITE_URL = "https://nikoartgallery.com/";
//        String SITE_URL = "https://et-cetera.ru/mobile/";
//        String SITE_URL = "https://www.lutherancathedral.ru/";
//        String SITE_URL = "https://dombulgakova.ru/";

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new SiteMap(SITE_URL, ""));

        DBConnection.execMultiInsert();
        DBConnection.indexPath();
    }
}