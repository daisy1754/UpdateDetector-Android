package jp.gr.java_conf.daisy.update_detector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to compare two version string. This class assumes version string is formatted like
 * {major_version}.{minor_version}.{patch_version}. Example of parsable version includes
 * "3.42.1", "1.0.3-beta", or "0.5".
 */
public class VersionComparator {

    private static final Pattern NUM_PREFIX_PATTERN = Pattern.compile("\\A[0-9]+");

    /**
     * Compare two version and returns one of {@link UpdateType#UPDATE_TYPE_MAJOR},
     * {@link UpdateType#UPDATE_TYPE_MINOR}, {@link UpdateType#UPDATE_TYPE_PATCH},
     * {@link UpdateType#UPDATE_TYPE_NO_UPDATE}.
     */
    int compare(String newVersion, String oldVersion) {
        if (newVersion.equals(oldVersion)) {
            return UpdateType.UPDATE_TYPE_NO_UPDATE;
        }

        String[] newInfo = newVersion.split("\\.");
        String[] oldInfo = oldVersion.split("\\.");

        if (parseIntOrZero(newInfo[0]) > parseIntOrZero(oldInfo[0])) {
            return UpdateType.UPDATE_TYPE_MAJOR;
        }
        if (newInfo.length > 1
                && oldInfo.length > 1
                && parseIntOrZero(newInfo[1]) > parseIntOrZero(oldInfo[1])) {
            return UpdateType.UPDATE_TYPE_MINOR;
        }
        if (newInfo.length > 2
                && oldInfo.length > 2
                && parseIntOrZero(newInfo[2]) > parseIntOrZero(oldInfo[2])) {
            return UpdateType.UPDATE_TYPE_PATCH;
        }
        return UpdateType.UPDATE_TYPE_NO_UPDATE;
    }

    private static int parseIntOrZero(String string) {
        Matcher match = NUM_PREFIX_PATTERN.matcher(string);
        if (match.find()) {
            return Integer.parseInt(match.group());
        }
        return 0;
    }
}
