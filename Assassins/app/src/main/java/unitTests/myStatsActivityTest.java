package unitTests;
import android.test.ActivityInstrumentationTestCase2;
import com.tony_justin.mobile_app.assassins.MyStatsActivity;

import org.junit.Test;

/**
 * Created by JMontage on 11/20/17.
 */

public class myStatsActivityTest extends ActivityInstrumentationTestCase2<MyStatsActivity> {
    public myStatsActivityTest() {
        super(MyStatsActivity.class);
    }

    private MyStatsActivity mMyStatsActivity;

    protected void setUp() throws Exception {
        super.setUp();
        //set up private variables

        mMyStatsActivity = getActivity();

        getInstrumentation().waitForIdleSync();

    }

    @Test
    public void testPreconditions() {
        assertNotNull(mMyStatsActivity);
    }

    @Test
    public void testPedometer() { assertEquals(mMyStatsActivity.numberOfSteps(), 0); }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

