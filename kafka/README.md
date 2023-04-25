L'installation de kafka se fait en deux parties : l'installation de zookeeper, qui gère
le cluster, puis d'un broker kafka.

# Zookeeper
Nous avons fait le choix d'installer Zookeeper en tant que service Linux. Pour cela, nous avons
d'abord téléchargé l'archive zookeeper depuis le site officiel de zookeeper :
wget https://dlcdn.apache.org/zookeeper/zookeeper-3.8.1/apache-zookeeper-3.8.1-bin.tar.gz
Puis nous avons décompressé l'archive dans le dossier /opt :
tar -xvf zookeeper-3.4.14.tar.gz -C /opt
Nous avons ensuite créé un lien symbolique vers le dossier zookeeper-3.4.14 :
ln -s /opt/zookeeper-3.4.14 /opt/zookeeper
Nous avons ensuite créé un fichier de configuration zoo.cfg dans le dossier /opt/zookeeper/conf :
tickTime=2000
dataDir=/var/lib/zookeeper
clientPort=2181
initLimit=5
syncLimit=2

Nous avons ensuite créé un dossier /var/lib/zookeeper pour stocker les données de zookeeper.
Nous avons ensuite créé un fichier de configuration zookeeper.service dans le dossier /etc/systemd/system :

'''bash
    [Unit]
    Description=ZooKeeper Service
    Documentation=http://zookeeper.apache.org
    Requires=network.target
    After=network.target
    
    [Service]
    Type=forking
    User=zookeeper
    Group=zookeeper
    ExecStart=/opt/zookeeper/bin/zkServer.sh start /opt/zookeeper/conf/zookeeper.conf
    ExecStop=/opt/zookeeper/bin/zkServer.sh stop /opt/zookeeper/conf/zookeeper.conf
    ExecReload=/opt/zookeeper/bin/zkServer.sh restart /opt/zookeeper/conf/zookeeper.conf
    WorkingDirectory=/var/lib/zookeeper
    
    [Install]
    WantedBy=default.target
'''
Nous avons ensuite démarré le service zookeeper :
systemctl start zookeeper
Nous avons ensuite vérifié que le service zookeeper était bien démarré :
systemctl status zookeeper
Nous avons ensuite vérifié que le port 2181 était bien ouvert :
netstat -tulpn | grep 2181

# Kafka

Nous avons ensuite installé kafka en tant que conteneur Docker :
docker run -d  --network="host" --name kafka -p 9092:9092   
    --env KAFKA_ADVERTISED_HOST_NAME=localhost  
    --env KAFKA_ZOOKEEPER_CONNECT=localhost:2181   wurstmeister/kafka
On s'y connecte ensuite :
docker exec -it 2720d7ce10ac /bin/sh

On créé les topics emergency, coach1, coach2, coach3, notif et hrdata:
 kafka-topics.sh --bootstrap-server localhost:9092 --topic emergency-topic --create --partitions 1 --replication-factor 1


