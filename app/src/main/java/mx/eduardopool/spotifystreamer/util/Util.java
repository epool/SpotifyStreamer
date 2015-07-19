package mx.eduardopool.spotifystreamer.util;

import java.util.concurrent.TimeUnit;

/**
 * Class for general utilities.
 * Created by EduardoPool on 7/18/15.
 */
public class Util {

    public static String millisecondsToMinutesAndSecondsString(long milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        return String.format("%d:%02d", minutes, seconds);
    }

}
