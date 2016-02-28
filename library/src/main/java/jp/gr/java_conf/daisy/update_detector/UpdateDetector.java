package jp.gr.java_conf.daisy.update_detector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Fetches the latest version information from given url, compares it with current app's version,
 * and notifies if any update is detected.
 */
public class UpdateDetector {

    private final String versionFileUrl;
    private DetectUpdateBroadcastReceiver receiver;

    public interface UpdateDetectedCallback {
        void onUpdateFound(@UpdateType int updateType, String latestVersion);
    }

    public static UpdateDetector withRemoteVersionFileUrl(String url) {
        return new UpdateDetector(url);
    }

    public UpdateDetector(String versionFileUrl) {
        this.versionFileUrl = versionFileUrl;
    }

    public UpdateDetector register(Context context, UpdateDetectedCallback callback) {
        context.startService(CheckUpdateService.intentWithVersionFileUrl(context, versionFileUrl));
        IntentFilter filter = new IntentFilter();
        filter.addAction(
                "jp.gr.java_conf.daisy.update_detector." + context.getPackageName() + ".UPDATE");
        receiver = new DetectUpdateBroadcastReceiver(callback);
        context.registerReceiver(receiver, filter);
        return this;
    }

    public void unregister(Context context) {
        context.unregisterReceiver(receiver);
    }

    private static class DetectUpdateBroadcastReceiver extends BroadcastReceiver {

        private final UpdateDetectedCallback callback;

        DetectUpdateBroadcastReceiver(UpdateDetectedCallback callback) {
            super();
            this.callback = callback;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            callback.onUpdateFound(
                    CheckUpdateService.getUpdateType(intent),
                    CheckUpdateService.getLatestVersion(intent));
        }
    }
}
