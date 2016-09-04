package com.jpmorgan.exercise.stock;

import com.jpmorgan.exercise.exception.InvalidParameterException;

/**
 * Immutable class to hold information on preferred stocks.
 * 
 * @author Tony Cruickshank
 *
 */
public class PreferredStockData extends StockData
{

    private final double fixedDividend;

    public PreferredStockData (final String stockIdentifier, final double lastDividend, final double parValue, final double fixedDividend)
    {
        super (stockIdentifier, lastDividend, parValue);
        this.fixedDividend = fixedDividend;
    }

    @Override
    public double calculateDividendYield (final double price) throws InvalidParameterException
    {
        if (price <= 0)
        {
            throw new InvalidParameterException ("invalid price " + price);
        }
        return fixedDividend * getParValue () / price;
    }

    @Override
    public String toString ()
    {
        StringBuilder builder = new StringBuilder ();

        builder.append ("PreferredStockData:[stock: ");
        builder.append (super.getStockIdentifier ());
        builder.append (", lastDividend: ");
        builder.append (super.getLastDividend ());
        builder.append (", parValue: ");
        builder.append (super.getParValue ());
        builder.append (", fixedDividend: ");
        builder.append (fixedDividend);
        builder.append ("]");

        return builder.toString ();
    }
}
