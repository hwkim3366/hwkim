package com.exception;

public class ProductNotFoundException extends NotFoundException
{
    public ProductNotFoundException()
    {
        super("productId is missing or invalid");
    } 
}
