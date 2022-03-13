package com.exception;

public class UserIdNullException extends ValidationException
{
    public UserIdNullException()
    {
        super("x-user-id required");
    }
}