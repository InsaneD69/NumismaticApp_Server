package com.numismatic_app.server.repository;


import com.numismatic_app.server.entity.CollectionEntity;
import com.numismatic_app.server.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CollectionRepo extends CrudRepository<CollectionEntity,Long> {
    CollectionEntity findByCollectionnameAndUser_id(String collectionName,Long user_id);

    @Modifying
    @Query("DELETE FROM CollectionEntity WHERE collectionid= :collectionid")
    void deleteByCollectionid(@Param("collectionid")Long collectionid);
}
