package com.vertigrated.oss.jdbc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vertigrated.oss.jdbc.SqlType.Serializer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.sql.Types;
import java.util.EnumMap;

@Immutable
@JsonSerialize(using = Serializer.class)
public enum SqlType
{
    ARRAY(Types.ARRAY),
    BIGINT(Types.BIGINT),
    BINARY(Types.BINARY),
    BIT(Types.BIT),
    BLOB(Types.BLOB),
    CLOB(Types.CLOB),
    DATE(Types.DATE),
    CHAR(Types.CHAR),
    BOOLEAN(Types.BOOLEAN),
    FLOAT(Types.FLOAT),
    NUMERIC(Types.NUMERIC),
    NCHAR(Types.NCHAR),
    NCLOB(Types.NCLOB),
    NULL(Types.NULL),
    DOUBLE(Types.DOUBLE),
    TINYINT(Types.TINYINT),
    VARCHAR(Types.VARCHAR),
    OTHER(Types.OTHER),
    REAL(Types.REAL),
    REF(Types.REF),
    NVARCHAR(Types.NVARCHAR),
    REF_CURSOR(Types.REF_CURSOR),
    ROWID(Types.ROWID),
    SQLXML(Types.SQLXML),
    STRUCT(Types.STRUCT),
    TIME(Types.TIME),
    SMALLINT(Types.SMALLINT),
    TIMESTAMP(Types.TIMESTAMP),
    VARBINARY(Types.VARBINARY),
    INTEGER(Types.INTEGER),
    LONGVARCHAR(Types.LONGVARCHAR),
    JAVA_OBJECT(Types.JAVA_OBJECT),
    LONGNVARCHAR(Types.LONGNVARCHAR),
    DECIMAL(Types.DECIMAL),
    LONGVARBINARY(Types.LONGVARBINARY),
    DATALINK(Types.DATALINK),
    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE),
    TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE),
    DISTINCT(Types.DISTINCT);

    private final Integer jdbcType;

    SqlType(@Nonnull final Integer jdbcType)
    {
        this.jdbcType = jdbcType;
    }

    public EnumMap<SqlType,Integer> enumMap()
    {
        return new EnumMap<SqlType,Integer>(SqlType.class);
    }

    @Override
    public String toString()
    {
        return this.jdbcType.toString();
    }

    public static final class Serializer extends JsonSerializer<SqlType>
    {
        @Override
        public void serialize(final SqlType value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException
        {
            // @formatter:off
            gen.writeStartObject();
                gen.writeStringField("name", value.toString());
                gen.writeNumberField("value", value.jdbcType);
            gen.writeEndObject();
            // @formatter:off
        }
    }

    public static final class Deserializer extends JsonDeserializer<SqlType>
    {
    @Override
        public SqlType deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            return SqlType.valueOf(p.getCodec().readTree(p).get("name").toString());
        }
    }
}
