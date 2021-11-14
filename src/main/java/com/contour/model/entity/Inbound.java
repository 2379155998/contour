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
public class Inbound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tag;

    private int level;
    private int alterId;
    private boolean secureEncryptionOnly;

    private boolean AcceptProxyProtocol;
    private String path;
    @OneToMany
    @ToString.Exclude
    private List<Header> headers;
    private int maxEarlyData;
    private boolean useBrowserForwarding;
    private String earlyDataHeaderName;

    @ManyToOne
    private Server server;
    @ManyToMany
    @ToString.Exclude
    private List<User> users;
    @ManyToMany
    @ToString.Exclude
    private List<DataPlan> dataPlans;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Inbound inbound = (Inbound) o;
        return id != null && Objects.equals(id, inbound.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

