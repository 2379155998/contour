package com.contour.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class DataPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer level;

    @ManyToMany
    @ToString.Exclude
    private List<Inbound> inbounds;
    @OneToMany
    @ToString.Exclude
    private List<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DataPlan dataPlan = (DataPlan) o;
        return id != null && Objects.equals(id, dataPlan.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
