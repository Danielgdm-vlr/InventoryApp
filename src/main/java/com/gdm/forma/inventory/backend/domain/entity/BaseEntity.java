package com.gdm.forma.inventory.backend.domain.entity;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
}

