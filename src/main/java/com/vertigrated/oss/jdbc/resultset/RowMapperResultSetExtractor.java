package com.vertigrated.oss.jdbc.resultset;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>>
{
    private final RowMapper<T> rowMapper;

    public RowMapperResultSetExtractor(final RowMapper<T> rowMapper)
    {
        this.rowMapper = rowMapper;
    }

    @Nonnull
    @Override
    public List<T> from(@Nonnull final ResultSet rs)
    {
        final ImmutableList.Builder<T> ilb = ImmutableList.builder();
        try
        {
            while (rs.next())
            {
                ilb.add(this.rowMapper.map(rs, -1));
            }
        }
        catch (final SQLException e)
        {
            throw new RuntimeException(e);
        }
        return ilb.build();
    }
}
