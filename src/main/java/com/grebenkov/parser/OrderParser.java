package com.grebenkov.parser;

import com.grebenkov.dto.Result;

import java.io.File;
import java.util.List;

/**
 * Parses orders in different ways.
 */
public interface OrderParser {

    /**
     * Parses from file.
     *
     * @param file file to parse
     */
    List<Result> parse(File file);
}
