package jp.gr.java_conf.daisy.update_detector;

import android.content.Intent;

/**
 * Data class containing update information.
 */
public class DetectedUpdateInfo {

    private static final String EXTRA_KEY_VERSION = "CheckUpdateResult:version";
    private static final String EXTRA_KEY_UPDATE_TYPE = "CheckUpdateResult:updateType";

    private final String latestVersion;
    @UpdateType private final int updateType;

    void writeBundleToIntent(Intent intent) {
        intent.putExtra(EXTRA_KEY_VERSION, latestVersion);
        intent.putExtra(EXTRA_KEY_UPDATE_TYPE, updateType);
    }

    @SuppressWarnings("ResourceType")
    public static DetectedUpdateInfo fromIntent(Intent intent) {
        return new DetectedUpdateInfo(
                intent.getStringExtra(EXTRA_KEY_VERSION),
                intent.getIntExtra(EXTRA_KEY_UPDATE_TYPE, UpdateType.UPDATE_TYPE_NO_UPDATE));
    }

    DetectedUpdateInfo(String latestVersion, @UpdateType int updateType) {
        this.latestVersion = latestVersion;
        this.updateType = updateType;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    @UpdateType
    public int getUpdateType() {
        return updateType;
    }
}
