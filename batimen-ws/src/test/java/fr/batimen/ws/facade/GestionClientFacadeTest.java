package fr.batimen.ws.facade;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;

import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ClientService;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
@Cleanup(phase = TestExecutionPhase.AFTER, strategy = CleanupStrategy.USED_ROWS_ONLY)
public class GestionClientFacadeTest extends AbstractBatimenWsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionClientFacadeTest.class);

	@Test
	@UsingDataSet("datasets/clients.yml")
	public void testGetClientForLogin() {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebronne");
		toLogin.setPassword("lollollol");

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
	@UsingDataSet("datasets/clients.yml")
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
