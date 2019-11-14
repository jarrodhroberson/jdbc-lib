package com.vertigrated.oss.json;

public interface Jsonizable
{
    public default String toJson()
    {
        return JsonSerializer.INDENTED.apply(this);
    }
}
