package com.grebenkov.handler.impl;

import com.grebenkov.dto.Result;
import com.grebenkov.handler.ResultHandler;
import org.springframework.stereotype.Component;

@Component
public class ResultHandlerImpl implements ResultHandler {

    @Override
    public void print(Result result) {
        System.out.println(result);
    }
}
