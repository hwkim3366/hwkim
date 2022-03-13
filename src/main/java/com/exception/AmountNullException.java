package com.exception;

public class AmountNullException extends ValidationException
{
    public AmountNullException()
    {
        super("amount required");
    }
}