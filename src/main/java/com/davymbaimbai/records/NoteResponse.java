package com.davymbaimbai.records;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class NoteResponse {
    private Integer id;
    private String title;
    private String message;
    @DateTimeFormat(pattern = "dd/MM/yyyy - hh:mm")
    private ZonedDateTime timestamp;
}
