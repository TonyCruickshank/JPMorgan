package com.jpmorgan.exercise.stock;

import com.jpmorgan.exercise.exception.InvalidParameterException;

/**
 * Base immutable class to hold stock data used for both common and preferred stocks.
 * 
 * Note that par value data is not used for common stocks, but is provided, so I've placed
 * it in this class assuming that this data would be used if the code was extended.
 * 
 * @author Tony Cruickshank
 *
 */
public abstract class StockData
{

    private final String stockIdentifier;
    private final double lastDividend;
    private final double parValue;

    public StockData (final String stockIdentifier, final double lastDividend, final double parValue)
    {
        this.stockIdentifier = stockIdentifier;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
    }

    public String getStockIdentifier ()
    {
        return stockIdentifier;
    }

    public double getLastDividend ()
    {
        return lastDividend;
    }

    public double getParValue ()
    {
        return parValue;
    }

    public abstract double calculateDividendYield (final double price) throws InvalidParameterException;

    public double calculatePERatio (final double price) throws InvalidParameterException
    {
        if (price <= 0)
        {
            throw new InvalidParameterException ("invalid price " + price);
        }
        return (lastDividend == 0) ? 0 : price / lastDividend;
    }
}
