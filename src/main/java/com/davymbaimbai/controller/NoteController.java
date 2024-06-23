package com.davymbaimbai.controller;

import com.davymbaimbai.records.NoteRequest;
import com.davymbaimbai.records.NoteResponse;
import com.davymbaimbai.service.NoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/save")
    public ResponseEntity<Integer> saveNote(
            @RequestBody @Valid NoteRequest request
    ) throws JsonProcessingException {
        return ResponseEntity.ok(noteService.saveNote(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<NoteResponse>> findAllNotes(
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "tz") String tz
    ) {
        return ResponseEntity.ok(noteService.findAllNotesSortedByTimestamp(tz, order));
    }

    @GetMapping("/{note-id}")
    public ResponseEntity<NoteResponse> findNoteById(
            @PathVariable("note-id") Integer noteId,
            @RequestParam(value = "tz", required = false) String tz
    ) {
        return ResponseEntity.ok(noteService.findNoteById(noteId, tz));
    }
}
