package com.codegym.inote.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(targetEntity = UsersRoles.class, fetch = FetchType.EAGER, mappedBy = "role")
    private Set<UsersRoles> usersRoles = new HashSet<>();

    public Role() {
    }

    public Role(String name, Set<UsersRoles> usersRoles) {
        this.name = name;
        this.usersRoles = usersRoles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<UsersRoles> getUsersRoles() {
        return usersRoles;
    }
}
