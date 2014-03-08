package fr.batimen.ws.facade;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import fr.batimen.core.constant.Constant;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.ws.AbstractBatimenTest;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
public class GestionAnnonceFacadeTest extends AbstractBatimenTest {

	@ObjectUnderTest
	private GestionAnnonceFacade gestionAnnonceFacade;

	@ObjectUnderTest
	@InjectInto(targetComponentId = "gestionAnnonceFacade")
	private AnnonceDAO annonceDAO;

	@ObjectUnderTest
	private ClientDAO clientDAO;

	private CreationAnnonceDTO creationAnnonceDTO;

	@Before
	public void init() {
		creationAnnonceDTO = new CreationAnnonceDTO();
		// Infos Client
		creationAnnonceDTO.setCivilite(Civilite.MONSIEUR);
		creationAnnonceDTO.setNom("Du Pebron");
		creationAnnonceDTO.setPrenom("Johnny");
		creationAnnonceDTO.setAdresse("106 chemin du pébron");
		creationAnnonceDTO.setComplementAdresse("Res des pébrons");
		creationAnnonceDTO.setCodePostal("06700");
		creationAnnonceDTO.setIsSignedUp(false);
		creationAnnonceDTO.setEmail("pebron@delapebronne.com");
		creationAnnonceDTO.setLogin("johnny06");
		creationAnnonceDTO.setNumeroTel("0615458596");
		creationAnnonceDTO.setPassword(HashHelper.hashString("lolmdr06"));

		// Infos Qualification Annonce
		creationAnnonceDTO.setDescription("Peinture d'un mur");
		creationAnnonceDTO.setDateInscription(new Date());
		creationAnnonceDTO.setDelaiIntervention(DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE);
		creationAnnonceDTO.setDepartement(06);
		creationAnnonceDTO.setMetier(Metier.PEINTRE);
		creationAnnonceDTO.setNbDevis(3);
		creationAnnonceDTO.setTitre("Peinture facade");
		creationAnnonceDTO.setTypeContact(TypeContact.EMAIL);
		creationAnnonceDTO.setVille("Nice");
	}

	/**
	 * Cas de test : le client n'est pas inscrit sur le site. Il faut donc creer
	 * l'annonce mais également enregister son compte dans la base de données.
	 */
	@Test
	public void testCreationAnnonceIsNotSignedIn() {

		String loginDeJohnny = "johnny06";

		Integer isCreationOK = gestionAnnonceFacade.creationAnnonce(creationAnnonceDTO);

		// On utilise le DAO de l'annonce et de l'user pour vérifier le tout
		Client johnny = clientDAO.getClientByLoginName(loginDeJohnny);
		List<Annonce> annoncesDeJohnny = annonceDAO.getAnnoncesByLogin(loginDeJohnny);

		// On test que la creation de l'annonce et du client en bdd est ok
		Assert.assertTrue(isCreationOK == Constant.CODE_SERVICE_RETOUR_OK);
		// On check que le client est présent dans la BDD
		Assert.assertNotNull(johnny);
		// On regarde que ce soit le bon login
		Assert.assertTrue(loginDeJohnny.equals(johnny.getLogin()));
		// On s'assure que la liste renvoyé n'est pas null
		Assert.assertNotNull(annoncesDeJohnny);
		// On test qu'il y a bien une annonce liée a Johnny boy
		Assert.assertTrue(annoncesDeJohnny.size() == 1);
	}
}
