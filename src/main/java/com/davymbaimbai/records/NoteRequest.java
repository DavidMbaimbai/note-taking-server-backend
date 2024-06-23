package com.davymbaimbai.records;

import java.time.ZonedDateTime;

public record NoteRequest(
        Integer id,
        String title,
        String message
) {
}
