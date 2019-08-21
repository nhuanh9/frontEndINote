package com.codegym.inote.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stack")
public class Stack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(targetEntity = NoteType.class)
    private List<NoteType> noteTypes;

    public Stack() {
    }

    public Stack(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NoteType> getNoteTypes() {
        return noteTypes;
    }

    public void setNoteTypes(List<NoteType> noteTypes) {
        this.noteTypes = noteTypes;
    }
}
