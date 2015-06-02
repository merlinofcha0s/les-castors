INSERT INTO Entreprise (id, nomcomplet, statutjuridique, siret, datecreation, nbemployees, adresse_id)
VALUES (100007, 'Entreprise de toto', 0, '43394298400017', '2014-03-23 22:00:00.0', 20, 100005);

INSERT INTO Entreprise (id, nomcomplet, statutjuridique, siret, datecreation, nbemployees, adresse_id)
VALUES (100008, 'Entreprise de roberta', 0, '43394298400017', '2010-02-23 14:00:00.0', 10, 100007);

INSERT INTO categoriemetier (id, categoriemetier, entreprise_fk)
VALUES (100001, 0, 100007);

INSERT INTO categoriemetier (id, categoriemetier, entreprise_fk)
VALUES (100002, 0, 100008);