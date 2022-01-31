/*
    Erstellt die Tabelle tbl_answers, überschreibt diese wenn sie bereits existiert.
    Hinweis: Tabelle tbl_questions muss zuerst erstellt werden!
 */
CREATE OR REPLACE TABLE trainthebrain.tbl_answers (
    answer_id int(10) unsigned auto_increment PRIMARY KEY,
    question_id int(10) unsigned,
    answer_correct BOOL default 0,
    answer_text text,
    CONSTRAINT FK_QUESTIONID FOREIGN KEY (question_id) REFERENCES tbl_questions(question_id)
 );