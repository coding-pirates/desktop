package de.upb.codingpirates.battleships.desktop.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;

public class Settings {
    private static final Logger LOGGER = LogManager.getLogger();

    private static Settings settings = new Settings();

    private Locale locale;
    private Integer soundEffectVolume;
    private Integer musicVolume;
    private Boolean musicMute;
    private Boolean soundMute;

    public Settings() {
        this.locale = Locale.US;
        this.soundEffectVolume = 100;
        this.musicVolume = 100;
        this.musicMute = false;
    }

    public static Locale getLocale() {
        return settings.locale;
    }

    public static Integer getSoundEffectVolume() {
        return settings.soundEffectVolume;
    }

    public static Integer getMusicVolume() {
        return settings.musicVolume;
    }

    public static Boolean getMusicMute() {
        return settings.musicMute;
    }

    public static Boolean getSoundMute() {
        return settings.soundMute;
    }

    public static void setLocale(Locale locale) {
        settings.locale = locale;
    }

    public static void setMusicVolume(Integer musicVolume) {
        settings.musicVolume = musicVolume;
    }

    public static void setSoundEffectVolume(Integer soundEffectVolume) {
        settings.soundEffectVolume = soundEffectVolume;
    }

    public static void setMusicMute(Boolean musicMute) {
        settings.musicMute = musicMute;
    }

    public static void setSoundMute(Boolean soundMute) {
        settings.soundMute = soundMute;
    }

    public static void init(){
        load();
    }

    public static void save() {
        LOGGER.debug(DesktopMarker.SETTINGS,"Save settings");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Files.write(new File("settings.json").toPath(), gson.toJson(settings).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.warn(DesktopMarker.SETTINGS, "Could not save settings");
        }
    }

    private static void load()  {
        LOGGER.debug(DesktopMarker.SETTINGS,"Load settings");
        Gson gson = new Gson();
        try {
            settings = gson.fromJson(new String(Files.readAllBytes(new File("settings.json").toPath()), StandardCharsets.UTF_8), Settings.class);
        } catch (IOException e) {
            LOGGER.warn(DesktopMarker.SETTINGS, "Could not load settings. Settings resetting");
        }
        if(settings == null){
            settings = new Settings();
        }
        save();
    }

}
