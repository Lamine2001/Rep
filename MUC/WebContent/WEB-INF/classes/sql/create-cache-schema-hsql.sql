create table CachedActivity (
    objectId numeric(10)
    , id varchar(255)
    , projectObjectId numeric(10)
    , projectId varchar(255)
    , wbsObjectId numeric(10)
    , status varchar(255)
    , startDate timestamp
    , actualStartDate timestamp
    , finishDate timestamp
    , actualFinishDate timestamp
    , netzplanNummer varchar(255)
    , netzplanVorgang varchar(255)
);
create index ix_netzplan on CachedActivity (netzplanNummer, netzplanVorgang);
