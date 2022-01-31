# NDS HF SemesterArbeit 1
## Multiple-Choice Lern- und Prüfungsplattform (Train the Brain)
<p>Ein Tool mit dem Multiple Choice Fragen in eine Datenbank abgefüllt und verwaltet werden kann.
Das Tool stellt die eingetragenen Fragen in zufälliger Reihenfolge und die Antworten werden auf ihre Richtigkeit
bearbeitet.</p>

# Installation
## Hinweis
Bei diesem Projekt handelt es sich um ein IntelliJ Projekt.
Für die Verwendung wird eine lokale Installation der MariaDB sowie die Java JDK 17 vorausgesetzt. MariaDB kann unter MacOS mittels Homebrew installiert werden.

### Installation Datenbank unter MacOS
Eine Anleitung zur Installation der MariaDB mittels Homebrew findest du hier:
https://mariadb.com/kb/en/installing-mariadb-on-macos-using-homebrew/

### Installation Datenbank unter Windows
EIne Anleitung zur Installation der MariaDB auf Windows Systemen findest du hier:
https://mariadb.com/kb/en/installing-mariadb-msi-packages-on-windows/

## Konfiguration Datenbank
### 1.Anlegen der Datenbank
Wenn die MariaDB installiert wurde und läuft, kann über den folgenden SQL Befehl die benötigte Datenbank erstellt werden:

`CREATE OR REPLACE DATABASE trainthebrain;`

Hinweis: der oben gezeigte Befehl löscht eine vorhandene Datenbank mit dem Namen trainthebrain, sollte eine solche existieren!

### 2. Benutzer (Optional)
Das Programm benötigt einen eigenen Benutzer mit Zugriff auf die oben genannte Datenbank, damit diese out-of-the-box verwendet werden kann. Du kannst aber auch einen eigenen Benutzeraccount nutzen. Hierzu musst du aber die Verbindungsdaten in der Applikation anpassen.

#### 2.1 Anlegen des Benutzers (optional)
Standardmässig lautet der Benutzername sowie das Passwort **trainthebrain**.
Der folgende SQL Befehl erstellt einen neuen Benutzer mit Zugriff auf die Datenbank _trainthebrain_:

`CREATE USER 'trainthebrain'@localhost IDENTIFIED BY 'trainthebrain';`

#### 2.2. Erteilen der Berechtigung (optional)
Damit der Benutzer in der App verwendet werden kann, muss dieser über die nötigen Berechtigungen verfügen, diese erteilen wir mit dem SWL Befehl:

``GRANT ALL privileges ON `trainthebrain`.* TO 'trainthebrain'@localhost;``

Hinweis: Punkt 2 ist optional und dienen dazu, dass die App "out-of-the-box" verwendet werden kann, du kannst aber auf das Anlegen eines eigenen Benutzers verzichten und stattdessen die Verbindungsparameter der App selbst anpassen.
Diese findest du in der Datei SQLConnectionData im package sqlcontroller.

### 3.Erstellen der benötigten Tabellen
Führe die beiden untenstehenden SQL-Befehle aus, damit die nötigen Tabellen erstellt werden.

__Tabelle für Fragen:__

````
CREATE OR REPLACE TABLE trainthebrain.tbl_questions (
question_id int(10) unsigned auto_increment PRIMARY KEY,
question_text text
);
````

__Tabelle für Antworten:__

````
CREATE OR REPLACE TABLE trainthebrain.tbl_answers (
answer_id int(10) unsigned auto_increment PRIMARY KEY,
question_id int(10) unsigned,
answer_correct BOOL default 0,
answer_text text,
CONSTRAINT FK_QUESTIONID FOREIGN KEY (question_id)
REFERENCES tbl_questions(question_id)
);
````

__Alle SQL Scripts sind im Ordner _installation_ zusätzlich für dich abgelegt__
<br>Zusätzlich ist eine detailierte Verwendungsanleitung diesem Repository beigelegt.
