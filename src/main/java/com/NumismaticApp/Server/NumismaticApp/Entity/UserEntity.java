package com.NumismaticApp.Server.NumismaticApp.Entity;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<CollectionEntity> collectionEntities;

   // @Transient
   // private String passwordConfirm;
    //@ManyToMany(fetch = FetchType.EAGER)
   // private Set<Role> roles;

    public CollectionEntity getCollectionByCollectionName(String collectionname){

       return (CollectionEntity)collectionEntities.stream().filter(collection-> collection.getCollectionname()==collectionname);

    }




}
