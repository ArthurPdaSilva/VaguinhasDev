package com.vaguinhasdev.jobs.adapter.out.collector;

import java.util.Arrays;
import java.util.List;

record ConfiguredBoard(String company, String identifier) {

    static List<ConfiguredBoard> parse(String configuration) {
        if (configuration == null || configuration.isBlank()) return List.of();

        return Arrays.stream(configuration.split(";"))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(ConfiguredBoard::parseBoard)
                .toList();
    }

    private static ConfiguredBoard parseBoard(String value) {
        String[] parts = value.split("=", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException(
                    "Board inválido: use o formato Empresa=identificador;Outra=identificador"
            );
        }
        return new ConfiguredBoard(parts[0].trim(), parts[1].trim());
    }
}
