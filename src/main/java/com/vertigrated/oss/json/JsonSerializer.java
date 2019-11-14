package com.vertigrated.oss.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;

@Immutable
public enum JsonSerializer implements Function<Object,String>
{
    COMPACT(new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
    .enable(SerializationFeature.WRITE_ENUMS_USING_INDEX)
    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    .disable(SerializationFeature.WRAP_ROOT_VALUE)
    .setSerializationInclusion(Include.NON_DEFAULT)
    .setSerializationInclusion(Include.NON_EMPTY)),
    INDENTED(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
             .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
    .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    .enable(SerializationFeature.FAIL_ON_EMPTY_BEANS));

    private final ObjectMapper objectMapper;

    JsonSerializer(@Nonnull final ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public void apply(@Nonnull final Object object, @Nonnull final OutputStream outputStream)
    {
        try
        {
            try
            {
                this.objectMapper.writer().writeValue(outputStream, object);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                outputStream.flush();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String apply(@Nonnull final Object object)
    {
        try
        {
            return this.objectMapper.writer().writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }
}
