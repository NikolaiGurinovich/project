package com.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "l_user_group")
public class LinkUserGroup {
    @Id
    @SequenceGenerator(name = "lUserGroupSeqGen", sequenceName = "l_user_group_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "lUserGroupSeqGen")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;
}
