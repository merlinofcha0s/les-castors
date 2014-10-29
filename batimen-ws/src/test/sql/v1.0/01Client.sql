insert into Client (email, nom , prenom, login, password, id, numeroTel, dateInscription,  isActive, cleActivation) 
values ('pebron@batimen.fr', 'Pebron' , 'Goui', 'pebron', '$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=', 100000, '0614125698', '2014-01-10',  true, 'lolmdr07');

insert into Client (email, nom , prenom, login, password, id, numeroTel, dateInscription,  isActive, cleActivation) 
values ('raiden@batimen.fr', 'Casaucau' , 'Cyril', 'raiden', '$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=', 100001, '0614125696', '2014-01-08',  true, 'lolmdr06');

insert into Client (email, nom , prenom, login, password, id, numeroTel, dateInscription,  isActive, cleActivation) 
values ('xavier@batimen.fr', 'Dupont' , 'xavier', 'xavier', '$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=', 100002, '0614125696', '2014-01-08',  false, 'NTNkN2RmYzVkNWU2MDZkZjZlYTVjZGQ2ZGE0ZjljY2JhNGJjZWY5MmIxNmNiOWJmMjk2ZDVhNDY3OTEzMTIyZA==');

insert into Permission (typecompte, client_fk) 
values (4, 100000);

insert into Permission (typecompte, client_fk) 
values (4, 100001);

insert into Permission (typecompte, client_fk) 
values (4, 100002);