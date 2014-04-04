package fr.batimen.ws.facade;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.batimen.core.constant.Constant;
import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.dto.enums.DelaiIntervention;
import fr.batimen.dto.enums.Metier;
import fr.batimen.dto.enums.TypeContact;
import fr.batimen.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.AnnonceService;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;

/**
 * 
 * @author Casaucau Cyril
 * 
 */
public class GestionAnnonceFacadeTest extends AbstractBatimenWsTest {

	private CreationAnnonceDTO creationAnnonceDTO;

	@Inject
	private ClientDAO clientDAO;

	@Inject
	private AnnonceDAO annonceDAO;

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
		creationAnnonceDTO
		        .setPassword("$s0$54040$h99gyX0NNTBvETrAdfjtDw==$fo2obQTG56y7an9qYl3aEO+pv3eH6p4hLzK1xt8EuoY=");

		// Infos Qualification Annonce
		creationAnnonceDTO.setDescription("Peinture d'un mur");

		// Le 23/03/2014 @ 22:23:00
		Calendar calDateInsription = Calendar.getInstance();
		calDateInsription.set(Calendar.YEAR, 2014);
		calDateInsription.set(Calendar.MONTH, Calendar.MARCH);
		calDateInsription.set(Calendar.DAY_OF_MONTH, 23);
		calDateInsription.set(Calendar.HOUR_OF_DAY, 22);
		calDateInsription.set(Calendar.MINUTE, 00);
		calDateInsription.set(Calendar.SECOND, 00);
		calDateInsription.set(Calendar.MILLISECOND, 0);

		creationAnnonceDTO.setDateInscription(calDateInsription.getTime());
		creationAnnonceDTO.setDelaiIntervention(DelaiIntervention.LE_PLUS_RAPIDEMENT_POSSIBLE);
		creationAnnonceDTO.setDepartement(06);
		creationAnnonceDTO.setMetier(Metier.PEINTURE);
		creationAnnonceDTO.setNbDevis(3);
		creationAnnonceDTO.setTitre("Peinture facade");
		creationAnnonceDTO.setTypeContact(TypeContact.EMAIL);
		creationAnnonceDTO.setVille("Nice");
	}

	/**
	 * Cas de test : le client n'est pas inscrit sur le site. Il faut donc creer
	 * l'annonce mais également enregister son compte dans la base de données.
	 * 
	 * On ignore volontairement date inscription et datemaj car elles sont
	 * généréés dynamiquement lors de la creation de l'annonce.
	 */
	@Test
	@ShouldMatchDataSet(value = "datasets/out/creation_annonce_is_not_signed_in.yml", excludeColumns = { "id",
	        "datemaj" })
	public void testCreationAnnonceIsNotSignedIn() {
		creationVerificationAnnonce();
	}

	/**
	 * Cas de test : le client est inscrit sur le site. Il faut donc creer
	 * l'annonce mais ne pas enregistrer son compte, juste lier le compte de
	 * l'utilisateur avec l'annonce.
	 * 
	 * On ignore volontairement date inscription et datemaj car elles sont
	 * généréés dynamiquement lors de la creation de l'annonce.
	 */
	@Test
	@UsingDataSet("datasets/in/client_creation_annonce.yml")
	@ShouldMatchDataSet(value = "datasets/out/creation_annonce_is_signed_in.yml", excludeColumns = { "id", "datemaj" })
	public void testCreationAnnonceIsSignedIn() {
		creationAnnonceDTO.setIsSignedUp(true);
		creationVerificationAnnonce();
	}

	/**
	 * Cas de test : Le client tente de creer une annonce qui existe déjà avec
	 * le même titre.
	 * 
	 */
	@Test
	public void testCreationAnnonceDuplicata() {
		creationVerificationAnnonce();
		Integer isCreationOK = AnnonceService.creationAnnonce(creationAnnonceDTO);
		// Le service doit remonter une erreur
		Assert.assertTrue(isCreationOK == Constant.CODE_SERVICE_RETOUR_ANNONCE_DUPLICATE);
	}

	private void creationVerificationAnnonce() {

		String loginDeJohnny = "johnny06";
		Integer isCreationOK = AnnonceService.creationAnnonce(creationAnnonceDTO);

		// On utilise le DAO de l'annonce et de l'user pour vérifier le tout
		Client johnny = clientDAO.getClientByLoginName(loginDeJohnny);
		List<Annonce> annoncesDeJohnny = annonceDAO.getAnnoncesByLogin(loginDeJohnny);

		// On test que la creation de l'annonce et du client en bdd est ok
		Assert.assertTrue(isCreationOK == Constant.CODE_SERVICE_RETOUR_OK);
		// On check que le client est présent dans la BDD
		Assert.assertNotNull(johnny);
		// On regarde que ce soit le bon login
		Assert.assertTrue(loginDeJohnny.equals(johnny.getLogin()));
		// On s'assure que la liste d'annonce renvoyées n'est pas null
		Assert.assertNotNull(annoncesDeJohnny);
		// On test qu'il y a bien une annonce liée à Johnny boy
		Assert.assertTrue(annoncesDeJohnny.size() == 1);
	}
}
