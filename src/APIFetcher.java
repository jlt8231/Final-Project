import java.net.URL;
import java.util.Scanner;

public class APIFetcher {

    public static String fetchJSON(String url) {
        URL url1;
        Scanner scan;

        try {
            url1 = new URL(url);
            scan = new Scanner(url1.openStream());
        } catch (Exception e) {
            return "{ \"title\": \"ERROR\" }";
        }

        StringBuilder data = new StringBuilder();

        while (scan.hasNextLine()) {
            data.append(scan.nextLine());
        }
        scan.close();
        return data.toString();
    }
}
