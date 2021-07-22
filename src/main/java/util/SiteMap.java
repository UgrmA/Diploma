package util;

import data.DBConnection;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

import static configuration.Parameters.JSOUP_REFERRER;
import static configuration.Parameters.JSOUP_USER_AGENT;

public class SiteMap extends RecursiveAction {
    private static final Set<String> linksSeen = ConcurrentHashMap.newKeySet();
    private final String url;
    private final String child;
    private final List<Element> links;

    public SiteMap(String url, String child) throws IOException, SQLException {
        this.url = url;
        this.child = child;

        Connection connect = Jsoup.connect(url + child)
                .userAgent(JSOUP_USER_AGENT)
                .referrer(JSOUP_REFERRER)
                .ignoreHttpErrors(true);

        Document document = connect.ignoreContentType(true).get();
        links = document.select("a[href^=/]");

            DBConnection.appendMultiInsert(
                    child,
                    connect.response().statusCode(),
                    document.html());
    }

    @SneakyThrows
    protected void compute() {

        for (Element link : links) {
            String ref = link.attr("href").toLowerCase();
            if (!ref.matches(".*\\.(png|jpe?g|pptx?|docx?|xlsx?|pdf|zip|js|nc|fig|m).*") &&
                    !ref.matches(".*\\?.*=.*") &&
                    linksSeen.add(ref)) {
                new SiteMap(url, ref).invoke();

                System.out.println(child + "\t" + ref);
            }
        }
        Thread.sleep((long) (500 + 4500 * Math.random()));
    }
}

