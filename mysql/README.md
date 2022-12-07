# REPLICATION MASTER-SLAVE MYSQL
Soit 192.168.1.4, l’ip du master, et 192.168.1.5, celle du slave.
1)	Install mysql sur les deux serveurs : apt install mysql-server
2)	SUR LE SERVEUR SLAVE : mettre l’ip du master ou « * » dans « bind-address » du fichier /etc/mysql/mysql.conf.d/mysqld.cnf \
      SUR LE SERVEUR MASTER : \
      Uncomment les lignes error_log, log-bin, server-id, bin-do-db et remplacer la valeur du dernier paramètre par la bdd à répliquer.
1)	On créer le user slave sur le serveur master\
      a.	Root#~ mysql\
      b.	CREATE USER ‘slave’@’192.168.1.5’ IDENTIFIED BY ‘nccfisa’ ;
2)	Donner les droits de replication au user slave tjs sur le master :\
      Grant replication slave on *.* to ‘slave’@’192.168.1.5’ ;\
      FLUSH PRIVILEGES ;
3)	Créer les fichiers de logs /var/log/mysql/mysql-error.log et /var/log/mysql/mysql-bin.log et donner le owning à mysql sur le dossier  /var/log/mysql (en recurse)
4)	Bloquer l’écriture sur la bdd à répliquer : \
      mysqldump --databases epi_sport > epi_sport.sql
5)	Envoyer le fichier créer dans la machine slave : \
      scp epi_sport.sql ncc@192.168.1.5:/tmp
6)	SUR LE SERVEUR SLAVE : importer la bdd \
      mysql < /tmp/epi_sport.sql
7)	Lancer mysql et lancer cette requête pour préciser le master.
      stop slave;
      change master to master_host=’192.168.1.4’, master_user='slave', master_password='nccfisa', master_log_file='mysql-bin.000001', master_log_pos=1746; \
      start slave;
