package com.vertigrated.oss.jdbc;

import java.util.Set;

public interface Schema
{
    public Set<Table> tables();
    public Set<View> views();
    public Set<Procedure> procedures();
}
