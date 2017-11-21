package androidTest;

import org.junit.Test;
import android.test.ActivityInstrumentationTestCase2;
import com.tony_justin.mobile_app.assassins.MapsActivity;

/**
 * Created by JMontage on 11/20/17.
 */

public class mapsActivityTest extends ActivityInstrumentationTestCase2<MapsActivity> {

    private MapsActivity mMapsActivity;


    public mapsActivityTest() {
        super(MapsActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        //set up private variables

        mMapsActivity = getActivity();


    }

    @Test
    public void testPreconditions() {
        assertNotNull(mMapsActivity);
    }

    @Test
    public void testUserData() {

        assertFalse(mMapsActivity.legit == false);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
