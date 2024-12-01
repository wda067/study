CREATE DATABASE mydb;
       
GRANT ALL PRIVILEGES ON mydb.* TO 'myuser'@'%';

FLUSH PRIVILEGES;

