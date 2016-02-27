package jp.gr.java_conf.daisy.update_detector;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Set of constants referring type of update (major, minor, patch or no udpate).
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({UpdateType.UPDATE_TYPE_NO_UPDATE, UpdateType.UPDATE_TYPE_MAJOR,
        UpdateType.UPDATE_TYPE_MINOR, UpdateType.UPDATE_TYPE_PATCH})
public @interface UpdateType {
    int UPDATE_TYPE_NO_UPDATE = 0;
    int UPDATE_TYPE_PATCH = 1;
    int UPDATE_TYPE_MINOR = 2;
    int UPDATE_TYPE_MAJOR = 3;
}