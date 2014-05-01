package fr.batimen.ws.facade;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ClientService;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Client;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
public class GestionClientFacadeTest extends AbstractBatimenWsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionClientFacadeTest.class);

	@Inject
	private ClientDAO clientDAO;

	@Test
	@UsingDataSet("datasets/in/clients.yml")
	public void testGetClientForLogin() {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebronne");
		toLogin.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");

		// Appel du service qui check le login
		ClientDTO user = ClientService.login(toLogin);

		// Verification des infos
		assertTrue(user.getLogin().equals("pebronne"));
		assertTrue("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=".equals(user
		        .getPassword()));
		assertTrue(user.getEmail().equals("lol@lol.com"));
		assertTrue(user.getNumeroTel().equals("0615125645"));
		assertTrue(user.getCivilite() == Civilite.MONSIEUR);
		assertTrue(user.getPrenom().equals("Pebron"));
		assertTrue(user.getNom().equals("De la Pebronne"));

		Calendar calInscription = Calendar.getInstance(Locale.FRANCE);
		calInscription.setTime(user.getDateInscription());

		Calendar calAssert = Calendar.getInstance(Locale.FRANCE);
		calAssert.set(Calendar.DAY_OF_MONTH, 10);
		calAssert.set(Calendar.MONTH, Calendar.JANUARY);
		calAssert.set(Calendar.YEAR, 2014);

		// On check que la date d'inscription est bien le 10 janvier 2014
		assertTrue(calInscription.get(Calendar.DAY_OF_WEEK) == calAssert.get(Calendar.DAY_OF_WEEK));
		assertTrue(calInscription.get(Calendar.MONTH) == calAssert.get(Calendar.MONTH));
		assertTrue(calInscription.get(Calendar.YEAR) == calAssert.get(Calendar.YEAR));

	}

	@Test
	@UsingDataSet("datasets/in/clients.yml")
	public void testGetClientForLoginFail() throws Exception {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebronmdr");
		toLogin.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");

		// Appel du service qui check le login
		ClientDTO user = ClientService.login(toLogin);

		// Verification que rien n'est renvoyer ce qui veut dire que la
		// combinaison login / mdp n'est pas bonne ou que l'utilisateur n'existe
		// pas
		assertTrue(user.getLogin().equals(""));
		assertTrue(user.getPassword().equals(""));
		assertTrue(user.getEmail().equals(""));
	}

	/**
	 * On vérifie que le DAO renvoi bien le bon client par rapport a son email.
	 * 
	 */
	@Test
	@UsingDataSet("datasets/in/clients.yml")
	public void testGetClientForEmail() {

		ClientDTO clientEmail = ClientService.getClientByEmail("lol@lol.com");

		// On vérifie les differentes infos du client
		assertTrue(clientEmail.getLogin().equals("pebronne"));
		assertTrue(clientEmail.getPassword().equals(
		        "$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY="));
		assertTrue(clientEmail.getEmail().equals("lol@lol.com"));
	}

	/**
	 * On vérifie que le DAO n'enregistre pas le client si il y a duplication.
	 * 
	 * @throws BackendException
	 */
	@Test(expected = DuplicateEntityException.class)
	@UsingDataSet("datasets/in/clients.yml")
	public void testSaveDuplilcateClient() throws BackendException, DuplicateEntityException {

		Client clientDuplicate = new Client();
		clientDuplicate.setCivilite(Civilite.MONSIEUR);
		clientDuplicate.setEmail("lol@lol.com");
		clientDuplicate.setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");
		clientDuplicate.setLogin("pebronne");
		clientDuplicate.setNom("De la Pebronne");
		clientDuplicate.setNumeroTel("0615125645");
		clientDuplicate.setPrenom("Pebron");
		clientDuplicate.setIsArtisan(false);

		Calendar calClient = Calendar.getInstance(Locale.FRANCE);
		calClient.set(2014, 01, 10, 00, 00, 00);
		clientDuplicate.setDateInscription(calClient.getTime());

		clientDAO.saveClient(clientDuplicate);
	}
}
