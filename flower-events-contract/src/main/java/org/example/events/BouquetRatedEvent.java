package org.example.events;

import java.io.Serializable;

public record BouquetRatedEvent(
        Long bouquetId,
        Integer popularityScore,
        String popularityLevel,
        String recommendation
) implements Serializable {}
