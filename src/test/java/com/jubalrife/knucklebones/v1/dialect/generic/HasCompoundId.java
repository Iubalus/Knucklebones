package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;

@Table(name = "Example")
public class HasCompoundId {
    @Id public Integer id;
    @Id public Integer id2;
    public String value1;
    public String value2;
}
