#
CREATE PROCEDURE insertRandomPanelsEntity()
BEGIN
DECLARE i INT;
SET i = 1;
START TRANSACTION;
WHILE i <= 200000 DO
INSERT INTO lb.panel_entity (created_date,last_modified_date) VALUES(CURRENT_DATE,CURRENT_DATE);
SET i = i + 1;
END WHILE;
COMMIT;
END;
#