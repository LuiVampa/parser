package com.grebenkov.parser.impl;

import com.grebenkov.dto.Result;
import com.grebenkov.parser.OrderParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractOrderParser implements OrderParser {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractOrderParser.class);
    private static final String EMPTY_LINE = "Empty line";
    protected static final String OK = "OK";
    protected static final String ERROR_MSG = "Couldn't parse line - {}";

    @Override
    public List<Result> parse(File file) {
        try {
            final List<String> lines = Files.readAllLines(file.toPath());
            return IntStream.rangeClosed(1, lines.size())
                            .parallel()
                            .mapToObj(lineNumber -> {
                                final String line = lines.get(lineNumber - 1);
                                Result result = StringUtils.isBlank(line)
                                                   ? new Result(EMPTY_LINE)
                                                   : getResult(line);
                                result.setLine(lineNumber);
                                result.setFileName(file.getName());
                                return result;
                            })
                            .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Couldn't read file - {}", file.getName(), e);
        }
        return Collections.emptyList();
    }

    protected abstract Result getResult(String line);
}
