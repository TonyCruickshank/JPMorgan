package com.jpmorgan.exercise.exception;

/**
 * Exception raised when methods are given an invalid parameter value,
 * for example a negative price.
 * @author Tony Cruickshank
 *
 */
public class InvalidParameterException extends Exception
{

    private static final long serialVersionUID = -2504673485087542811L;

    public InvalidParameterException (String message)
    {
        super (message);
    }

}
