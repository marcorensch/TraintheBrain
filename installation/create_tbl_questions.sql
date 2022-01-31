/*
    Erstellt die Tabelle tbl_questions, überschreibt diese wenn sie bereits existiert.
 */
CREATE OR REPLACE TABLE trainthebrain.tbl_questions (
    question_id int(10) unsigned auto_increment PRIMARY KEY,
    question_text text
 );