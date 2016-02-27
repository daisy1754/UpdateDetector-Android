package jp.gr.java_conf.daisy.update_detector;

import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Fetches the latest version from remote url.
 */
public class RemoteFileVersionFetcher {

    private static final String TAG = RemoteFileVersionFetcher.class.getSimpleName();
    private static final int CONNECT_TIMEOUT_MILLIS = 30 * 1000;
    private static final int READ_TIMEOUT_MILLIS = 30 * 1000;
    private static final String KEY_VERSION = "version";

    /**
     * Fetch the latest version from remote json file containing version information.
     *
     * @return version string, or <code>null</code> if something wrong happens.
     */
    @Nullable
    public String fetchVersion(String versionFileUrl) {
        JsonReader reader = null;
        String latestVersion = null;
        try {
            HttpURLConnection connection = connectToUrl(versionFileUrl);

            int responseCode = connection.getResponseCode();
            if (responseCode / 100 != 2) {
                Log.w(TAG, "Has non successful response code :" + responseCode);
                return null;
            }

            reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals(KEY_VERSION)) {
                    latestVersion = reader.nextString();
                } else {
                    Log.d(TAG, "unknown key in version file: " + name);
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            Log.e(TAG, "exception during fetching version info", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Cannot do anything
                }
            }
            return latestVersion;
        }
    }

    private HttpURLConnection connectToUrl(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
        connection.setReadTimeout(READ_TIMEOUT_MILLIS);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Pragma", "no-cache");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Expires", "-1");
        connection.connect();
        return connection;
    }
}
