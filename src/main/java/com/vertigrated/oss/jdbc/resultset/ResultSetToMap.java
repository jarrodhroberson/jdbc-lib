package com.vertigrated.oss.jdbc.resultset;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

public final class ResultSetToMap implements ResultSetExtractor<List<Map<String,String>>>
{
    private RowMapper<Map<String,String>> rowMapper = new RowMapper<Map<String,String>>()
    {
        @Nonnull
        @Override
        public Map<String,String> map(@Nonnull final ResultSet rs, @Nonnull final Integer rowNumber)
        {
            final ImmutableSortedMap.Builder<String,String> ismb = ImmutableSortedMap.naturalOrder();
            try
            {
                final ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next())
                {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++)
                    {
                        ismb.put(rsmd.getColumnName(i), firstNonNull(rs.getString(i), ""));
                    }
                }
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
            return ismb.build();
        }
    };

    @Nonnull
    @Override
    public List<Map<String,String>> from(@Nonnull final ResultSet rs)
    {
        final ImmutableList.Builder<Map<String,String>> ilb = ImmutableList.builder();
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
