package com.vertigrated.oss.jdbc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.vertigrated.oss.json.Jsonizable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.sql.Types;
import java.util.Objects;

@Immutable
public final class Column implements Comparable<Column>, Jsonizable
{
    public static final Ordering<Column> NATURAL;
    public static final Ordering<Column> ALPHABETICAL;
    public static final Ordering<Column> ORDINAL;

    public static enum Generated
    {
        IS_GENERATED("YES"),
        IS_NOT_GENERATED("NO"),
        UNKNOWN("UNKNOWN");

        public static Generated from(@Nonnull final String value)
        {
            return value.equals("YES") ? IS_GENERATED : value.equals("NO") ? IS_NOT_GENERATED : UNKNOWN;
        }

        private final String value;

        private Generated(@Nonnull final String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return this.value;
        }
    }

    public static enum AutoIncrement
    {
        IS_AUTO_INCREMENT("YES"),
        IS_NOT_AUTO_INCREMENT("NO"),
        UNKNOWN("");

        public static final AutoIncrement from(@Nonnull final String value)
        {
            return value.equals("YES") ? IS_AUTO_INCREMENT : value.equals("NO") ? IS_NOT_AUTO_INCREMENT : UNKNOWN;
        }

        private final String value;

        private AutoIncrement(@Nonnull final String value)
        {
            this.value = value;
        }

        public String toString()
        {
            return this.value;
        }
    }

    static
    {
        NATURAL = Ordering.compound(ImmutableList.of((o1, o2) -> o1.catalog.compareTo(o2.catalog),
                                                     (o1, o2) -> o1.schema.compareTo(o2.schema),
                                                     (o1, o2) -> o1.table.compareTo(o2.table),
                                                     (o1, o2) -> o1.ordinal.compareTo(o2.ordinal)));
        ALPHABETICAL = Ordering.compound(ImmutableList.of((o1, o2) -> o1.catalog.compareTo(o2.catalog),
                                                          (o1, o2) -> o1.schema.compareTo(o2.schema),
                                                          (o1, o2) -> o1.table.compareTo(o2.table),
                                                          (o1, o2) -> o1.name.compareTo(o2.name)));
        ORDINAL = new Ordering<Column>()
        {
            @Override
            public int compare(@Nonnull final Column left, @Nonnull final Column right)
            {
                return left.ordinal.compareTo(right.ordinal);
            }
        };
    }

    @JsonProperty
    public final String catalog;
    @JsonProperty
    public final String schema;
    @JsonProperty
    public final String table;
    @JsonProperty
    public final String name;
    @JsonProperty
    public final String type;
    @JsonProperty
    public final Size size;
    @JsonProperty
    public final String remarks;
    @JsonProperty
    public final Integer ordinal;
    @JsonProperty("is_nullable")
    public final boolean isNullable;
    @JsonProperty("default_value")
    public final String defaultValue;
    @JsonProperty("is_auto_increment")
    public final AutoIncrement isAutoIncremented;
    @JsonProperty("is_generated")
    public final Generated isGenerated;

    public Column(final String catalog, final String schema, final String table, final String name, final String type,
                  final Size size, final String remarks, final boolean isNullable, final Integer ordinal, final String defaultValue,
                  final AutoIncrement isAutoIncremented, final Generated isGenerated)
    {
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
        this.name = name;
        this.type = type;
        this.size = size;
        this.remarks = remarks;
        this.ordinal = ordinal;
        this.isNullable = isNullable;
        this.defaultValue = defaultValue;
        this.isAutoIncremented = isAutoIncremented;
        this.isGenerated = isGenerated;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(catalog, schema, table, name, type, remarks, isNullable, ordinal);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Column column = (Column) o;
        return isNullable == column.isNullable &&
        Objects.equals(catalog, column.catalog) &&
        Objects.equals(schema, column.schema) &&
        Objects.equals(table, column.table) &&
        Objects.equals(name, column.name) &&
        Objects.equals(type, column.type) &&
        Objects.equals(remarks, column.remarks) &&
        Objects.equals(ordinal, column.ordinal);
    }

    @Override
    public String toString()
    {
        return this.toJson();
    }

    @Override
    public int compareTo(final Column o)
    {
        return NATURAL.compare(this, o);
    }

    public static final class Size implements Jsonizable
    {
        public static enum SizeType
        {
            CHARACTER_LENGTH,
            MAXIMUM_PRECISION,
            BYTE_LENGTH,
            NOT_APPLICABLE
        }

        public static final SizeType from(@Nonnull final Integer value)
        {
            switch (value)
            {
                case Types.ARRAY:
                case Types.BIGINT:
                case Types.BINARY:
                case Types.BIT:
                case Types.BLOB:
                case Types.CLOB:
                case Types.DATE:
                case Types.CHAR:
                case Types.BOOLEAN:
                case Types.FLOAT:
                case Types.NUMERIC:
                case Types.NCHAR:
                case Types.NCLOB:
                case Types.NULL:
                case Types.DOUBLE:
                case Types.TINYINT:
                case Types.VARCHAR:
                case Types.OTHER:
                case Types.REAL:
                case Types.REF:
                case Types.NVARCHAR:
                case Types.REF_CURSOR:
                case Types.ROWID:
                case Types.SQLXML:
                case Types.STRUCT:
                case Types.TIME:
                case Types.SMALLINT:
                case Types.TIMESTAMP:
                case Types.VARBINARY:
                case Types.INTEGER:
                case Types.LONGVARCHAR:
                case Types.JAVA_OBJECT:
                case Types.LONGNVARCHAR:
                case Types.DECIMAL:
                case Types.LONGVARBINARY:
                case Types.DATALINK:
                case Types.TIME_WITH_TIMEZONE:
                case Types.TIMESTAMP_WITH_TIMEZONE:
                case Types.DISTINCT:
                default:
                    throw new IllegalArgumentException(String.format("%s is not a valid java.sql.Type [%s]", value, Joiner.on(',').withKeyValueSeparator(":").join(Maps.newEnumMap(SqlType.class))));
            }
        }

        @JsonProperty
        public final SizeType type;
        @JsonProperty
        public final Long size;

        public Size(final SizeType type, final Long size)
        {
            this.type = type;
            this.size = size;
        }

        @Override
        public int hashCode()
        {
            return com.google.common.base.Objects.hashCode(type, size);
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            final Size size1 = (Size) o;
            return type == size1.type &&
            com.google.common.base.Objects.equal(size, size1.size);
        }

        @Override
        public String toString()
        {
            return this.toJson();
        }
    }

    public static final class Privilege implements Jsonizable
    {
        public enum Grantable
        {
            YES,
            NO,
            UNKNOWN
        }

        @JsonProperty
        public final String catalog;
        @JsonProperty
        public final String schema;
        @JsonProperty
        public final String table;
        @JsonProperty
        public final String name;
        @JsonProperty
        public final String grantor;
        @JsonProperty
        public final String priviledge;
        @JsonProperty("is_grantable")
        public final Grantable isGrantable;

        public Privilege(final String catalog, final String schema, final String table, final String name, final String grantor, final String priviledge, final Grantable isGrantable)
        {
            this.catalog = catalog;
            this.schema = schema;
            this.table = table;
            this.name = name;
            this.grantor = grantor;
            this.priviledge = priviledge;
            this.isGrantable = isGrantable;
        }

        @Override
        public int hashCode()
        {
            return com.google.common.base.Objects.hashCode(catalog, schema, table, name, grantor, priviledge, isGrantable);
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            final Privilege privilege = (Privilege) o;
            return com.google.common.base.Objects.equal(catalog, privilege.catalog) &&
            com.google.common.base.Objects.equal(schema, privilege.schema) &&
            com.google.common.base.Objects.equal(table, privilege.table) &&
            com.google.common.base.Objects.equal(name, privilege.name) &&
            com.google.common.base.Objects.equal(grantor, privilege.grantor) &&
            com.google.common.base.Objects.equal(priviledge, privilege.priviledge) &&
            isGrantable == privilege.isGrantable;
        }

        @Override
        public String toString()
        {
            return this.toJson();
        }
    }
}
