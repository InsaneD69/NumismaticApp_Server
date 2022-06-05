package com.numismatic_app.server.repository;


import com.numismatic_app.server.entity.CollectionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepo extends CrudRepository<CollectionEntity,Long> {


}
