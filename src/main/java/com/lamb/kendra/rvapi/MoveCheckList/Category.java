package com.lamb.kendra.rvapi.MoveCheckList;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @OrderBy
    @Column(name = "category", unique = true)
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    Set<Task> tasks;

    public Long getId() {
        return id;
    }

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Category setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Category setTasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }
}
