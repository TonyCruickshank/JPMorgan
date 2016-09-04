# JP Morgan Super Simple Stock Market

## Exercising the stock market code.

Running the code is done via the JUnit4 framework, within Eclipse. (Right-click on test,
select "Run As" > "JUnit Test".)

## Specification Questions & Assumptions

The following questions arose while examining the specification and implementing
the simple stock market. In theory I'd send these questions to the product owner/
business analyst/customer, but for the purposes of the exercise I've simply made
assumptions, which are recorded here.

   1. What are the expected range of inputs, and how should errors be handled?
      Assumed: prices, and quantities must be greater than 0.
      Assumed: prices, and quantities outside these ranges can be passed.
      Assumed: unrecognised stock identifiers can be passed.
      Assumed: an exception will be thrown when an invalid value is passed. 
   1. The P/E Ratio calculation uses the "Dividend". Is this different for different
      stock types (common, preferred)?
      Assumed: use the last dividend value, independent of stock type.
   1. What is the P/E Ratio for a stock that has a last dividend of 0. I presume this
      happens when a stock is new and thus no dividend has been paid, or the performance
      of the company was insufficient to pay a dividend.
      Assumed: the P/E ratio of such a stock is 0.
   1. The fixed dividend for "GIN" is 2%, which presumably should be 0.02 in absolute terms?
      Assumed: fixed dividend is 0.02 for "GIN".
   1. What is the volume weighted stock price for a stock that has not been traded within the
      last five minutes?
      Assumed: the volume weighted stock price is 0 (or 0/0 == 0).
   1. Does the GBCE all share index include stocks that have not traded within the last five
      minutes?
      Assumed: only traded stocks are used to calculate the GBCE value, as otherwise the 
      result will devolve to 0, which is not really useful as a market index.

## Specification Observations

Reviewing the specification, there several pieces of extraneous information that are not
used within the market. In all cases this information has been incorporated into the code,
on the assumption that it is required for extensions.

   1. Whether a trade is a buy or sell.
   1. Par value for a common stock.
   
## Code Overview

See [here](doc/index.html) for class documentation.

### src

<code>Market</code>

Main working class. To provide a "minimal viable product", the <code>Market</code> class
implements all the required methods for the exercise. For a larger project, this could be
split into two, with the stock information methods moved to a "Broker" class, leaving
just the trading and market information methods.

Note that, also following the "minimal viable product" paradigm, the assumption has been
made that the <code>Market</code> will be accessed by a single thread. I.e. the class is
not thread-safe. Synchronization will need to be added to address this, if required later,
presumably at the same time as the above move of stock related methods.

<code>Environment</code> & <code>Clock</code>

Interface (in the non-Java sense) to the outside world that the market
functions in. In this simple example, the only interface of interest
is the clock, which is used in trading operations.

Abstracting the environment is used to implement the Dependency Injection
pattern on the market. I.e. the information that the market depends on
(in this case the time of trade and window for calculate the VWSP/GBCE) is
injected into the class.

The <code>Clock</code> interface has one implementation, <code>ArtificialClock</code>,
which allows for manual setting and updating of the current time. This is used to test
the horizon of the stock market calculations.

### test

JUnit4 test classes, with a package structure that matches the src.
