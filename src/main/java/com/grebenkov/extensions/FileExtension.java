package com.grebenkov.extensions;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Supported file extensions.
 */
public enum FileExtension {
    JSON,
    CSV;

    private static final Set<String> supportedExtensions = Arrays.stream(FileExtension.values())
                                                          .map(FileExtension::name)
                                                          .collect(Collectors.toSet());

    public static boolean contains(String extension) {
        return supportedExtensions.contains(extension.toUpperCase());
    }
}
