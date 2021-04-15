package com.bry.coffeeshopjpa.support;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理 CNY 10.00 或者10.00 类似的数据
 */
@Component
@Slf4j
public class MoneyFormatter implements Formatter<Money> {
    @Override public Money parse(String text, Locale locale) throws ParseException {
        if (NumberUtils.isParsable(text)) {
            return Money.of(CurrencyUnit.of("CNY"), NumberUtils.createBigDecimal(text));
        } else if (StringUtils.isNoneEmpty(text)) {
            String[] splits = StringUtils.split(text, " ");
            if (splits != null && splits.length == 2 && NumberUtils.isParsable(splits[1])) {
                return Money.of(CurrencyUnit.of(splits[0]), NumberUtils.createBigDecimal(splits[1]));
            } else {
                throw new ParseException(text, 0);
            }
        }
        throw new ParseException(text, 0);
    }

    @Override public String print(Money money, Locale locale) {
        log.info("need convert to String {}", money);
        if (money == null) {
            return null;
        }
        return money.getCurrencyUnit().getCode() + " " + money.getAmountMajorInt();
    }
}
