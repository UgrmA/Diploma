package configuration.constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class CatalogSite {
    private final String catalogPath = Objects.requireNonNull(Thread.currentThread()
            .getContextClassLoader().getResource("catalog.properties")).getPath();
    private final Properties catalogSite = new Properties();
    private final Set<String> names;

    public CatalogSite() throws IOException {
        catalogSite.load(new FileInputStream(catalogPath));
        names = catalogSite.stringPropertyNames();
    }

    public Set<String> getNames() {
        return names;
    }

    public String getUrl(String key) {
        return catalogSite.getProperty(key);
    }

    public void setUrl(String key, String url) {
        catalogSite.put(key, url);
    }

    public void store() throws IOException {
        catalogSite.store(new FileOutputStream(catalogPath), null);
    }
}
