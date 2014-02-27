package fr.batimen.ws.dao;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.QueryJPQL;
import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.LoginDTO;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;

/**
 * Controlleur d'utilisateurs
 * 
 * @author Casaucau Cyril
 */
@Stateless(name = "UserDAO")
@Path(WsPath.USER_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClientDAO {

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDAO.class);

	/**
	 * Methode de login des utilisateurs
	 * 
	 * @param LoginDTO
	 *            loginDTO objet permettant l'authentification
	 * @return UserDTO vide si la combinaison login / mdp ne corresponds pas ou
	 *         si inexistant
	 */
	@POST
	@Path(WsPath.CLIENT_SERVICE_LOGIN)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ClientDTO login(LoginDTO toLogin) {

		Client userFinded = null;
		ModelMapper modelMapper = new ModelMapper();

		try {
			Query query = em.createNamedQuery(QueryJPQL.CLIENT_LOGIN);
			query.setParameter(QueryJPQL.CLIENT_LOGIN, toLogin.getLogin());

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Chargement requete JPQL OK ");
			}

			userFinded = (Client) query.getSingleResult();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Récuperation resultat requete JPQL OK ");
			}

			return modelMapper.map(userFinded, ClientDTO.class);
		} catch (NoResultException nre) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Aucune correspondance trouvées dans la BDD", nre);
			}
			return new ClientDTO();
		}
	}
}