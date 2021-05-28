package com.grebenkov.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Result {

    private static final String RESULT_TO_STRING = "{\"id\":%d,\"amount\":%s,\"currency\":\"%s\",\"comment\":\"%s\",\"fileName\":\"%s\",\"line\":%d,\"processingResult\":\"%s\"}";

    private Long id;
    private BigDecimal amount;
    private String currency;
    private String comment;
    private String fileName;
    private Integer line;
    private String processingResult;

    public Result(String result) {
        this.processingResult = result;
    }

    public Result(Long id, BigDecimal amount, String currency, String comment, String processingResult) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
        this.processingResult = processingResult;
    }

    @Override
    public String toString() {
        return String.format(
                RESULT_TO_STRING,
                id,
                amount,
                currency,
                comment,
                fileName,
                line,
                processingResult
        );
    }
}
