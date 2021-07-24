import configuration.constants.CatalogSite;
import service.IndexingSite;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        CatalogSite catalogSite = new CatalogSite();
        Scanner scanner = new Scanner(System.in);

        catalogSite.getNames().forEach(System.out::println);
        System.out.print("Введите наименование ресурса из перечисленных выше :");

        String line = scanner.nextLine().trim().toLowerCase();
        System.out.println(line + ":\t" + catalogSite.getUrl(line) + "\n");
        new IndexingSite(catalogSite.getUrl(line));

        scanner.close();
    }
}