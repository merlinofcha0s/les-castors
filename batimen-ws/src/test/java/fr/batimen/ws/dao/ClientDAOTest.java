package fr.batimen.ws.dao;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.transaction.VoidRunnable;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.ws.AbstractBatimenTest;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.helper.HashHelper;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
public class ClientDAOTest extends AbstractBatimenTest {

	@ObjectUnderTest
	public ClientDAO clientDAO;

	final Client clientToRec = new Client();

	final Calendar cal = Calendar.getInstance(Locale.FRANCE);

	// Creation d'un user de test dans la BDD
	@Before
	public void init() throws Exception {

		cal.set(Calendar.DAY_OF_MONTH, 13);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.YEAR, 2014);

		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 01);
		cal.set(Calendar.SECOND, 00);

		clientToRec.setEmail("lol@lol.com");
		clientToRec.setLogin("pebron");
		clientToRec.setPassword(HashHelper.hashString("lollollol"));
		clientToRec.setCivilite(Civilite.MONSIEUR);
		clientToRec.setPrenom("Pebron");
		clientToRec.setNom("De la Pebronne");
		clientToRec.setNumeroTel("0615125645");
		clientToRec.setDateInscription(cal.getTime());

		// On ouvre une transaction avec la BDD
		try {
			transactionHelper.executeInTransaction(new VoidRunnable() {
				@Override
				public void doRun(EntityManager entityManager) throws Exception {
					// On persiste un utilisateur dans la bdd
					entityManager.persist(clientToRec);
				}
			});
		} catch (Exception e) {
			throw new Exception();
		}
	}

	@Test
	public void testLogin() {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebron");
		toLogin.setPassword("lollollol");

		// Appel du service qui check le login
		ClientDTO user = clientDAO.login(toLogin);

		// Verification des infos
		assertTrue(user.getLogin().equals("pebron"));
		assertTrue(HashHelper.check(toLogin.getPassword(), user.getPassword()));
		assertTrue(user.getEmail().equals("lol@lol.com"));
		assertTrue(user.getNumeroTel().equals("0615125645"));
		assertTrue(user.getCivilite() == Civilite.MONSIEUR);
		assertTrue(user.getPrenom().equals("Pebron"));
		assertTrue(user.getNom().equals("De la Pebronne"));

		Calendar calInscription = Calendar.getInstance(Locale.FRANCE);
		calInscription.setTime(user.getDateInscription());

		assertTrue(calInscription.get(Calendar.DAY_OF_WEEK) == cal.get(Calendar.DAY_OF_WEEK));

	}

	@Test
	public void testLoginFail() throws Exception {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebron");
		toLogin.setPassword("lollol");

		// Appel du service qui check le login
		ClientDTO user = clientDAO.login(toLogin);

		// Verification que rien n'est renvoyer ce qui veut dire que la
		// combinaison login / mdp n'est pas bonne ou que l'utilisateur n'existe
		// pas
		assertTrue(user.getLogin().equals(""));
		assertTrue(user.getPassword().equals(""));
		assertTrue(user.getEmail().equals(""));
	}
}
