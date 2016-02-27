package jp.gr.java_conf.daisy.update_detector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static jp.gr.java_conf.daisy.update_detector.UpdateType.UPDATE_TYPE_MAJOR;
import static jp.gr.java_conf.daisy.update_detector.UpdateType.UPDATE_TYPE_MINOR;
import static jp.gr.java_conf.daisy.update_detector.UpdateType.UPDATE_TYPE_NO_UPDATE;
import static jp.gr.java_conf.daisy.update_detector.UpdateType.UPDATE_TYPE_PATCH;

@RunWith(JUnit4.class)
public class VersionComparatorTest {

    private VersionComparator versionComparator;

    @Before
    public void setup() {
        versionComparator = new VersionComparator();
    }

    @Test
    public void testNoUpdate() {
        assertVersionCompareResult("1.0.0", "1.0.0", UPDATE_TYPE_NO_UPDATE);
        assertVersionCompareResult("1.1", "1.1", UPDATE_TYPE_NO_UPDATE);
        assertVersionCompareResult("1", "1", UPDATE_TYPE_NO_UPDATE);
        assertVersionCompareResult("1.1.alpha", "1.1.alpha", UPDATE_TYPE_NO_UPDATE);
        assertVersionCompareResult("1.2-beta", "1.2-beta", UPDATE_TYPE_NO_UPDATE);
        assertVersionCompareResult("1.1.1-beta", "1.1.1-alpha", UPDATE_TYPE_NO_UPDATE);
        assertVersionCompareResult("1.1.1.2", "1.1.1.1", UPDATE_TYPE_NO_UPDATE);
    }

    @Test
    public void testPatchUpdate() {
        assertVersionCompareResult("1.0.1", "1.0.0", UPDATE_TYPE_PATCH);
        assertVersionCompareResult("1.1.2alpha", "1.1.1alpha", UPDATE_TYPE_PATCH);
        assertVersionCompareResult("1.2.9-beta", "1.2.3-beta", UPDATE_TYPE_PATCH);
    }

    @Test
    public void testMinorUpdate() {
        assertVersionCompareResult("1.1.0", "1.0.0", UPDATE_TYPE_MINOR);
        assertVersionCompareResult("1.1.9", "1.0.0", UPDATE_TYPE_MINOR);
        assertVersionCompareResult("0.5", "0.3", UPDATE_TYPE_MINOR);
        assertVersionCompareResult("1.2", "1.1", UPDATE_TYPE_MINOR);
        assertVersionCompareResult("1.2beta", "1.1alpha", UPDATE_TYPE_MINOR);
    }

    @Test
    public void testMajorUpdate() {
        assertVersionCompareResult("2.1.1", "1.1.1", UPDATE_TYPE_MAJOR);
        assertVersionCompareResult("2.2.1", "1.1.1", UPDATE_TYPE_MAJOR);
        assertVersionCompareResult("2.2.2", "1.1.1", UPDATE_TYPE_MAJOR);
        assertVersionCompareResult("1.0", "0.5", UPDATE_TYPE_MAJOR);
        assertVersionCompareResult("2.0", "1-alpha", UPDATE_TYPE_MAJOR);
    }

    private void assertVersionCompareResult(
            String newVersion, String oldVersion, @UpdateType int updateType) {
        assertThat(versionComparator.compare(newVersion, oldVersion)).isEqualTo(updateType);
    }
}
