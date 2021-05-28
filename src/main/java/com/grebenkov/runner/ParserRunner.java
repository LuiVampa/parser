package com.grebenkov.runner;

import com.grebenkov.extensions.FileExtension;
import com.grebenkov.handler.ResultHandler;
import com.grebenkov.parser.OrderParser;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main parser runner.
 */
@Component
public class ParserRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ParserRunner.class);

    private final ResultHandler resultHandler;
    private final Map<FileExtension, OrderParser> parsers;

    public ParserRunner(Map<FileExtension, OrderParser> parsers, ResultHandler resultHandler) {
        this.parsers = parsers;
        this.resultHandler = resultHandler;
    }

    @Override
    public void run(String... args) {
        final Map<FileExtension, List<File>> filesToParse = getFiles(args);

        LOG.info("List of files to parse: {}", Arrays.toString(args));

        filesToParse.forEach((fileExtension, files) -> files.stream()
                                                            .parallel()
                                                            .map(file -> parsers.get(fileExtension).parse(file))
                                                            .flatMap(Collection::stream)
                                                            .forEach(resultHandler::print));
    }

    private static Map<FileExtension, List<File>> getFiles(String[] filePaths) {
        return Arrays.stream(filePaths)
                     .parallel()
                     .map(ParserRunner::getFile)
                     .filter(Objects::nonNull)
                     .collect(Collectors.groupingBy(ParserRunner::getFileExtension,
                                                    Collectors.mapping(file -> file, Collectors.toList())));
    }

    private static File getFile(String filePath) {
        try {
            final File file = Paths.get(filePath).toFile();
            if (!file.exists()) {
                LOG.error("File with path - '{}' doesn't exist!", filePath);
                return null;
            }
            return checkFileExtension(file) ? file : null;
        } catch (InvalidPathException e) {
            LOG.error("Incorrect file path: {}", filePath, e);
        }
        return null;
    }

    private static boolean checkFileExtension(File file) {
        return FileExtension.contains(FilenameUtils.getExtension(file.getName()));
    }

    private static FileExtension getFileExtension(File file) {
        return FileExtension.valueOf(FilenameUtils.getExtension(file.getName()).toUpperCase());
    }
}
