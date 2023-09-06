package com.asap.server.domain;

import com.asap.server.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserV2 extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private MeetingV2 meeting;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "false")
    private Boolean isFixed;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    private UserV2(MeetingV2 meeting, String name, Role role) {
        this.meeting = meeting;
        this.name = name;
        this.role = role;
    }

    public static UserV2 of(MeetingV2 meeting, String name, Role role) {
        return new UserV2(meeting, name, role);
    }
}
