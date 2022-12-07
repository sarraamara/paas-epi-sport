# paas-epi-sport
docker command to run redis container :
docker run --name myredis -v /home/redis/redis.conf:/usr/local/etc/redis/redis.conf -p 6379:6379 -d redis
