package fr.batimen.ws.facade;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ClientService;
import fr.batimen.ws.entity.Client;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
public class GestionClientFacadeTest extends AbstractBatimenWsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionClientFacadeTest.class);

	private final Client clientToRec = new Client();

	private final Calendar cal = Calendar.getInstance(Locale.FRANCE);

	// Creation d'un user de test dans la BDD
	@Before
	public void init() throws Exception {

		utx.begin();
		entityManager.joinTransaction();

		cal.set(Calendar.DAY_OF_MONTH, 13);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.YEAR, 2014);

		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 01);
		cal.set(Calendar.SECOND, 00);

		clientToRec.setEmail("lol@lol.com");
		clientToRec.setLogin("pebron");
		clientToRec.setPassword("lollollol");
		clientToRec.setCivilite(Civilite.MONSIEUR);
		clientToRec.setPrenom("Pebron");
		clientToRec.setNom("De la Pebronne");
		clientToRec.setNumeroTel("0615125645");
		clientToRec.setDateInscription(cal.getTime());
		clientToRec.setIsArtisan(false);

		// On persiste un utilisateur dans la bdd
		entityManager.persist(clientToRec);

		utx.commit();
		// clear the persistence context (first-level cache)
		entityManager.clear();

	}

	@Test
	public void testGetClientForLogin() {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebron");
		toLogin.setPassword("lollollol");

		// Appel du service qui check le login
		ClientDTO user = ClientService.login(toLogin);

		// Verification des infos
		assertTrue(user.getLogin().equals("pebron"));
		assertTrue("lollollol".equals(user.getPassword()));
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
	public void testgetClientForLoginFail() throws Exception {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebronmdr");
		toLogin.setPassword("lollol");

		// Appel du service qui check le login
		ClientDTO user = ClientService.login(toLogin);

		// Verification que rien n'est renvoyer ce qui veut dire que la
		// combinaison login / mdp n'est pas bonne ou que l'utilisateur n'existe
		// pas
		assertTrue(user.getLogin().equals(""));
		assertTrue(user.getPassword().equals(""));
		assertTrue(user.getEmail().equals(""));
	}
}
