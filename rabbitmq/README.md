# paas-epi-sport
* docker command to run rabbitmq container : \
  docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management &
* add user and permissions : 
    - rabbitmqctl add_user test test
    - rabbitmqctl set_user_tags test administrator
    - rabbitmqctl set_permissions -p / test ".*" ".*" ".*"
