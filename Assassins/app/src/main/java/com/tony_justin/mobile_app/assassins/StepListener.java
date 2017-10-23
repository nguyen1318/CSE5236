package com.tony_justin.mobile_app.assassins;

/**
 * Created by JMontage on 10/18/17.
 */

/**
 * Listens for alerts about steps being detected.
 */
public interface StepListener {


     /* Called when a step has been detected.  Given the time in nanoseconds at
     * which the step was detected. */

    public void step(long timeNs);

}
