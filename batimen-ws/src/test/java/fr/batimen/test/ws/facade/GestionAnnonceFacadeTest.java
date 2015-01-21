package fr.batimen.test.ws.facade;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.batimen.core.constant.Constant;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.test.ws.helper.DataHelper;
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
        creationAnnonceDTO = DataHelper.getAnnonceDTOData();
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
            "datemaj", "datecreation" })
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
    @ShouldMatchDataSet(value = "datasets/out/creation_annonce_is_signed_in.yml", excludeColumns = { "id", "datemaj",
            "datecreation" })
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
        Assert.assertTrue(isCreationOK == Constant.CODE_SERVICE_ANNONCE_RETOUR_DUPLICATE);
    }

    /**
     * Cas de test : récupération des annonces des clients. /!\ charge
     * automatiquement les artisans qui sont inscrits a l'annonce.
     * 
     */
    @Test
    @UsingDataSet("datasets/in/annonces.yml")
    public void testGetAnnonceByLogin() {
        List<AnnonceDTO> annonces = AnnonceService.getAnnonceByLoginForClient("pebronne");
        Assert.assertEquals(2, annonces.size());

    }

    /**
     * Cas de test : récupération des annonces des clients. /!\ charge
     * automatiquement les artisans qui sont inscrits a l'annonce.
     * 
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetAnnonceByID() {
        AnnonceDTO annonce = AnnonceService
                .getAnnonceByID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        Assert.assertNotNull(annonce);
        Assert.assertEquals("Peinture d'un mur", annonce.getDescription());
        // Assert.assertEquals("Pebronne enterprise",
        // annonce.getgetDescription());
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

        for (Annonce annonce : annoncesDeJohnny) {
            Assert.assertFalse(annonce.getHashID().isEmpty());
            Assert.assertFalse(annonce.getSelHashID().isEmpty());
        }
    }
}
