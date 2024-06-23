package com.davymbaimbai.service;

import com.davymbaimbai.mapper.NoteMapper;
import com.davymbaimbai.records.NoteRequest;
import com.davymbaimbai.records.NoteResponse;
import com.davymbaimbai.repository.NoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper mapper;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    SimpleModule module = new SimpleModule();


    private static final Logger log = LoggerFactory.getLogger(NoteService.class);

    public Integer saveNote(NoteRequest request) throws JsonProcessingException {
        var note = noteRepository.save(mapper.toNote(request));
        log.info("Response: {}", objectMapper.writeValueAsString(note));
        String response = pushNoteToThirdPartyServer(mapper.fromNote(note));
        log.info("Third party response: {}", objectMapper.writeValueAsString(response));
        return note.getId();
    }
    

    public List<NoteResponse> findAllNotesSortedByTimestamp(String tz, String order) {
        var zoneId = ZoneId.of(tz);
        List<NoteResponse> notes = noteRepository.findAll()
                .stream()
                .map(mapper::fromNote)
                .map(note -> convertToSpecifiedTZ(note, zoneId))
                .collect(Collectors.toList());

        if ("desc".equalsIgnoreCase(order)) {
            notes.sort(Comparator.comparing(NoteResponse::getTimestamp).reversed());
        } else {
            notes.sort(Comparator.comparing(NoteResponse::getTimestamp));
        }

        return notes;
    }

    private NoteResponse convertToSpecifiedTZ(NoteResponse note, ZoneId zoneId) {
        var convertedZoneDateTime =
                ZonedDateTime.of(note.getTimestamp().toLocalDateTime(), zoneId)
                        .withZoneSameInstant(ZoneOffset.UTC);
        note.setTimestamp(convertedZoneDateTime);
        return note;
    }

    public NoteResponse findNoteById(Integer noteId, String tz) {
        NoteResponse noteResponse = noteRepository.findById(noteId)
                .map(mapper::fromNote)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No note found with the provided ID: %d", noteId)));
        if (tz != null && !tz.isEmpty()) {
            ZoneId zoneId = ZoneId.of(tz);
            ZonedDateTime zonedDateTime = noteResponse.getTimestamp();
            noteResponse.setTimestamp(ZonedDateTime.from(zonedDateTime.toLocalDateTime()));
        }

        return noteResponse;
    }

    private String pushNoteToThirdPartyServer(NoteResponse noteResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return webClient.post()
                .uri("/save")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(noteResponse))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
