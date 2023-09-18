#!/bin/bash

while IFS='=' read -r key value
do
    key=$(echo $key)
    eval ${key}=\${value}
done < "db.properties"

echo "MySQL username: $user"
echo "MySQL hostname: $host"
echo " Database name: $name"

[ -z $name ] && echo "db.properties is not valid" && exit 1

echo ""
echo "[*] Creating database '$name'"
mysql -h $host -P 3306 -u $user -p$password -e "DROP DATABASE IF EXISTS $name; CREATE DATABASE $name;"

echo "[*] Creating tables"
mysql -h $host -P 3306 -u $user -p$password $name < resources/create-database-script.sql

echo "[*] Inserting test users"
mysql -h $host -P 3306 -u $user -p$password $name < resources/insert-test-users.sql

echo "[*] Inserting test job roles"
mysql -h $host -P 3306 -u $user -p$password $name < resources/insert-test-job-roles.sql
