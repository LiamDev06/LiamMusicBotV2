package dev.liamhbest.musicbot.utils;

import dev.liamhbest.musicbot.LiamMusicBot;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OtherUtil {

    private final static String WINDOWS_INVALID_PATH = "c:\\windows\\system32\\";
    
    /**
     * gets a Path from a String
     * also fixes the windows tendency to try to start in system32
     * any time the bot tries to access this path, it will instead start in the location of the jar file
     * 
     * @param path the string path
     * @return the Path object
     */
    public static Path getPath(String path) {
        Path result = Paths.get(path);
        // special logic to prevent trying to access system32

        if (result.toAbsolutePath().toString().toLowerCase().startsWith(WINDOWS_INVALID_PATH)) {
            try {
                result = Paths.get(new File(LiamMusicBot.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath() + File.separator + path);

            } catch(URISyntaxException ignored) {}
        }
        return result;
    }
    
    /**
     * Loads a resource from the jar as a string
     * 
     * @param clazz class base object
     * @param name name of resource
     * @return string containing the contents of the resource
     */
    public static String loadResource(Object clazz, String name) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clazz.getClass().getResourceAsStream(name)))) {
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(line -> sb.append("\r\n").append(line));
            return sb.toString().trim();

        } catch (IOException ex) {
            return null;
        }
    }
    
    /**
     * Loads image data from a URL
     * 
     * @param url url of image
     * @return inputstream of url
     */
    public static InputStream imageFromUrl(String url) {
        if (url == null) return null;

        try {
            URL u = new URL(url);
            URLConnection urlConnection = u.openConnection();
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
            return urlConnection.getInputStream();

        } catch(IOException | IllegalArgumentException ignore) {}
        return null;
    }
    
    /**
     * Parses an activity from a string
     * 
     * @param game the game, including the action such as 'playing' or 'watching'
     * @return the parsed activity
     */
    public static Activity parseGame(String game) {
        if (game == null || game.trim().isEmpty() || game.trim().equalsIgnoreCase("default")) return null;
        String lower = game.toLowerCase();

        if (lower.startsWith("playing")) return Activity.playing(makeNonEmpty(game.substring(7).trim()));

        if (lower.startsWith("listening to")) return Activity.listening(makeNonEmpty(game.substring(12).trim()));

        if (lower.startsWith("listening")) return Activity.listening(makeNonEmpty(game.substring(9).trim()));

        if (lower.startsWith("watching")) return Activity.watching(makeNonEmpty(game.substring(8).trim()));

        if (lower.startsWith("streaming")) {
            String[] parts = game.substring(9).trim().split("\\s+", 2);
            if (parts.length == 2) {
                return Activity.streaming(makeNonEmpty(parts[1]), "https://twitch.tv/"+parts[0]);
            }
        }
        return Activity.playing(game);
    }
   
    public static String makeNonEmpty(String str) {
        return str == null || str.isEmpty() ? "\u200B" : str;
    }
    
    public static OnlineStatus parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) return OnlineStatus.ONLINE;

        OnlineStatus st = OnlineStatus.fromKey(status);
        return st == null ? OnlineStatus.ONLINE : st;
    }
    
    public static String getCurrentVersion() {
        if (LiamMusicBot.class.getPackage() != null && LiamMusicBot.class.getPackage().getImplementationVersion() != null) {
            return LiamMusicBot.class.getPackage().getImplementationVersion();

        } else {
            return "UNKNOWN";
        }
    }
}
