package com.NumismaticApp.Server.NumismaticApp.repository;


import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepo extends CrudRepository<CollectionEntity,Long> {

    CollectionEntity findByCollectionname(String collectionname);


}
