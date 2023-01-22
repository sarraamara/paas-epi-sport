# paas-epi-sport
* docker command to run rabbitmq container : \
  docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management &
* add user and permissions : 
    - rabbitmqctl add_user test test
    - rabbitmqctl set_user_tags test administrator
    - rabbitmqctl set_permissions -p / test ".*" ".*" ".*"

* Cluster:
    - docker run --name rabbit1 --hostname rabbit1 -d --network host -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=ncc -e RABBITMQ_DEFAULT_PASS=nccfisa -e RABBITMQ_ERLANG_COOKIE=rabbitmq-cluster-cookie rabbitmq:3.11-management
    - docker run --name rabbit1 --hostname rabbit1 -d --network host -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=ncc -e RABBITMQ_DEFAULT_PASS=nccfisa -e RABBITMQ_ERLANG_COOKIE=rabbitmq-cluster-cookie rabbitmq:3.11-management
    - Ajoutez dans /etc/hosts des deux noeuds: 
        192.168.1.2 rabbit1
        192.168.1.13 rabbit2
    Sur rabbit1:
    - rabbitmqctl stop_app
    - rabbitmqctl join_cluster rabbit@rabbit2
    - rabbitmqctl start_app

    