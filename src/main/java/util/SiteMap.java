package util;

import configuration.constants.App;
import data.ProjectRepository;
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

public class SiteMap extends RecursiveAction {
    private static final Set<String> linksSeen = ConcurrentHashMap.newKeySet();
    private final String url;
    private final String child;
    private final List<Element> links;
    ProjectRepository repository = new ProjectRepository();
    private final App app;

    public SiteMap(String url, String child)
            throws IOException, SQLException {
        this.url = url;
        this.child = child;
        app = new App();

        Connection connect = Jsoup.connect(url +
                child.substring(child.length() == 0 ? 0 : 1))
                .userAgent(app.getUserAgent())
                .referrer(app.getReferrer())
                .ignoreHttpErrors(true);

        Document document = connect.ignoreContentType(true).get();
        links = document.select("a[href]");

        repository.appendMultiInsert(
                child,
                connect.response().statusCode(),
                document.html());
    }

    @SneakyThrows
    protected void compute() {

        for (Element link : links) {
            String ref = link.attr("href")
                    .toLowerCase()
                    .replace(url, "/")
                    .trim();

            int index = ref.indexOf("#");
            switch (index) {
                case -1:
                    break;
                case 0:
                    continue;
                default:
                    ref = ref.substring(0, --index);
            }

            if (!ref.matches(app.getMatchContent()) &&
                    !ref.matches(app.getMatchParameters()) &&
                    linksSeen.add(ref)) {
                new SiteMap(url, ref).invoke();
            }
        }
        Thread.sleep((long) (500 + 4500 * Math.random()));
    }
}

