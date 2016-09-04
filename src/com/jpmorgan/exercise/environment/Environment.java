package com.jpmorgan.exercise.environment;

/**
 * Interface (in the non-Java sense) to the outside world that the market
 * functions in. In this simple example, the only interface of interest
 * is the clock, which is used in trading operations.
 * 
 * Abstracting the environment is used to implement the Dependency Injection
 * pattern on the market. I.e. the information that the market depends on
 * (in this case the time of trade and window for calculate the VWSP/GBCE) is
 * injected into the class.
 * 
 * The environment is set up as an immutable singleton.
 * 
 * @author Tony Cruickshank
 *
 */
public class Environment
{
    private static final Environment environment = new Environment ();

    private Clock                    clock       = new ArtificialClock ();

    private Environment ()
    {

    }

    public static Environment getEnvironment ()
    {
        return Environment.environment;
    }

    public long getTime ()
    {
        return clock.getTime ();
    }

    public void tick (final long seconds)
    {
        clock.tick (seconds);
    }
}
