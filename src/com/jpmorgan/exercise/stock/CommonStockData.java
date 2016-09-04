package com.jpmorgan.exercise.stock;

import com.jpmorgan.exercise.exception.InvalidParameterException;

/**
 * Immutable bean to hold information on common stocks.
 * 
 * @author Tony Cruickshank
 *
 */
public class CommonStockData extends StockData
{

    public CommonStockData (String stockIdentifier, double lastDividend, double parValue)
    {
        super (stockIdentifier, lastDividend, parValue);
    }

    @Override
    public double calculateDividendYield (final double price) throws InvalidParameterException
    {
        if (price <= 0)
        {
            throw new InvalidParameterException ("invalid price " + price);
        }
        return getLastDividend () / price;
    }

    @Override
    public String toString ()
    {
        StringBuilder builder = new StringBuilder ();

        builder.append ("CommonStockData:[stock: ");
        builder.append (super.getStockIdentifier ());
        builder.append (", lastDividend: ");
        builder.append (super.getLastDividend ());
        builder.append (", parValue: ");
        builder.append (super.getParValue ());
        builder.append ("]");

        return builder.toString ();
    }
}
