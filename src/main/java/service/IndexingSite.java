package service;

import data.ProjectRepository;
import util.SiteMap;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;

public class IndexingSite {

    public IndexingSite(String url) throws SQLException, IOException {
        ProjectRepository repository = new ProjectRepository();
        repository.createTable();
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new SiteMap(url, "", true));

        repository.execMultiInsert();
        repository.indexPath();

    }
}
