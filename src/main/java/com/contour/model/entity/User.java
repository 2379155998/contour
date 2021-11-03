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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Integer upTraffic;
    private Integer downTraffic;
    private Integer traffic;

    @OneToOne
    private VMessUser vmessUser;
    @OneToOne
    private ShadowsocksUser shadowsocksUser;
    @OneToOne
    private TrojanUser trojanUser;
    @OneToOne
    private VLESSUser vlessUser;

    @ManyToMany
    @ToString.Exclude
    private List<Server> servers;
    @ManyToMany
    @ToString.Exclude
    private List<Inbound> inbounds;
    @ManyToOne
    private DataPlan dataPlan;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

