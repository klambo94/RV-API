package com.lamb.kendra.rvapi.MoveCheckList;

import javax.persistence.*;

@Entity
@Table(name = "task")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="task_id")
    private Long id;

    private String description;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
