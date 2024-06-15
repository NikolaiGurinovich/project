package com.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
