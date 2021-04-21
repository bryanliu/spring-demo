package com.bry.coffeeshopjpa.support;

import java.io.IOException;

import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonComponent
public class MoneySerializer extends StdSerializer<Money> {

    protected MoneySerializer() {
        super(Money.class);
    }

    @Override public void serialize(Money value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value.getAmount());
    }
}
