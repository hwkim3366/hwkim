package com.exception;

public class ProductIdNullException extends ValidationException
{
    public ProductIdNullException()
    {
        super("productId required");
    }
}