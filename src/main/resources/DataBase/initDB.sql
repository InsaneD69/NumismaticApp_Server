CREATE TABLE IF NOT EXISTS collection
(
    collectionid    BIGSERIAL PRIMARY KEY ,
    username  VARCHAR(200) NOT NULL ,
    collectionname VARCHAR(200)  NOT NULL ,
    placehash VARCHAR(254)
);