package com.jpmorgan.exercise.environment;

/**
 * Management of time within the market is managed by a clock.
 * 
 * @author Tony Cruickshank
 *
 */

public interface Clock
{
    /**
     * @return the current time, in seconds.
     */
    public long getTime ();

    /**
     * Advance the clock by the specified number of seconds.
     * 
     * @param seconds the number of seconds by which to adjust the clock.
     */
    public void tick (long seconds);
}
