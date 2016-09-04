package com.jpmorgan.exercise.clock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jpmorgan.exercise.environment.ArtificialClock;
import com.jpmorgan.exercise.environment.Clock;

public class ArtificialClockTest
{

    @Test
    public void testArtificialClockInitZero ()
    {
        Clock clock = new ArtificialClock ();
        
        assertEquals (0, clock.getTime ());
    }

    @Test
    public void testArtificialClockTick1 ()
    {
        Clock clock = new ArtificialClock ();
        
        clock.tick (1);
        
        assertEquals (1,  clock.getTime ());
    }
}
