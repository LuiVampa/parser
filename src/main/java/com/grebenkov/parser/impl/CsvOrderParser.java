package com.grebenkov.parser.impl;

import com.grebenkov.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Parses orders in csv format.
 */
@Component("csvObjectParser")
public class CsvOrderParser extends AbstractOrderParser {

    private static final Logger LOG = LoggerFactory.getLogger(CsvOrderParser.class);
    private static final String INVALID_LINE = "Invalid line: '%s'";
    private static final String INCORRECT_FORMAT = "Incorrect line format: '%s'";

    @Override
    protected Result getResult(String line) {
        final String[] orderData = line.split(",");
        if (orderData.length != 4) {
            return new Result(String.format(INVALID_LINE, line));
        }
        try {
            return new Result(Long.parseLong(orderData[0]),
                              new BigDecimal(orderData[1]),
                              orderData[2],
                              orderData[3],
                              OK);
        } catch (NumberFormatException e) {
            LOG.error(ERROR_MSG, line, e);
            return new Result(String.format(INCORRECT_FORMAT, line));
        }
    }
}
