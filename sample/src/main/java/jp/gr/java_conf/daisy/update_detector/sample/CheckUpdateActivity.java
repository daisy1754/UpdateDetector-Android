package jp.gr.java_conf.daisy.update_detector.sample;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import jp.gr.java_conf.daisy.update_detector.UpdateDetector;
import jp.gr.java_conf.daisy.update_detector.UpdateType;

public class CheckUpdateActivity extends AppCompatActivity {

    private UpdateDetector detector;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_update_activity);
        statusText = (TextView) findViewById(R.id.status_text);

        detector = UpdateDetector
                .withRemoteVersionFileUrl("https://gist.githubusercontent.com/daisy1754/ab854927d509613a585d/raw/cd0fc73b9f9a0e0d7b3ce9b3dd3a9dfa424b9944/version.json")
                .register(this, new UpdateDetector.UpdateDetectedCallback() {
                    @Override
                    public void onUpdateFound(@UpdateType int updateType, String latestVersion) {
                        if (statusText != null) {
                            statusText.setText(String.format(
                                    "Update detected. current version: %s, new version: %s, update type: %s",
                                    getVersionName(), latestVersion, updateTypeToString(updateType)));
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        detector.unregister(this);
        super.onDestroy();
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

    private static String updateTypeToString(@UpdateType int type) {
        switch (type) {
            case UpdateType.UPDATE_TYPE_NO_UPDATE:
                return "NO_UPDATE";
            case UpdateType.UPDATE_TYPE_PATCH:
                return "PATCH";
            case UpdateType.UPDATE_TYPE_MINOR:
                return "MINOR";
            case UpdateType.UPDATE_TYPE_MAJOR:
                return "MAJOR";
        }
        return "UNKNOWN";
    }
}
