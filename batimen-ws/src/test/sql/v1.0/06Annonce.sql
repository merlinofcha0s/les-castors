INSERT INTO Annonce (id, datecreation, datemaj, delaiintervention, description, etatannonce, categoriemetier, souscategoriemetier, nbconsultation, typecontact, hashID, selHashID, typeTravaux, adressechantier_id, demandeur_fk, notationannonce_id, entreprise_selectionnee_fk)
VALUES
  (100010, '2014-01-10', '2014-01-10', '0', 'Construction compliqué qui necessite des connaissance en geologie', '0', 0,
   'Tableaux électriques', '0', 0, 'toto', 'tata', 0, 100005, 100001, 100010, NULL);
INSERT INTO Annonce (id, datecreation, datemaj, delaiintervention, description, etatannonce, categoriemetier, souscategoriemetier, nbconsultation, typecontact, hashID, selHashID, typetravaux, adressechantier_id, demandeur_fk, notationannonce_id, entreprise_selectionnee_fk)
VALUES
  (100011, '2014-01-11', '2014-01-12', '0', 'Construction de lol mdr', '2', 1, 'Petit travaux', '0', 0, 'titi', 'tete',
   0, 100006, 100001, 100009, 100008);