package com.jpmorgan.exercise.stock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jpmorgan.exercise.exception.InvalidParameterException;
import com.jpmorgan.exercise.stock.CommonStockData;

public class StockDataTest
{

    @Test
    public void testCalculatePERatio () throws Exception
    {
        CommonStockData testStockData = new CommonStockData ("TST", 10, 100);

        double expectedDividendYield = 10.0;
        double actualDividendYield = testStockData.calculatePERatio (100);

        assertEquals (expectedDividendYield, actualDividendYield, 0.0d);

    }
    
    @Test
    public void testCalculateDividendYieldZeroLastDividend () throws Exception
    {
        CommonStockData testStockData = new CommonStockData ("TST", 0, 100);

        double expectedDividendYield = 0.0;
        double actualDividendYield = testStockData.calculatePERatio (100);

        assertEquals (expectedDividendYield, actualDividendYield, 0.0d);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateDividendYieldZeroPrice () throws Exception
    {
        CommonStockData testStockData = new CommonStockData ("TST", 10, 100);
        testStockData.calculatePERatio (0);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateDividendYieldInvalidPrice () throws Exception
    {
        CommonStockData testStockData = new CommonStockData ("TST", 10, 100);
        testStockData.calculatePERatio (-100);
    }
}
