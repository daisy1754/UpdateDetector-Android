package jp.gr.java_conf.daisy.update_detector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * {@link android.app.Service} to check if application update is available.
 */
public class CheckUpdateService extends IntentService {

    private static final String TAG = CheckUpdateService.class.getSimpleName();
    private static final String EXTRA_KEY_URL = "CheckUpdateService:url";
    private static final String UPDATE_DETECT_BROADCAST_ACTION_FORMAT =
            "jp.gr.java_conf.daisy.update_detector.%s.UPDATE";

    static Intent intentWithVersionFileUrl(Context context, String url) {
        return new Intent(context, CheckUpdateService.class)
                .putExtra(EXTRA_KEY_URL, url);
    }

    public CheckUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isConnectedToNetwork()) {
            Log.d(TAG, "Not connected to network. Abort update check.");
            return;
        }

        String url = intent.getStringExtra(EXTRA_KEY_URL);
        String latestVersion = new RemoteFileVersionFetcher().fetchVersion(url);
        if (latestVersion == null) {
            Log.e(TAG, "Failed to extract latest update info from" + url);
            return;
        }
        String currentVersion = getVersionName();
        @UpdateType int updateType = new VersionComparator().compare(latestVersion, currentVersion);

        Log.d(TAG,
                String.format("Check update result: %d, current: %s, latest: %s",
                        updateType, currentVersion, latestVersion));
        if (updateType != UpdateType.UPDATE_TYPE_NO_UPDATE) {
            Intent broadcastIntent = new Intent(
                    String.format(UPDATE_DETECT_BROADCAST_ACTION_FORMAT, getPackageName()));
            new DetectedUpdateInfo(currentVersion, updateType).writeBundleToIntent(broadcastIntent);
            sendBroadcast(broadcastIntent);
        }
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new IllegalStateException("Could not get package name: " + e);
        }
    }
}
