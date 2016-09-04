package com.jpmorgan.exercise.environment;

/**
 * Artifical clock that keeps track of an internal timer, used for testing of
 * the trading calculations.
 * 
 * @author Tony Cruickshank
 *
 */
public class ArtificialClock implements Clock
{
    private long currentTime;
    
    public ArtificialClock (final long initialTime)
    {
        currentTime = initialTime;
    }
    
    public ArtificialClock ()
    {
        this (0);
    }
    
    @Override
    public long getTime ()
    {
        return currentTime;
    }

    @Override
    public void tick (long seconds)
    {
        currentTime += seconds;
    }

}
