## Installation de HiveMQ

git clone https://github.com/hivemq/hivemq-community-edition.git
cd hivemq-community-edition
./gradlew clean hivemqZip
unzip hivemq-ce-2023.3.zip
ln -s /opt/hivemq-ca-2023.3 /opt/hivemq
useradd -d /opt/hivemq hivemq
chown -R hivemq:hivemq /opt/hivemq-ce-2023.3
chown -R hivemq:hivemq /opt/hivemq
chmod +x ./bin/run.sh
cp /opt/hivemq/bin/init-script/hivemq-debian /etc/init.d/hivemq
cp /opt/hivemq-ce-2023.3/bin/init-script/hivemq-debian /etc/init.d/hivemq
chmod +x /etc/init.d/hivemq
cp /opt/hivemq-ce-2023.3/bin/init-script/hivemq.service /etc/systemd/system/hivemq.service
cp /opt/hivemq-ce-2023.3/bin/init-script/hivemq /etc/init.d/hivemq
chmod +x /etc/init.d/hivemq
update-rc.d hivemq defaults
systemctl enable hivemq
apt install openjdk-11*
./bin/run.sh &


