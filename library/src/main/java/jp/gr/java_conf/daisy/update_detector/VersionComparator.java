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

        int[] newVersions = parseVersion(newVersion);
        int[] oldVersions = parseVersion(oldVersion);

        if (newVersions[0] > oldVersions[0]) {
            return UpdateType.UPDATE_TYPE_MAJOR;
        } else if (newVersions[1] > oldVersions[1]) {
            return UpdateType.UPDATE_TYPE_MINOR;
        } else if (newVersions[2] > oldVersions[2]){
            return UpdateType.UPDATE_TYPE_PATCH;
        }
        return UpdateType.UPDATE_TYPE_NO_UPDATE;
    }

    /**
     * Parses version string into array. e.g., "1.3.7" -> [1,3,7].
     */
    private int[] parseVersion(String version) {
        String[] split = version.split("\\.");
        return new int[] {
                parseIntOrZero(split[0]),
                split.length > 1 ? parseIntOrZero(split[1]) : 0,
                split.length > 2 ? parseIntOrZero(split[2]) : 0
        };
    }

    /**
     * Parses string and parse as integer or default to zero. e.g., "1" -> 1, "2-alpha" -> 2,
     * "unknown" -> 0
     */
    private static int parseIntOrZero(String string) {
        Matcher match = NUM_PREFIX_PATTERN.matcher(string);
        if (match.find()) {
            return Integer.parseInt(match.group());
        }
        return 0;
    }
}
