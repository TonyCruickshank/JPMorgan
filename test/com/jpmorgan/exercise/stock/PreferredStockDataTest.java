package com.jpmorgan.exercise.stock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jpmorgan.exercise.exception.InvalidParameterException;
import com.jpmorgan.exercise.stock.PreferredStockData;

public class PreferredStockDataTest {

    @Test
    public void testCalculateDividendYield () throws Exception
    {
        PreferredStockData testStockData = new PreferredStockData ("TST", 10, 100, 0.05);

        double expectedDividendYield = 0.05;
        double actualDividendYield = testStockData.calculateDividendYield (100);

        assertEquals (expectedDividendYield, actualDividendYield, 0.0d);
    }

    @Test
    public void testCalculateDividendYieldZeroLastDividend () throws Exception
    {
        PreferredStockData testStockData = new PreferredStockData ("TST", 0, 100, 0.05); // 0.05 == 5%

        double expectedDividendYield = 0.05;
        double actualDividendYield = testStockData.calculateDividendYield (100);

        assertEquals (expectedDividendYield, actualDividendYield, 0.0d);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateDividendYieldZeroPrice () throws Exception
    {
        PreferredStockData testStockData = new PreferredStockData ("TST", 10, 100, 0.05);
        testStockData.calculateDividendYield (0);
    }

    @Test (expected = InvalidParameterException.class)
    public void testCalculateDividendYieldInvalidPrice () throws Exception
    {
        PreferredStockData testStockData = new PreferredStockData ("TST", 10, 100, 0.05);
        testStockData.calculateDividendYield (-100);
    }
}
