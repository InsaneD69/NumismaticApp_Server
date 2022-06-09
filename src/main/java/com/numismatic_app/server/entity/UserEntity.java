package com.numismatic_app.server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "clients")
public class UserEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;


    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "user")
    private List<CollectionEntity> collectionEntities;

    public List<CollectionEntity> getCollectionEntities() {
        return collectionEntities;
    }



}
