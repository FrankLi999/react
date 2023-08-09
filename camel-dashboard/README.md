# Postgresql setup
used with wcm-bpm project

```
cd notes\postgresql
docker network create -d bridge wcm-network
docker volume create --name postgres-data -d local
docker volume create --name pgadmin-data -d local
docker-compose up -d
```

pgadmin: 
    http://localhost:18181

    admin@admin.com/admin

    > add a new server:

        connection:
            host: postgres
            port:5432
            db: postgres_db 
            user: postgres
            password: 123456