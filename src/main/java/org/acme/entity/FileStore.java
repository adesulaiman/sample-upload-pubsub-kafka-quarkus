package org.acme.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "filestore", schema = "public")
public class FileStore extends PanacheEntity{
    public String filecontent;
    public LocalDateTime processtime;

    @PrePersist
    public void prePersist() {
        this.processtime = LocalDateTime.now();
    }
}
