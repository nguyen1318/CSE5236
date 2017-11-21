package androidTest;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.tony_justin.mobile_app.assassins.CurrentGameActivity;



/**
 * Created by JMontage on 11/20/17.
 */


public class currentGameActivityTest extends ActivityInstrumentationTestCase2<CurrentGameActivity> {

    private CurrentGameActivity mCurrentGameActivity;
    private boolean t = true;


    public currentGameActivityTest() {
        super(CurrentGameActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        //set up private variables

        mCurrentGameActivity = getActivity();


    }

    @Test
    public void testPreconditions() {
        assertNotNull(mCurrentGameActivity);
        //assertTrue("this is true!", true);
    }


    public void testGame() {
        assertEquals(mCurrentGameActivity.getPlayCount(), 1);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
