package com.jpmorgan.exercise.market;

/**
 * Trades are marked as buy/sell using this enumerated type. In the current implementation
 * the trade types aren't used, but using an enumeration (instead of different classes)
 * means that extensions do not have to use <code>instanceof</code>.
 * 
 * @author Tony Cruickshank
 *
 */
public enum TradeType
{
    BUY, SELL,
}
