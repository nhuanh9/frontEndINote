package com.codegym.inote.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @OneToMany(targetEntity = UsersRoles.class, mappedBy = "users"
            , fetch = FetchType.EAGER)
    private Set<UsersRoles> usersRoles = new HashSet<>(0);

    @JsonIgnore
    @OneToMany(targetEntity = Note.class)
    List<Note> notes;

    @JsonIgnore
    @OneToMany(targetEntity = NoteType.class)
    List<NoteType> noteTypes;

    @JsonIgnore
    @OneToMany(targetEntity = Stack.class)
    List<Stack> stacks;

    @JsonIgnore
    @OneToMany(targetEntity = Tag.class)
    List<Stack> tags;

    @JsonIgnore
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

    public User(String username, String password, boolean enabled, Set<UsersRoles> usersRoles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.usersRoles = usersRoles;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UsersRoles> getUsersRoles() {
        return usersRoles;
    }

    public void setUsersRoles(Set<UsersRoles> usersRoles) {
        this.usersRoles = usersRoles;
    }

    @Transient
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (UsersRoles usersRoles : this.usersRoles) {
            authorities.add(new SimpleGrantedAuthority(usersRoles.getRole().getName()));
        }
        return authorities;
    }


}
