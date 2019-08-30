package com.codegym.inote.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "username must be fill")
    @Size(min = 6, max = 15, message = "username length must be between 6 and 15")
    private String username;

    @NotEmpty(message = "password must be fill")
    @Size(min = 6, message = "password length must be at least 6 characters")
    private String password;

    @OneToMany(targetEntity = Note.class)
    List<Note> notes;

    @OneToMany(targetEntity = NoteType.class)
    List<NoteType> noteTypes;

    @OneToMany(targetEntity = Stack.class)
    List<Stack> stacks;

    @OneToMany(targetEntity = Tag.class)
    List<Stack> tags;

    @OneToMany(targetEntity = Trash.class)
    private List<Trash> trashes;

    public List<Trash> getTrashes() {
        return trashes;
    }

    public void setTrash(List<Trash> trashes) {
        this.trashes = trashes;
    }

    public List<Stack> getTags() {
        return tags;
    }

    public void setTags(List<Stack> tags) {
        this.tags = tags;
    }

    public List<Stack> getStacks() {
        return stacks;
    }

    public void setStacks(List<Stack> stacks) {
        this.stacks = stacks;
    }

    public List<NoteType> getNoteTypes() {
        return noteTypes;
    }

    public void setNoteTypes(List<NoteType> noteTypes) {
        this.noteTypes = noteTypes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
