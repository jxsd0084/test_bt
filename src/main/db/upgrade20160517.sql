ALTER TABLE level_two_fields ADD creator varchar(255);

ALTER TABLE level_two_fields ADD modifier varchar(255);

ALTER TABLE level_two_fields ADD status int(12);

ALTER TABLE m99_fields ADD creator varchar(255);

ALTER TABLE m99_fields ADD modifier varchar(255);

ALTER TABLE m99_fields ADD status int(12);

ALTER TABLE level_one_fields ADD creator varchar(255);

ALTER TABLE level_one_fields ADD modifier varchar(255);

ALTER TABLE level_one_fields ADD status int(12);

UPDATE level_one_fields SET creator="admin",modifier="admin",status =1;

UPDATE level_two_fields SET creator="admin",modifier="admin",status =1;

UPDATE m99_fields SET creator="admin",modifier="admin",status =1;
