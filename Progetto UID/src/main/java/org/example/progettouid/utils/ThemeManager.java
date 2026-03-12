package org.example.progettouid.utils;

import javafx.scene.Scene;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Properties;

public class ThemeManager {

    private static final Path PREFS_PATH =
            Paths.get(System.getProperty("user.home"), ".progettouid", "preferences.properties");

    private static final String LIGHT_CSS = "/org/example/css/light-theme.css";
    private static final String DARK_CSS  = "/org/example/css/dark-theme.css";

    public static void applySavedTheme(Scene scene) {
        String theme = loadTheme();
        applyTheme(scene, theme);
    }

    public static void applyTheme(Scene scene, String theme) {
        if (scene == null) return;

        scene.getStylesheets().removeIf(css ->
                css.contains("light-theme.css") || css.contains("dark-theme.css")
        );

        String css = "Scuro".equalsIgnoreCase(theme) ? DARK_CSS : LIGHT_CSS;

        var url = ThemeManager.class.getResource(css);
        if (url != null) {
            scene.getStylesheets().add(url.toExternalForm());
        }
    }

    private static String loadTheme() {
        if (!Files.exists(PREFS_PATH)) return "Chiaro";

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(PREFS_PATH)) {
            props.load(in);
            return props.getProperty("tema", "Chiaro");
        } catch (Exception e) {
            return "Chiaro";
        }
    }
}