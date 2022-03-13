package com.config;

public enum ResponseCodes
{

     S1("00", "Processing completed") //성공
    ,F1("01", "Required input value is missing") //필수입력 누락
    ,F2("02", "Bad Request") //잘못된 요청
    ,F3("03", "Forbidden") //권한 없음
    ,F4("04", "Not Found") //찾을수 없음
    ;

    public final String code;
    
    public final String description;

    ResponseCodes(String code, String description) 
    {
        this.code = code;
        
        this.description = description;
    }
}
