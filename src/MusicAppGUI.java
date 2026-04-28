import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class MusicAppGUI {

    private static final Preferences preferences =
            Preferences.userNodeForPackage(MusicAppGUI.class);

    private static final String GENERATION_MODE_KEY = "generationMode";
    private static final String MODE_GENRE = "genre";
    private static final String MODE_ARTIST = "artist";

    public static void loadSong(JTextField searchField, JLabel statusLabel,
                                JTextArea songTitleLabel, JTextArea artistLabel,
                                JLabel updateLabel) {
        String genre = searchField.getText();
        statusLabel.setText("Status: Finding a song by genre...");

        try {
            String[] result = SongGenerator.getSong(genre);
            songTitleLabel.setText("Title: " + result[0]);
            artistLabel.setText("Artist: " + result[1]);

            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

            updateLabel.setText("Last updated: " + now.format(formatter));
            statusLabel.setText("Status: Done");
        } catch (Exception ex) {
            songTitleLabel.setText("Title: Error");
            artistLabel.setText("Artist: Error");
            updateLabel.setText("Last updated: Error");
            statusLabel.setText("Status: Something went wrong");
        }
    }

    public static void loadSongByArtist(JTextField searchField, JLabel statusLabel,
                                        JTextArea songTitleLabel, JTextArea artistLabel,
                                        JLabel updateLabel) {
        String artistName = searchField.getText();
        statusLabel.setText("Status: Finding a song by artist...");

        try {
            String[] result = SongGenerator.getSongByArtist(artistName);
            songTitleLabel.setText("Title: " + result[0]);
            artistLabel.setText("Artist: " + result[1]);

            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

            updateLabel.setText("Last updated: " + now.format(formatter));
            statusLabel.setText("Status: Done");
        } catch (Exception ex) {
            songTitleLabel.setText("Title: Error");
            artistLabel.setText("Artist: Error");
            updateLabel.setText("Last updated: Error");
            statusLabel.setText("Status: Something went wrong");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Music Generator App");
        frame.setSize(450, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Music Generator App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JRadioButton genreModeButton = new JRadioButton("Generate by Genre");
        JRadioButton artistModeButton = new JRadioButton("Generate by Artist");

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(genreModeButton);
        modeGroup.add(artistModeButton);

        JTextField searchField = new JTextField();

        String savedMode = preferences.get(GENERATION_MODE_KEY, MODE_GENRE);

        if (savedMode.equals(MODE_ARTIST)) {
            artistModeButton.setSelected(true);
            searchField.setToolTipText("Enter an artist name");
        } else {
            genreModeButton.setSelected(true);
            searchField.setToolTipText("Enter a genre");
        }

        JLabel searchLabel = new JLabel();

        if (artistModeButton.isSelected()) {
            searchLabel.setText("Enter an artist name:");
        } else {
            searchLabel.setText("Enter a genre:");
        }

        genreModeButton.addActionListener(e -> {
            preferences.put(GENERATION_MODE_KEY, MODE_GENRE);
            searchLabel.setText("Enter a genre:");
            searchField.setToolTipText("Enter a genre");
            searchField.setText("");
        });

        artistModeButton.addActionListener(e -> {
            preferences.put(GENERATION_MODE_KEY, MODE_ARTIST);
            searchLabel.setText("Enter an artist name:");
            searchField.setToolTipText("Enter an artist name");
            searchField.setText("");
        });

        JButton button = new JButton("Generate Song");

        JLabel statusLabel = new JLabel("Status: Waiting...");

        JTextArea songTitleLabel = new JTextArea("Title: ");
        songTitleLabel.setLineWrap(true);
        songTitleLabel.setWrapStyleWord(true);
        songTitleLabel.setEditable(false);
        songTitleLabel.setBackground(panel.getBackground());

        JTextArea artistLabel = new JTextArea("Artist: ");
        artistLabel.setLineWrap(true);
        artistLabel.setWrapStyleWord(true);
        artistLabel.setEditable(false);
        artistLabel.setBackground(panel.getBackground());

        JLabel updateLabel = new JLabel("Last updated: ");

        button.addActionListener(e -> {
            if (genreModeButton.isSelected()) {
                loadSong(searchField, statusLabel, songTitleLabel, artistLabel, updateLabel);
            } else {
                loadSongByArtist(searchField, statusLabel, songTitleLabel, artistLabel, updateLabel);
            }
        });

        panel.add(titleLabel);
        panel.add(genreModeButton);
        panel.add(artistModeButton);
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(button);
        panel.add(statusLabel);
        panel.add(songTitleLabel);
        panel.add(artistLabel);
        panel.add(updateLabel);

        frame.add(panel);
        frame.setVisible(true);
    }
}