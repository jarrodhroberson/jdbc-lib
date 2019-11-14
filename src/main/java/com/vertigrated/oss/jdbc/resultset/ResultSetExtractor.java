package com.vertigrated.oss.jdbc.resultset;

import javax.annotation.Nonnull;
import java.sql.ResultSet;

@FunctionalInterface
public interface ResultSetExtractor<T>
{
    @Nonnull
    public T from(@Nonnull final ResultSet rs);
}
