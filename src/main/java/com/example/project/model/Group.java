package com.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")
public class Group {
    @Id
    @SequenceGenerator(name = "groupSeqGen", sequenceName = "group_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "groupSeqGen")
    private Long id;

    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "group_name", unique = true)
    private String groupName;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;

    @Column(name = "number_of_members")
    private Long numberOfMembers;

    @Column(name = "group_admin_id")
    private Long groupAdminID;
}
