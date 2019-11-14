package com.vertigrated.oss.jdbc.resultset;

import javax.annotation.Nonnull;
import java.sql.ResultSet;

@FunctionalInterface
public interface RowMapper<T>
{
    @Nonnull
    public T map(@Nonnull final ResultSet rs, @Nonnull final Integer rowNumber);
}
