package com.config;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Response
{
    @NonNull
    private String code;
    
    @NonNull
    private String message;
    
    private Object body;

    public static Response of(String code, String message)
    {
        return new Response(code, message);
    }

    public static Response of(ResponseCodes code)
    {
        return new Response(code.code, code.description);
    }

    public static Response of(ResponseCodes code, Object body)
    {
    	Response response = of(code);
        
        response.setBody(body);
        
        return response;
    }

}
