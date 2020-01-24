package de.upb.codingpirates.battleships.desktop.util;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class DesktopMarker {
    private DesktopMarker() {}

    public static final Marker SETTINGS = MarkerManager.getMarker("settings");
}
