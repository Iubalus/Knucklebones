package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;

@Table(name = "Example")
public class HasSingleId {
    @Id public Integer id;
    public String value1;
    public String value2;
}
