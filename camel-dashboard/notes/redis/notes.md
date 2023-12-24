docker exec -it test-redis

> KEYS *
> MSET test "Test str"
> MGET test
> exit