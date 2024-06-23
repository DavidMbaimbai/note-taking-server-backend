package com.davymbaimbai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String message;
    @DateTimeFormat(pattern = "dd/MM/yyyy - hh:mm")
    private ZonedDateTime timestamp;
}
