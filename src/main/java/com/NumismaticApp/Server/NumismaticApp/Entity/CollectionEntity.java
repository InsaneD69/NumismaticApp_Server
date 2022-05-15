package com.NumismaticApp.Server.NumismaticApp.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "collection")
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="collectionid")
    private Long collectionid;

    @Column(name="collectionname")
    private String collectionname;

    @Column(name="placehash")
    private String placehash;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
