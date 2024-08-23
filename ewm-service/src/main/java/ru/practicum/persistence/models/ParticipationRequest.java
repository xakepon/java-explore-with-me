package ru.practicum.persistence.models;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.common.enums.ParticipationStatus;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "participation")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "status", length = 30)
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;
}
