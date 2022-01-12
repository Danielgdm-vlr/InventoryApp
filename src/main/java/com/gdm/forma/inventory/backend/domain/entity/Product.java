package com.gdm.forma.inventory.backend.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "product")

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@ToString
public class Product extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "stockInSalon")
    private Integer stockInSalon;

    @NonNull
    @Column(name = "stockPerWeek")
    private Integer stockPerWeek;
}
