/*
    Diese SQL Befehle erstellen die Datenbank sowie den von der App genutzten User und vergibt die nötigen Privilegien.
 */

/* Datenbank anlegen / überschreiben */

CREATE OR REPLACE DATABASE trainthebrain;

/* User erstellen */
CREATE USER 'trainthebrain'@localhost IDENTIFIED BY 'trainthebrain';

/* Userberechtigungen für die Datenbank trainthebrain setzen */
GRANT ALL privileges ON `trainthebrain`.* TO 'trainthebrain'@localhost;
