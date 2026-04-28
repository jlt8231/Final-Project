import java.net.URL;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SongGenerator {

    public static String fetchJSON(String url) {
        URL url1;
        Scanner scan;

        try {
            url1 = new URL(url);
            scan = new Scanner(url1.openStream());
        } catch (Exception e) {
            return "{ \"count\": 0, \"recordings\": [] }";
        }

        StringBuilder data = new StringBuilder();

        while (scan.hasNextLine()) {
            data.append(scan.nextLine());
        }

        scan.close();
        return data.toString();
    }

    public static String getArtistList(JsonObject song) {
        if (!song.has("artist-credit") || song.getAsJsonArray("artist-credit").size() == 0) {
            return "Main Artist: No artist found\nFeatured Artists: None";
        }

        JsonArray artistCredit = song.getAsJsonArray("artist-credit");

        String mainArtist = artistCredit
                .get(0)
                .getAsJsonObject()
                .get("name")
                .getAsString();

        StringBuilder featuredArtists = new StringBuilder();

        for (int i = 1; i < artistCredit.size(); i++) {
            String featuredArtist = artistCredit
                    .get(i)
                    .getAsJsonObject()
                    .get("name")
                    .getAsString();

            if (!featuredArtists.isEmpty()) {
                featuredArtists.append(", ");
            }

            featuredArtists.append(featuredArtist);
        }

        if (featuredArtists.isEmpty()) {
            featuredArtists.append("None");
        }

        return mainArtist + "\nFeatured Artists: " + featuredArtists;
    }

    public static String[] getSong(String genre) {
        Random random = new Random();

        genre = genre.replaceAll("\\s", "-");
        if (genre.isEmpty()) {
            return new String[]{"Please enter an genre", "No genre found"};
        }

        String pageURL = "https://musicbrainz.org/ws/2/recording?query=tag:" + genre + "&limit=1&fmt=json";
        String pageJSON = fetchJSON(pageURL);

        JsonObject countObj = JsonParser.parseString(pageJSON).getAsJsonObject();
        int pages = countObj.get("count").getAsInt();

        if (pages == 0) {
            return new String[]{"No song found", "No artist found"};
        }

        int max = Math.min(pages, 10000);
        int randomPage = random.nextInt(max);

        String url = "https://musicbrainz.org/ws/2/recording?query=tag:" + genre + "&limit=1&offset=" + randomPage + "&fmt=json";
        String data = fetchJSON(url);

        JsonObject obj = JsonParser.parseString(data).getAsJsonObject();

        if (!obj.has("recordings") || obj.getAsJsonArray("recordings").size() == 0) {
            return new String[]{"No song found", "No artist found"};
        }

        JsonObject song = obj.getAsJsonArray("recordings")
                .get(0)
                .getAsJsonObject();

        String title = song.get("title").getAsString();

        String artist = getArtistList(song);

        return new String[]{title, artist};
    }

    public static String[] getSongByArtist(String artistName) {
        Random random = new Random();

        artistName = artistName.trim();

        if (artistName.isEmpty()) {
            return new String[]{"Please enter an artist name", "No artist found"};
        }

        String pageURL = "https://musicbrainz.org/ws/2/recording?query=artist:" + artistName + "&limit=1&fmt=json";
        String pageJSON = fetchJSON(pageURL);

        JsonObject countObj = JsonParser.parseString(pageJSON).getAsJsonObject();
        int pages = countObj.get("count").getAsInt();

        if (pages == 0) {
            return new String[]{"No song found", "No artist found"};
        }

        int max = Math.min(pages, 10000);
        int randomPage = random.nextInt(max);

        String url = "https://musicbrainz.org/ws/2/recording?query=artist:" + artistName + "&limit=1&offset=" + randomPage + "&fmt=json";
        String data = fetchJSON(url);

        JsonObject obj = JsonParser.parseString(data).getAsJsonObject();

        if (!obj.has("recordings") || obj.getAsJsonArray("recordings").size() == 0) {
            return new String[]{"No song found", "No artist found"};
        }

        JsonObject song = obj.getAsJsonArray("recordings")
                .get(0)
                .getAsJsonObject();

        String title = song.get("title").getAsString();

        String artist = getArtistList(song);

        return new String[]{title, artist};
    }
}