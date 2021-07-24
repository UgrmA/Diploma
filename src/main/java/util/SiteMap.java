package util;

import configuration.constants.AppPrm;
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
    private final AppPrm appPrm;

    public SiteMap(String url, String child, Boolean addLink)
            throws IOException, SQLException {
        this.url = url;
        this.child = child;
        if (addLink) {
            linksSeen.add("");
        }
        appPrm = new AppPrm();

        Connection connect = Jsoup.connect(url +
                child.substring(child.length() == 0 ? 0 : 1))
                .userAgent(appPrm.getUserAgent())
                .referrer(appPrm.getReferrer())
                .ignoreHttpErrors(true);

        Document document = connect.get();
        links = document.select("a[href]");

        ProjectRepository.appendMultiInsert(
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

            if (!ref.matches(appPrm.getMatchContent()) &&
                    !ref.matches(appPrm.getMatchParameters()) &&
                    linksSeen.add(ref)) {
                new SiteMap(url, ref, false).invoke();

                System.out.println(child + "\t" + ref);
            }
        }
        Thread.sleep((long) (500 + 4500 * Math.random()));
    }
}

