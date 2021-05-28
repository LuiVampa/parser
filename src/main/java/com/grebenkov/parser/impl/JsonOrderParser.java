package com.grebenkov.parser.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.grebenkov.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Parses orders in json format.
 */
@Component("jsonObjectParser")
public class JsonOrderParser extends AbstractOrderParser {

    private static final Logger LOG = LoggerFactory.getLogger(JsonOrderParser.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ResourceLoader resourceLoader;

    @Value("${order.json.schema.path}")
    private String schemaPath;
    private JsonSchema schema;

    public JsonOrderParser(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws IOException, ProcessingException {
        try(InputStream jsonSchemaIS = resourceLoader.getResource(schemaPath).getInputStream()) {
            schema = JsonSchemaFactory.byDefault().getJsonSchema(objectMapper.readTree(jsonSchemaIS));
        }
    }

    @Override
    protected Result getResult(String line) {
        Result result;
        try {
            final JsonNode json = objectMapper.readTree(line);
            final ProcessingReport report = schema.validate(json);
            if (report.isSuccess()) {
                result = objectMapper.convertValue(json, Result.class);
                result.setProcessingResult(OK);
            } else {
                result = new Result(StreamSupport.stream(report.spliterator(), false)
                                                 .map(ProcessingMessage::getMessage)
                                                 .collect(Collectors.joining(", ")));
            }
        } catch (IOException | ProcessingException e) {
            LOG.error(ERROR_MSG, line, e);
            result = new Result(e.getMessage());
        }
        return result;
    }
}
