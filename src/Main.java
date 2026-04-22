import java.util.Random;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your genre: ");
        String genre = scanner.nextLine();
        genre = genre.replaceAll("\\s", "-");

        System.out.println("Your genre is " + ANSI_RED + genre + ANSI_RESET +
                ". Please wait for response to load(It can take a while).");

        String pageURL = "https://musicbrainz.org/ws/2/recording?query=tag:" + genre + "&limit=1&fmt=json";
        String pageJSON = APIFetcher.fetchJSON(pageURL);

        JsonObject countObj = JsonParser.parseString(pageJSON).getAsJsonObject();
        int pages = countObj.get("count").getAsInt();
        int maxPage = Math.min(pages, 100000);

        int randomPage = random.nextInt(0, maxPage);

        String url = "https://musicbrainz.org/ws/2/recording?query=tag:" + genre +
                "&limit=1&offset=" + randomPage + "&fmt=json";

        String data = APIFetcher.fetchJSON(url);

        JsonObject obj = JsonParser.parseString(data).getAsJsonObject();

        JsonObject song = obj.getAsJsonArray("recordings")
                .get(0)
                .getAsJsonObject();

        String title = song.get("title").getAsString();

        String artist = song.getAsJsonArray("artist-credit")
                .get(0)
                .getAsJsonObject()
                .get("name")
                .getAsString();

        System.out.println("Title: " + title);
        System.out.println("Artist: " + artist);
    }
}
