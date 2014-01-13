package fr.batimen.ws.dao;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.db.transaction.VoidRunnable;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.UserDTO;
import fr.batimen.ws.AbstractBatimenTest;
import fr.batimen.ws.entity.User;
import fr.batimen.ws.helper.HashHelper;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
public class UserDAOTest extends AbstractBatimenTest {

	@ObjectUnderTest
	public UserDAO userDAO;

	final User userToRec = new User();

	// Creation d'un user de test dans la BDD
	@Before
	public void init() throws Exception {

		userToRec.setEmail("lol@lol.com");
		userToRec.setLogin("pebron");
		userToRec.setPassword(HashHelper.hashString("lollollol"));

		// On ouvre une transaction avec la BDD
		try {
			transactionHelper.executeInTransaction(new VoidRunnable() {
				@Override
				public void doRun(EntityManager entityManager) throws Exception {
					// On persiste un utilisateur dans la bdd
					entityManager.persist(userToRec);
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
		UserDTO user = userDAO.login(toLogin);

		// Verification des infos
		assertTrue(user.getLogin().equals("pebron"));
		assertTrue(HashHelper.check(toLogin.getPassword(), user.getPassword()));
		assertTrue(user.getEmail().equals("lol@lol.com"));
		assertTrue(user.getFullname().equals("Pebron de la pebronne"));
	}

	@Test
	public void testLoginFail() throws Exception {

		// L'objet que l'on doit recevoir du frontend quand l'utilisateur
		// tentera de s'authentifier
		LoginDTO toLogin = new LoginDTO();
		toLogin.setLogin("pebron");
		toLogin.setPassword("lollol");

		// Appel du service qui check le login
		UserDTO user = userDAO.login(toLogin);

		// Verification que rien n'est renvoyer ce qui veut dire que la
		// combinaison login / mdp n'est pas bonne ou que l'utilisateur n'existe
		// pas
		assertTrue(user.getLogin().equals(""));
		assertTrue(user.getPassword().equals(""));
		assertTrue(user.getEmail().equals(""));
		assertTrue(user.getFullname().equals(""));
	}
}
