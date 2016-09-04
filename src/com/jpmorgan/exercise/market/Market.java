package com.jpmorgan.exercise.market;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import com.jpmorgan.exercise.exception.InvalidParameterException;
import com.jpmorgan.exercise.environment.Environment;
import com.jpmorgan.exercise.stock.CommonStockData;
import com.jpmorgan.exercise.stock.PreferredStockData;
import com.jpmorgan.exercise.stock.StockData;

/**
 * Main working class. To provide a "minimal viable product", the <code>Market</code> class
 * implements all the required methods for the exercise. For a larger project, this could be
 * split into two, with the stock information methods moved to a "Broker" class, leaving
 * just the trading and market information methods.
 * 
 * Note that, also following the "minimal viable product" paradigm, the assumption has been
 * made that the <code>Market</code> will be accessed by a single thread. I.e. the class is
 * not thread-safe. Synchronization will need to be added to address this, if required later,
 * presumably at the same time as the above move of stock related methods.
 * 
 * @author Tony Cruickshank
 */

public class Market
{

    /**
     * Hard coded horizon for the calculation of the VWSP/GBCE.
     */
    private static final long HORIZON_WINDOW_SEC = 5 * 60;

    /**
     * Hard coded stock information.
     * 
     * Using a TreeMap with CASE_INSENSITIVE_ORDER allows access using non-case
     * matching keys.
     */
    private static final Map<String /* stockIdentifier */, StockData> MARKET_DATA ()
    {
        return Collections.unmodifiableMap (new TreeMap<String, StockData> (String.CASE_INSENSITIVE_ORDER)
        {
            private static final long serialVersionUID = -5389107945226081680L;
            {
                put ("TEA", new CommonStockData ("TEA", 0, 100));
                put ("POP", new CommonStockData ("POP", 8, 100));
                put ("ALE", new CommonStockData ("ALE", 23, 100));
                put ("GIN", new PreferredStockData ("GIN", 8, 100, 0.02)); // Note 2% => 0.02
                put ("JOE", new CommonStockData ("JOE", 13, 100));
            }
        });
    };

    /**
     * Ledger of the trades made on the market.
     * 
     * Note that, in the current implementation, the ledger is never cleared as offers/bids
     * are not matched.
     */
    private LinkedList<Trade> ledger = new LinkedList<Trade> ();

    public Market ()
    {

    }

    /*
     * ********************************************************************************
     * STOCK INFORMATION METHODS
     */

    /**
     * Returns the dividend yield for a stock, by passing through to the stock held by
     * the market.
     * 
     * @param stockIdentifier
     *            Three letter identifier for the stock.
     * @param price
     *            The price to use in the calculation. Must be greater than 0.
     * @return Dividend yield.
     * @throws InvalidParameterException
     *             For unrecognised stock identifier or invalid price.
     */
    public double calculateDividendYield (final String stockIdentifier,
                                          final double price) throws InvalidParameterException
    {
        if (!MARKET_DATA ().containsKey (stockIdentifier))
        {
            throw new InvalidParameterException ("invalid stock " + stockIdentifier);
        }
        return MARKET_DATA ().get (stockIdentifier).calculateDividendYield (price);
    }

    /**
     * Returns the P/E Ratio for a stock, by passing through to the stock held by the
     * market.
     * 
     * @param stockIdentifier
     *            Three letter identifier for the stock.
     * @param price
     *            The price to use in the calculation. Must be greater than 0.
     * @return P/E Ratio.
     * @throws InvalidParameterException
     *             For unrecognised stock identifier or invalid price.
     */
    public double calculatePERatio (final String stockIdentifier, final double price) throws InvalidParameterException
    {
        if (!MARKET_DATA ().containsKey (stockIdentifier))
        {
            throw new InvalidParameterException ("invalid stock " + stockIdentifier);
        }
        return MARKET_DATA ().get (stockIdentifier).calculatePERatio (price);
    }

    /*
     * ********************************************************************************
     * MARKET TRADE AND INFORMATION METHODS
     */

    /**
     * Record a trade, placing it in the ledger.
     * 
     * @param stockIdentifier
     *            Three letter identifier for the stock.
     * @param quantity
     *            The number of stocks in the trade.
     * 
     * @param price
     *            The price to use in the calculation. Must be greater than 0.
     * @param tradeType
     *            Buy or sell (currently unused).
     * @throws InvalidParameterException
     *             For unrecognised stock identifier, quantity, or invalid price.
     */
    public void recordTrade (final String stockIdentifier,
                             final int quantity,
                             final double price,
                             final TradeType tradeType) throws InvalidParameterException
    {
        if (!MARKET_DATA ().containsKey (stockIdentifier))
        {
            throw new InvalidParameterException ("invalid stock " + stockIdentifier);
        }
        if (quantity <= 0)
        {
            throw new InvalidParameterException ("invalid quantity " + stockIdentifier);
        }
        if (price <= 0)
        {
            throw new InvalidParameterException ("invalid price " + price);
        }

        ledger.addLast (new Trade (stockIdentifier.toUpperCase (),
                                   Environment.getEnvironment ().getTime (),
                                   quantity,
                                   price,
                                   tradeType));
    }

    /**
     * Calculate the volume weighted stock price over a horizon of 5 seconds.
     * 
     * @param stockIdentifier
     *            Three letter identifier for the stock.
     * @return Volume weighted stock price. Note this will be 0 if there are no trades within
     *         the horizon.
     * @throws InvalidParameterException
     *             For unrecognised stock identifier
     */
    public double calculateVolumeWeightedStockPrice (final String stockIdentifier) throws InvalidParameterException
    {
        if (!MARKET_DATA ().containsKey (stockIdentifier))
        {
            throw new InvalidParameterException ("invalid stock " + stockIdentifier);
        }

        double vwspNominator = 0;
        double vwspDenominator = 0;

        long horizon = Environment.getEnvironment ().getTime () - HORIZON_WINDOW_SEC;
        for (Trade trade : ledger)
        {
            if (trade.getStockIdentifier ().equalsIgnoreCase (stockIdentifier) && trade.getTimestamp () > horizon)
            {
                vwspNominator += trade.getPrice () * trade.getQuantity ();
                vwspDenominator += trade.getQuantity ();
            }
        }

        return (vwspDenominator == 0) ? 0 : vwspNominator / vwspDenominator;
    }

    /**
     * Calculate the GBCE all share index, using the volume weighted stock price for all stocks
     * traded over a horizon of 5 seconds.
     * 
     * Note that <code>double</code> is used for the calculation as for this exercise it's
     * assumed that it provides the necessary range and precision. To increase the range,
     * the calculations below could be modified to use log (changing products to sums,
     * and power roots to multiplications). And/or, use <code>BigDecimal</code> for the
     * calculations.
     * 
     * @return GBCE all share index. Note that this will be 0 if there are no trades within the
     *         horizon.
     */
    public double calculateGBCE ()
    {

        /**
         * Two options:
         * 1) Re-use the above method for each stock in turn.
         * 2) Run through the ledger again, storing the necessary data for each stock.
         * 
         * Option 1 is less time efficient, requiring multiple runs through the ledger
         * which will include stocks that weren't traded within the horizon.
         * 
         * Option 2 essentally replicates the calculation, so any code update will need
         * to be done in two places.
         * 
         * I've selected option 2 as, generally speaking, markets can contain many stocks,
         * and many trades, and avoiding processing the ledger multiple times is worth the
         * trade-off of code replication.
         */

        /**
         * Maps to hold the calculations required for each trade in the ledger, within the window.
         */
        Map<String, Double> vwspNominators = new HashMap<> ();
        Map<String, Double> vwspDenominators = new HashMap<> ();

        long horizon = Environment.getEnvironment ().getTime () - HORIZON_WINDOW_SEC;
        for (Trade trade : ledger)
        {
            if (trade.getTimestamp () > horizon)
            {
                String stockIndicator = trade.getStockIdentifier ();
                if (!vwspNominators.containsKey (stockIndicator))
                {
                    vwspNominators.put (stockIndicator, 0d);
                    vwspDenominators.put (stockIndicator, 0d);
                }
                vwspNominators.put (stockIndicator,
                                    vwspNominators.get (stockIndicator) + trade.getPrice () * trade.getQuantity ());
                vwspDenominators.put (stockIndicator, vwspDenominators.get (stockIndicator) + trade.getQuantity ());
            }
        }
        double gbce = 0;

        int nTrades = vwspNominators.keySet ().size ();
        if (nTrades > 0)
        {
            double geometricMean = 1;
            for (String stockIndicator : vwspNominators.keySet ())
            {
                geometricMean *= vwspNominators.get (stockIndicator) / vwspDenominators.get (stockIndicator);
            }
            gbce = Math.pow (geometricMean, 1 / (double)nTrades);
        }

        return gbce;
    }
}
