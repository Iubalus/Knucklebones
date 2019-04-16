package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.annotation.GeneratedValue;
import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;

@Table(name = "Example")
public class HasGeneratedId {

    @Id @GeneratedValue public Integer id;
    public String value;
}
