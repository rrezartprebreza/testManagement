package com.backend.testManagement.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class TestModel {

    @Id
    @Column(name = "id")
    private String id= UUID.randomUUID().toString();

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;
}
