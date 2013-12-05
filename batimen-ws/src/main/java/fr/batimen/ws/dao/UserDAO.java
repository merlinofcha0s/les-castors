package fr.batimen.ws.dao;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.LoginDTO;
import fr.batimen.dto.UserDTO;
import fr.batimen.ws.entity.User;
import fr.batimen.ws.helper.HashHelper;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;

/**
 * Controlleur d'utilisateurs
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "UserDAO")
@Path(WsPath.USER_SERVICE_PATH)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
public class UserDAO {

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

	/**
	 * Methode de login des utilisateurs
	 * 
	 * @param LoginDTO
	 *            loginDTO objet permettant l'authentification
	 * @return UserDTO vide si la combinaison login / mdp ne corresponds pas ou
	 *         si inexistant
	 */
	@POST
	@Path(WsPath.USER_SERVICE_LOGIN)
	public UserDTO login(LoginDTO toLogin) {

		User userFinded = null;
		ModelMapper modelMapper = new ModelMapper();

		try {
			Query query = em.createNamedQuery(QueryJPQL.USER_LOGIN);
			query.setParameter(QueryJPQL.USER_LOGIN, toLogin.getLogin());

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Chargement requete JPQL OK ");
			}

			userFinded = (User) query.getSingleResult();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Récuperation resultat requete JPQL OK ");
			}

			// Vérification du password avec le hash qui se trouve dans la bdd
			boolean passwordMatch = HashHelper.check(toLogin.getPassword(), userFinded.getPassword());

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Verification du password : " + passwordMatch);
			}

			if (passwordMatch) {
				// Si le password match on transforme l'entité en DTO
				return modelMapper.map(userFinded, UserDTO.class);
			} else {
				return new UserDTO();
			}

		} catch (NoResultException nre) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Aucune correspondance trouvées dans la BDD", nre);
			}
			return new UserDTO();
		}
	}
}
