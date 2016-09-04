package com.jpmorgan.exercise.market;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jpmorgan.exercise.environment.Environment;
import com.jpmorgan.exercise.exception.InvalidParameterException;

import static com.jpmorgan.exercise.market.TradeType.*;

public class MarketTest
{
    /**
     * Hard coded horizon for the calculation of the VWSP/GBCE.
     */
    private static final long HORIZON_WINDOW_SEC = 5 * 60;

    @Test
    public void testCalculateDividendYieldAllStocks () throws Exception
    {
        Market market = new Market ();

        double price = 10;

        assertEquals (0.0d, market.calculateDividendYield ("TEA", price), 0.0d);
        assertEquals (0.8d, market.calculateDividendYield ("POP", price), 0.0d);
        assertEquals (2.3d, market.calculateDividendYield ("ALE", price), 0.0d);
        assertEquals (0.2d, market.calculateDividendYield ("GIN", price), 0.0d);
        assertEquals (1.3d, market.calculateDividendYield ("JOE", price), 0.0d);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateDividendYieldInvalidStock () throws Exception
    {
        Market market = new Market ();

        market.calculateDividendYield ("invalid", 10);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateDividendYieldZeroPrice () throws Exception
    {
        Market market = new Market ();

        market.calculateDividendYield ("POP", 0);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateDividendYieldNegativePrice () throws Exception
    {
        Market market = new Market ();

        market.calculateDividendYield ("POP", -10);
    }

    @Test
    public void testCalculatePERatio () throws Exception
    {
        Market market = new Market ();

        double price = 10;

        assertEquals (0.0d, market.calculatePERatio ("TEA", price), 0.0d);
        assertEquals (1.25d, market.calculatePERatio ("POP", price), 0.0d);
        assertEquals (0.4348d, market.calculatePERatio ("ALE", price), 0.001d);
        assertEquals (1.25d, market.calculatePERatio ("GIN", price), 0.0d);
        assertEquals (0.7692d, market.calculatePERatio ("JOE", price), 0.001d);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculatePERatioInvalidStock () throws Exception
    {
        Market market = new Market ();

        market.calculatePERatio ("invalid", 10);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculatePERatioZeroPrice () throws Exception
    {
        Market market = new Market ();

        market.calculatePERatio ("POP", 0);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculatePERatioNegativePrice () throws Exception
    {
        Market market = new Market ();

        market.calculatePERatio ("POP", -10);
    }

    @Test
    public void testMixedCaseStockIdentifier () throws Exception
    {
        Market market = new Market ();

        double price = 10;

        /**
         * Use dividend yield for POP for the test.
         */
        assertEquals (0.8d, market.calculateDividendYield ("POP", price), 0.0d);
        assertEquals (0.8d, market.calculateDividendYield ("pop", price), 0.0d);
        assertEquals (0.8d, market.calculateDividendYield ("Pop", price), 0.0d);
        assertEquals (0.8d, market.calculateDividendYield ("pOp", price), 0.0d);
    }

    @Test
    public void testCalculateVolumeWeightedStockPrice () throws Exception
    {
        Market market = new Market ();

        /*
         * One trade within horizon.
         */
        market.recordTrade ("POP", 1, 100, BUY);
        assertEquals (100.0d, market.calculateVolumeWeightedStockPrice ("POP"), 0.0d);

        /*
         * Additional trade within horizon, for a different amount.
         */
        market.recordTrade ("POP", 3, 50, SELL);
        assertEquals (62.5d, market.calculateVolumeWeightedStockPrice ("POP"), 0.0d);

        /*
         * Additional trade just within the edge of the horizon.
         */
        Environment.getEnvironment ().tick (HORIZON_WINDOW_SEC - 1);
        market.recordTrade ("POP", 1, 50, SELL);
        assertEquals (60.0d, market.calculateVolumeWeightedStockPrice ("POP"), 0.0d);

        /*
         * Advance the clock so that first trade is outside the horizon.
         */
        Environment.getEnvironment ().tick (1);
        assertEquals (50.0d, market.calculateVolumeWeightedStockPrice ("POP"), 0.0d);
    }

    @Test
    public void testCalculateVolumeWeightedStockPriceNoTrades () throws Exception
    {
        Market market = new Market ();

        assertEquals (0.0d, market.calculateVolumeWeightedStockPrice ("POP"), 0.0d);

        /*
         * Add a trade, then advance the clock beyond the horizon.
         */
        market.recordTrade ("POP", 1, 50, SELL);
        Environment.getEnvironment ().tick (HORIZON_WINDOW_SEC);
        assertEquals (0.0d, market.calculateVolumeWeightedStockPrice ("POP"), 0.0d);

        /*
         * Add a trade, then calculate the volume weighted stock price for a different stock
         */
        market.recordTrade ("POP", 1, 50, SELL);
        assertEquals (0.0d, market.calculateVolumeWeightedStockPrice ("GIN"), 0.0d);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateVolumeWeightedStockPriceInvalidStock () throws Exception
    {
        Market market = new Market ();

        market.recordTrade ("invalid", 1, 100, BUY);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateVolumeWeightedStockPriceZeroQuantity () throws Exception
    {
        Market market = new Market ();

        market.recordTrade ("POP", 0, 100, BUY);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateVolumeWeightedStockPriceNegativeQuantity () throws Exception
    {
        Market market = new Market ();

        market.recordTrade ("POP", -1, 100, BUY);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateVolumeWeightedStockPriceZeroPrice () throws Exception
    {
        Market market = new Market ();

        market.recordTrade ("POP", 1, 0, BUY);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateVolumeWeightedStockPriceNegativePrice () throws Exception
    {
        Market market = new Market ();

        market.recordTrade ("POP", 1, -100, BUY);
    }

    @Test
    public void testCalculateGBCEOneStock () throws Exception
    {
        /**
         * GBCE will be the same as the volume weighted stock price if all trades are for a
         * single stock.
         */

        Market market = new Market ();

        /*
         * One trade within horizon.
         */
        market.recordTrade ("POP", 1, 100, BUY);
        assertEquals (100.0d, market.calculateGBCE (), 0.0d);

        /*
         * Additional trade within horizon, for a different amount.
         */
        market.recordTrade ("POP", 3, 50, SELL);
        assertEquals (62.5d, market.calculateGBCE (), 0.0d);

        /*
         * Additional trade just within the edge of the horizon.
         */
        Environment.getEnvironment ().tick (HORIZON_WINDOW_SEC - 1);
        market.recordTrade ("POP", 1, 50, SELL);
        assertEquals (60.0d, market.calculateGBCE (), 0.0d);

        /*
         * Advance the clock so that first trade is outside the horizon.
         */
        Environment.getEnvironment ().tick (1);
        assertEquals (50.0d, market.calculateGBCE (), 0.0d);
    }

    @Test
    public void testCalculateGBCE () throws Exception
    {
        Market market = new Market ();
        
        /**
         * Add three trades, with different stocks, calculate GBCE after each one.
         */
        market.recordTrade ("POP", 1, 50, BUY);
        assertEquals (50.0d, market.calculateGBCE (), 0.001d);

        market.recordTrade ("GIN", 2, 50, SELL);
        assertEquals (50.0d, market.calculateGBCE (), 0.001d);

        market.recordTrade ("JOE", 3, 50, BUY);
        assertEquals (50.0d, market.calculateGBCE (), 0.001d);
    }

    @Test
    public void testCalculateGBCENoTrades () throws Exception
    {
        Market market = new Market ();

        assertEquals (0.0d, market.calculateGBCE (), 0.0d);

        /*
         * Add a trade, then advance the clock beyond the horizon.
         */
        market.recordTrade ("POP", 1, 50, SELL);
        Environment.getEnvironment ().tick (HORIZON_WINDOW_SEC);
        assertEquals (0.0d, market.calculateGBCE (), 0.0d);
    }
}
