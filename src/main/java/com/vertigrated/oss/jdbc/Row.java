package com.vertigrated.oss.jdbc;

import com.vertigrated.oss.identity.Identifiable;

import java.util.Set;

public class Row extends Identifiable<String>
{
    public Set<Column> columns;

    public Row(final Set<Column> columns)
    {
        this.columns = columns;
    }

    @Override
    public String identity()
    {

    }
}
