package com.grebenkov.handler;

import com.grebenkov.dto.Result;

/**
 * Handles parsing result.
 */
public interface ResultHandler {

    /**
     * Prints the result to console.
     *
     * @param result result to print
     */
    void print(Result result);
}
