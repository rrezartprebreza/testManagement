package com.backend.testManagement.model;



import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test")
@Entity
@Builder
public class Test {

    @Id
    @Column(name = "id")
    private final String id= UUID.randomUUID().toString();

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;
}
