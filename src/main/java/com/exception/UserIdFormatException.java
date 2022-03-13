package com.exception;

public class UserIdFormatException extends ValidationException
{
    public UserIdFormatException()
    {
        super("x-user-id can only be numeric");
    }
}