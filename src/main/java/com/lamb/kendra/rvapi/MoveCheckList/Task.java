package com.lamb.kendra.rvapi.MoveCheckList;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="task_id")
    private Long id;

    private String description;

    @JsonProperty
    private boolean isCompleted;

    private int orderNum;

    private String notes;

    @ManyToMany(cascade = {CascadeType.ALL})
            @JoinTable(
                    name="task_categories",
                    joinColumns = {@JoinColumn(name="task_id")},
                    inverseJoinColumns = {@JoinColumn(name="category_id")}
            )
    Set<Category> categories;

    public Task() {
        this.isCompleted = false;
        categories = new HashSet<>();
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getNotes() {
        return notes;
    }

    public Task setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Task setCategories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }
}
