package com.davymbaimbai.mapper;

import com.davymbaimbai.entity.Note;
import com.davymbaimbai.records.NoteRequest;
import com.davymbaimbai.records.NoteResponse;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;

@Service
public class NoteMapper {
    public Note toNote(NoteRequest request) {
        if (request == null) {
            return null;
        }
        return Note.builder()
                .id(request.id())
                .title(request.title())
                .message(request.message())
                .timestamp(ZonedDateTime.now())
                .build();
    }

    public NoteResponse fromNote(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getMessage(),
                note.getTimestamp()
        );
    }
}
