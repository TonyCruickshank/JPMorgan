package com.jpmorgan.exercise.market;

/**
 * Immutable bean to hold trade information.
 * 
 * @author Tony Cruickshank
 *
 */
public class Trade
{

    private final String    stockIdentifier;
    private final long      timestamp;
    private final int       quantity;
    private final double    price;
    private final TradeType type;

    public Trade (final String stockIdentifier,
                  final long timestamp,
                  final int quantity,
                  final double price,
                  final TradeType type)
    {
        this.stockIdentifier = stockIdentifier;
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public String getStockIdentifier ()
    {
        return stockIdentifier;
    }

    public long getTimestamp ()
    {
        return timestamp;
    }

    public int getQuantity ()
    {
        return quantity;
    }

    public double getPrice ()
    {
        return price;
    }

    public TradeType getType ()
    {
        return type;
    }
    
    @Override
    public String toString ()
    {
        StringBuilder builder = new StringBuilder ();
        
        builder.append ("Trade:[stock: ");
        builder.append (stockIdentifier);
        builder.append (", timestamp: ");
        builder.append (timestamp);
        builder.append (", quantity: ");
        builder.append (quantity);
        builder.append (", price: ");
        builder.append (price);
        builder.append (", type: ");
        builder.append (type);
        builder.append ("]");
        
        return builder.toString ();
    }
}
