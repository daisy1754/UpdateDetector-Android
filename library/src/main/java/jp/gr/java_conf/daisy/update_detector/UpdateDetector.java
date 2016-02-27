package jp.gr.java_conf.daisy.update_detector;

import android.content.Context;

/**
 * Fetches the latest version information from given url, compares it with current app's version,
 * and notifies if any update is detected.
 */
public class UpdateDetector {

    private final String versionFileUrl;

    public static UpdateDetector withRemoteVersionFileUrl(String url) {
        return new UpdateDetector(url);
    }

    public UpdateDetector(String versionFileUrl) {
        this.versionFileUrl = versionFileUrl;
    }

    public void start(Context context) {
        context.startService(CheckUpdateService.intentWithVersionFileUrl(context, versionFileUrl));
    }
}
