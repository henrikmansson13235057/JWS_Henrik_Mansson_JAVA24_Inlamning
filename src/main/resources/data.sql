-- Initiera saker med testdata
INSERT INTO sak (name, typ, total_quantity, available_quantity) VALUES ('Cykel', 'Fordon', 5, 5);
INSERT INTO sak (name, typ, total_quantity, available_quantity) VALUES ('Stol', 'Möbel', 10, 10);
INSERT INTO sak (name, typ, total_quantity, available_quantity) VALUES ('Projektor', 'Elektronik', 2, 2);

-- Lägg till några testpersoner
INSERT INTO person (namn) VALUES ('Anna Andersson');
INSERT INTO person (namn) VALUES ('Bengt Bengtsson');
INSERT INTO person (namn) VALUES ('Cecilia Carlsson');
