package com.contour.repository;

import com.contour.model.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server,Long> {
    Server findServerByIp(String ip);
}
