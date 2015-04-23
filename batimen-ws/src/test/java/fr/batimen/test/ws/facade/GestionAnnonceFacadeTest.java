package fr.batimen.test.ws.facade;

import java.io.File;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import fr.batimen.dto.aggregate.*;
import fr.batimen.dto.enums.*;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.AnnonceDTO;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.NotationDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.test.ws.helper.DataHelper;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.service.NotificationService;

/**
 * @author Casaucau Cyril
 */
public class GestionAnnonceFacadeTest extends AbstractBatimenWsTest {

    private CreationAnnonceDTO creationAnnonceDTO;

    @Inject
    private ClientDAO clientDAO;

    @Inject
    private AnnonceDAO annonceDAO;

    @Inject
    private NotificationService notificationService;

    @Inject
    private ArtisanDAO artisanDAO;

    @Inject
    private AnnonceServiceREST annonceServiceREST;

    @Before
    public void init() {
        creationAnnonceDTO = DataHelper.getAnnonceDTOData();
    }

    /**
     * Cas de test : le client n'est pas inscrit sur le site. Il faut donc creer
     * l'annonce mais également enregister son compte dans la base de données.
     * <p/>
     * On ignore volontairement date inscription et datemaj car elles sont
     * généréés dynamiquement lors de la creation de l'annonce.
     */
    @Test
    @ShouldMatchDataSet(value = "datasets/out/creation_annonce_is_not_signed_in.yml", excludeColumns = {"id",
            "datemaj", "datecreation"})
    public void testCreationAnnonceIsNotSignedIn() {
        creationVerificationAnnonce();
    }

    /**
     * Cas de test : le client est inscrit sur le site. Il faut donc creer
     * l'annonce mais ne pas enregistrer son compte, juste lier le compte de
     * l'utilisateur avec l'annonce.
     * <p/>
     * On ignore volontairement date inscription et datemaj car elles sont
     * généréés dynamiquement lors de la creation de l'annonce.
     */
    @Test
    @UsingDataSet("datasets/in/client_creation_annonce.yml")
    @ShouldMatchDataSet(value = "datasets/out/creation_annonce_is_signed_in.yml", excludeColumns = {"id", "datemaj",
            "datecreation"})
    public void testCreationAnnonceIsSignedIn() {
        creationAnnonceDTO.setIsSignedUp(true);
        creationVerificationAnnonce();
    }

    /**
     * Cas de test : le client est inscrit sur le site. Il faut donc creer
     * l'annonce mais ne pas enregistrer son compte, juste lier le compte de
     * l'utilisateur avec l'annonce.<br/>
     * De plus dans ce cas, il enregistre des photos avec son annonce
     * <p/>
     * On ignore volontairement date inscription et datemaj car elles sont
     * généréés dynamiquement lors de la creation de l'annonce.
     */
    @Test
    @UsingDataSet("datasets/in/client_creation_annonce.yml")
    @ShouldMatchDataSet(value = "datasets/out/creation_annonce_is_signed_in.yml", excludeColumns = {"id", "datemaj",
            "datecreation"})
    public void testCreationAnnonceIsSignedInWithImage() {
        creationAnnonceDTO.setIsSignedUp(true);

        // On recupére la photo dans les ressources de la webapp de test
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("img/castor.jpg").getFile());
        creationAnnonceDTO.getPhotos().add(file);

        Integer codeRetour = annonceServiceREST.creationAnnonceAvecImage(creationAnnonceDTO);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetour);

        List<Annonce> annonces = annonceDAO.getAnnoncesByLogin("johnny06");

        for (Annonce annonce : annonces) {
            Assert.assertFalse(annonce.getImages().isEmpty());
        }
    }

    /**
     * Cas de test : Le client tente de creer une annonce qui existe déjà avec
     * le même titre.
     */
    @Test
    public void testCreationAnnonceDuplicata() {
        creationVerificationAnnonce();
        Integer isCreationOK = annonceServiceREST.creationAnnonce(creationAnnonceDTO);
        // Le service doit remonter une erreur
        Assert.assertTrue(isCreationOK == CodeRetourService.ANNONCE_RETOUR_DUPLICATE);
    }

    /**
     * Cas de test : récupération des annonces des clients. /!\ charge
     * automatiquement les artisans qui sont inscrits a l'annonce.
     */
    @Test
    @UsingDataSet("datasets/in/annonces.yml")
    public void testGetAnnonceByLogin() {
        List<AnnonceDTO> annonces = annonceServiceREST.getAnnonceByLoginForClient("pebronne");
        Assert.assertEquals(2, annonces.size());

    }

    /**
     * Cas de test : récupération de l'annonce d'un client par un client.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetAnnonceByIDForAffichage() {
        DemandeAnnonceDTO demandeAnnonceDTO = createDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                "pebronne", TypeCompte.CLIENT);

        AnnonceAffichageDTO annonceAffichage = annonceServiceREST.getAnnonceByIDForAffichage(demandeAnnonceDTO);
        Assert.assertNotNull(annonceAffichage);
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDescription());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateCreation());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateMAJ());

        Assert.assertNotNull(annonceAffichage.getEntreprises().get(0));
        Assert.assertNotNull(annonceAffichage.getEntreprises().get(0).getDateCreation());

        Assert.assertNotNull(annonceAffichage.getAdresse());
        Assert.assertNotNull(annonceAffichage.getEntrepriseSelectionnee());
        Assert.assertFalse(annonceAffichage.getImages().isEmpty());
    }

    /**
     * Cas de test : Récuperation d'une annonce par son id a partir d'un artisan
     * qui n'est pas inscrit à cette derniere.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetAnnonceByIDWithArtisanNotInscritAtAnnonce() {
        DemandeAnnonceDTO demandeAnnonceDTO = createDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                "pebronArtisanPasInsc", TypeCompte.ARTISAN);

        AnnonceAffichageDTO annonceAffichage = annonceServiceREST.getAnnonceByIDForAffichage(demandeAnnonceDTO);
        Assert.assertNotNull(annonceAffichage);
        Assert.assertFalse(annonceAffichage.getIsArtisanInscrit());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDescription());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateCreation());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateMAJ());
        Assert.assertNull(annonceAffichage.getTelephoneClient());
        Assert.assertNull(annonceAffichage.getEmailClient());
    }

    /**
     * Cas de test : Récuperation d'une annonce par son id a partir d'un artisan
     * qui est inscrit à cette derniere.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetAnnonceByIDWithArtisanInscritAtAnnonce() {
        DemandeAnnonceDTO demandeAnnonceDTO = createDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                "pebronneArtisanne", TypeCompte.ARTISAN);

        AnnonceAffichageDTO annonceAffichage = annonceServiceREST.getAnnonceByIDForAffichage(demandeAnnonceDTO);
        Assert.assertNotNull(annonceAffichage);
        Assert.assertTrue(annonceAffichage.getIsArtisanInscrit());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDescription());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateCreation());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateMAJ());
        Assert.assertEquals("0615125645", annonceAffichage.getTelephoneClient());
        Assert.assertEquals("lol@lol.com", annonceAffichage.getEmailClient());
    }

    /**
     * Cas de test : Récuperation d'une annonce par son id a partir d'un artisan
     * qui est inscrit à cette derniere.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetAnnonceByIDWithAdmin() {
        DemandeAnnonceDTO demandeAnnonceDTO = createDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                "admin", TypeCompte.ADMINISTRATEUR);

        AnnonceAffichageDTO annonceAffichage = annonceServiceREST.getAnnonceByIDForAffichage(demandeAnnonceDTO);
        Assert.assertNotNull(annonceAffichage);
        Assert.assertFalse(annonceAffichage.getIsArtisanInscrit());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDescription());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateCreation());
        Assert.assertNotNull(annonceAffichage.getAnnonce().getDateMAJ());
        Assert.assertEquals("0615125645", annonceAffichage.getTelephoneClient());
        Assert.assertEquals("lol@lol.com", annonceAffichage.getEmailClient());
    }

    /**
     * Cas de test : Incrémentation du nb de consultation quand un artisan
     * accede à la page d'annonce.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testUpdateNbConsultation() {
        NbConsultationDTO nbConsultationDTO = new NbConsultationDTO();
        nbConsultationDTO
                .setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        nbConsultationDTO.setNbConsultation(1);

        Integer updateOK = annonceServiceREST.updateNbConsultationAnnonce(nbConsultationDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, updateOK);

        Annonce annonce = annonceDAO
                .getAnnonceByIDWithoutTransaction(
                        "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                        true);
        Assert.assertEquals(annonce.getNbConsultation(), Integer.valueOf("2"));
    }

    /**
     * Cas de test : Suppression d'une annonce par un client, le test doit
     * mettre l'annonce en statut supprimer correctement.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testSuppressionAnnonceWithValidDemandeur() {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO
                .setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        demandeAnnonceDTO.setLoginDemandeur("pebronne");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.CLIENT);

        Integer updateOK = annonceServiceREST.suppressionAnnonce(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, updateOK);

        Annonce annonce = annonceDAO
                .getAnnonceByIDWithoutTransaction(
                        "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                        true);
        Assert.assertNotNull(annonce);
        Assert.assertEquals(EtatAnnonce.SUPPRIMER, annonce.getEtatAnnonce());

    }

    /**
     * Cas de test : Un client essai d'effacer d'une annonce qui n'est pas a
     * lui, le webservice refuse. Aucune demande de devis ne s'efface, on la
     * passe juste en mode supprimé
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testSuppressionAnnonceWithInValidDemandeur() {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO
                .setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        demandeAnnonceDTO.setLoginDemandeur("pebronnelol");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.CLIENT);

        Integer updateKO = annonceServiceREST.suppressionAnnonce(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_KO, updateKO);

        Annonce annonce = annonceDAO
                .getAnnonceByIDWithoutTransaction(
                        "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                        true);
        Assert.assertNotNull(annonce);
        Assert.assertEquals(EtatAnnonce.ACTIVE, annonce.getEtatAnnonce());
    }

    /**
     * Cas de test : Suppression d'une annonce par un admin, le test doit mettre
     * l'annonce en statut supprimer correctement.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testSuppressionAnnonceWithAdmin() {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO
                .setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        demandeAnnonceDTO.setLoginDemandeur("admin");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.ADMINISTRATEUR);

        Integer updateOK = annonceServiceREST.suppressionAnnonce(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, updateOK);

        Annonce annonce = annonceDAO
                .getAnnonceByIDWithoutTransaction(
                        "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                        true);
        Assert.assertNotNull(annonce);
        Assert.assertEquals(EtatAnnonce.SUPPRIMER, annonce.getEtatAnnonce());
    }

    /**
     * Cas de test : Selection d'un artisan par un client dans son annonce
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testAjoutChoixArtisanForAnnonce() {
        AnnonceSelectEntrepriseDTO demandeAnnonceDTO = new AnnonceSelectEntrepriseDTO();
        demandeAnnonceDTO.setHashID("lolmdrp");
        demandeAnnonceDTO.setLoginDemandeur("pebronne");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.CLIENT);
        demandeAnnonceDTO.setLoginArtisanChoisi("pebronneChoisi");
        demandeAnnonceDTO.setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);

        Integer updateOK = annonceServiceREST.selectOneEnterprise(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, updateOK);

        assertEntrepriseForChoixArtisanWithTransaction(false);
    }

    /**
     * Cas de test : Selection d'un artisan par un admin pour une annonce.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testAjoutChoixArtisanByAdminForAnnonce() {
        AnnonceSelectEntrepriseDTO demandeAnnonceDTO = new AnnonceSelectEntrepriseDTO();
        demandeAnnonceDTO.setHashID("lolmdrp");
        demandeAnnonceDTO.setLoginDemandeur("admin");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.ADMINISTRATEUR);
        demandeAnnonceDTO.setLoginArtisanChoisi("pebronneChoisi");
        demandeAnnonceDTO.setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);

        Integer updateOK = annonceServiceREST.selectOneEnterprise(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, updateOK);

        assertEntrepriseForChoixArtisanWithTransaction(false);
    }

    /**
     * Cas de test : Suppression d'une entreprise qui avait été selectionnée. <br/>
     * Suppression faite par un administrateur
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testAjoutChoixArtisanByNotOwnerNorAdmin() {
        AnnonceSelectEntrepriseDTO demandeAnnonceDTO = new AnnonceSelectEntrepriseDTO();
        demandeAnnonceDTO
                .setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        demandeAnnonceDTO.setLoginDemandeur("doe");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.CLIENT);
        demandeAnnonceDTO.setLoginArtisanChoisi("pebronneChoisi");
        demandeAnnonceDTO.setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);

        Integer updateKO = annonceServiceREST.selectOneEnterprise(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_KO, updateKO);

        assertEntrepriseForChoixArtisanWithTransaction(true);
    }

    /**
     * Cas de test : l'artisan s'inscrit à l'annonce, il recoit une
     * notification, le tout doit etre correctement enregistré dans la BDD
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testInscriptionArtisanNominal() {

        String loginArtisan = "pebronneChoisi";

        DemandeAnnonceDTO demandeAnnonceDTO = initAndGetDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                loginArtisan, TypeCompte.ARTISAN);

        Integer codeRetour = annonceServiceREST.inscriptionUnArtisan(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetour);

        // VERIFICATION DE L'ENREGISTREMENT DE L'INSCRIPTION
        Annonce annonce = annonceDAO
                .getAnnonceByIDWithTransaction(
                        "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                        true);

        // VERIFICATION COTE ARTISAN
        Artisan artisan = artisanDAO.getArtisanByLogin(loginArtisan);
        Assert.assertNotNull(artisan);
        Assert.assertEquals(1, artisan.getAnnonces().size());
        boolean artisanIsPresent = false;
        for (Artisan artisanToCheck : annonce.getArtisans()) {
            if (artisanToCheck.getLogin().equals(artisan.getLogin())) {
                artisanIsPresent = true;
            }
        }
        Assert.assertTrue(artisanIsPresent);

        // VERIFICATION DE L'ENREGISTREMENT DE LA NOTIFICATION
        List<NotificationDTO> notifications = notificationService.getNotificationByLogin(annonce.getDemandeur()
                .getLogin(), TypeCompte.CLIENT);
        Assert.assertEquals(2, notifications.size());
        boolean isNotificationCorrecte = false;

        for (NotificationDTO notificationDTO : notifications) {
            if (notificationDTO.getTypeNotification().equals(TypeNotification.INSCRIT_A_ANNONCE)
                    && notificationDTO.getNomEntreprise().equals(artisan.getEntreprise().getNomComplet())) {
                isNotificationCorrecte = true;
            }
        }

        Assert.assertTrue(isNotificationCorrecte);

    }

    /**
     * Cas de test: L'artisan essaye de s'inscrire deux fois, la premiere fois
     * se passe bien, la deuxieme fois on renvoi un code retour indiquant qu'il
     * est deja inscrit.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testInscriptionArtisanInscriptionDeuxFois() {

        String loginArtisan = "pebronneChoisi";

        DemandeAnnonceDTO demandeAnnonceDTO = initAndGetDemandeAnnonceDTO("lolmdrp", loginArtisan, TypeCompte.ARTISAN);

        Integer codeRetourOK = annonceServiceREST.inscriptionUnArtisan(demandeAnnonceDTO);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetourOK);

        Integer codeRetourDejaInscrit = annonceServiceREST.inscriptionUnArtisan(demandeAnnonceDTO);
        Assert.assertEquals(CodeRetourService.ANNONCE_RETOUR_ARTISAN_DEJA_INSCRIT, codeRetourDejaInscrit);
    }

    /**
     * Cas de test : Un artisan essaye de s'inscrire alors que le quotas est
     * atteint.<br/>
     * Le webservice doit renvoyer un code retour qui indique quer le quotas est
     * atteint
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testInscriptionArtisanQuotasAtteint() {

        String loginArtisan = "pebronneChoisi";

        DemandeAnnonceDTO demandeAnnonceDTO = initAndGetDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                loginArtisan, TypeCompte.ARTISAN);

        Integer codeRetourOK = annonceServiceREST.inscriptionUnArtisan(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetourOK);

        // VERIFICATION DE L'ENREGISTREMENT DE L'INSCRIPTION
        Annonce annonce = annonceDAO
                .getAnnonceByIDWithTransaction(
                        "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                        true);

        Assert.assertEquals(EtatAnnonce.QUOTA_MAX_ATTEINT, annonce.getEtatAnnonce());
    }

    /**
     * Cas de test : Un artisan essaye de s'inscrire a une annonce qui n'existe
     * pas
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testInscriptionArtisanAvecIDInvalid() {

        String loginArtisan = "pebronneChoisi";

        DemandeAnnonceDTO demandeAnnonceDTO = initAndGetDemandeAnnonceDTO("toto", loginArtisan, TypeCompte.ARTISAN);

        Integer codeRetourKO = annonceServiceREST.inscriptionUnArtisan(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_KO, codeRetourKO);
    }

    /**
     * Cas de test : Un client ne veut pas d'un artisan et le desinscrit de
     * l'annonce
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testDesinscriptionArtisanAvecIDValid() {
        Annonce annonce = testDesinscriptionArtisan("pebronne");
        Assert.assertEquals(1, annonce.getArtisans().size());
    }

    /**
     * Cas de test : Un administrateur ne veut pas d'un artisan et le desinscrit
     * d'une annonce
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testDesinscriptionArtisanAvecIDValidByAdmin() {
        Annonce annonce = testDesinscriptionArtisan("admin");
        Assert.assertEquals(1, annonce.getArtisans().size());
    }

    /**
     * Cas de test : Un administrateur ne veut pas d'un artisan et le desinscrit
     * d'une annonce
     */
    @Test
    @UsingDataSet("datasets/in/annonce_5_artisans.yml")
    public void testDesinscriptionArtisanAvecIDValidReactivationAnnonce() {
        Annonce annonce = testDesinscriptionArtisan("pebronne");
        Assert.assertEquals(4, annonce.getArtisans().size());
        Assert.assertEquals(EtatAnnonce.ACTIVE, annonce.getEtatAnnonce());
    }

    /**
     * Cas de test : Un client veut noter un artisan car les travaux sont
     * terminés
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/out/notation_artisan_par_client_et_admin.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification"})
    public void testNotationArtisanParClient() {
        NoterArtisanDTO noterArtisanDTO = createNotationDTO("pebronne");

        Integer codeRetour = annonceServiceREST.noterUnArtisan(noterArtisanDTO);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetour);
    }

    /**
     * Cas de test : Un admin veut noter un artisan car les travaux sont
     * terminés
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/out/notation_artisan_par_client_et_admin.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification"})
    public void testNotationArtisanParAdmin() {
        NoterArtisanDTO noterArtisanDTO = createNotationDTO("admin");

        Integer codeRetour = annonceServiceREST.noterUnArtisan(noterArtisanDTO);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetour);
    }

    /**
     * Cas de test : Un admin veut noter un artisan car les travaux sont
     * terminés
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/out/modification_annonce_par_client.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification"})
    public void testModificationAnnonceParClient() {
        DemandeAnnonceDTO demandeAnnonceDTO = createDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                "pebronne", TypeCompte.CLIENT);

        AnnonceAffichageDTO annonceAffichage = annonceServiceREST.getAnnonceByIDForAffichage(demandeAnnonceDTO);
        Assert.assertNotNull(annonceAffichage);

        ModificationAnnonceDTO modificationAnnonceDTO = new ModificationAnnonceDTO();
        modificationAnnonceDTO.setAnnonce(annonceAffichage.getAnnonce());
        modificationAnnonceDTO.getAnnonce().setHashID(demandeAnnonceDTO.getHashID());
        modificationAnnonceDTO.setAdresse(annonceAffichage.getAdresse());
        modificationAnnonceDTO.setLoginDemandeur("pebronne");

        modificationAnnonceDTO.getAnnonce().setTypeContact(TypeContact.TELEPHONE);
        modificationAnnonceDTO.getAnnonce().setTypeTravaux(TypeTravaux.RENOVATION);
        modificationAnnonceDTO.getAnnonce().setDelaiIntervention(DelaiIntervention.SIX_MOIS);

        Integer codeRetourOK = annonceServiceREST.modifierAnnonce(modificationAnnonceDTO);

        Assert.assertNotNull(codeRetourOK);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetourOK);
    }

    private NoterArtisanDTO createNotationDTO(String loginDemandeur) {
        NotationDTO notationDTO = new NotationDTO();
        notationDTO.setScore((double) 4);
        notationDTO.setCommentaire("Bon travaux");
        notationDTO.setNomEntreprise("Pebronne enterprise");

        NoterArtisanDTO noterArtisanDTO = new NoterArtisanDTO();
        noterArtisanDTO.setNotation(notationDTO);
        noterArtisanDTO
                .setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        noterArtisanDTO.setLoginArtisan("pebronneArtisanne");
        noterArtisanDTO.setLoginDemandeur(loginDemandeur);

        return noterArtisanDTO;
    }

    private Annonce testDesinscriptionArtisan(String loginDemandeur) {
        DesinscriptionAnnonceDTO desinscriptionAnnonceDTO = new DesinscriptionAnnonceDTO();
        desinscriptionAnnonceDTO
                .setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        desinscriptionAnnonceDTO.setLoginDemandeur(loginDemandeur);
        desinscriptionAnnonceDTO.setLoginArtisan("pebronneArtisanne");

        Integer codeRetourOK = annonceServiceREST.desinscriptionArtisan(desinscriptionAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetourOK);

        Annonce annonce = annonceDAO
                .getAnnonceByIDWithTransaction(
                        "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                        false);

        return annonce;
    }

    private DemandeAnnonceDTO initAndGetDemandeAnnonceDTO(String hash, String loginDemandeur, TypeCompte typeCompte) {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID(hash);
        demandeAnnonceDTO.setLoginDemandeur(loginDemandeur);
        demandeAnnonceDTO.setTypeCompteDemandeur(typeCompte);
        return demandeAnnonceDTO;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void assertEntrepriseForChoixArtisanWithTransaction(boolean isSuppression) {
        Annonce annonce = annonceDAO.getAnnonceByIDWithTransaction("lolmdrp", true);
        Assert.assertNotNull(annonce);
        if (isSuppression) {
            Assert.assertNull(annonce.getEntrepriseSelectionnee());
        } else {
            Assert.assertNotNull(annonce.getEntrepriseSelectionnee());
            Assert.assertEquals("Pebronne enterprise choisi", annonce.getEntrepriseSelectionnee().getNomComplet());
            Assert.assertEquals(EtatAnnonce.A_NOTER, annonce.getEtatAnnonce());
            List<NotificationDTO> notificationsDTO = notificationService.getNotificationByLogin(annonce
                    .getEntrepriseSelectionnee().getArtisan().getLogin(), TypeCompte.ARTISAN);

            for (NotificationDTO notification : notificationsDTO) {
                Assert.assertEquals(TypeNotification.A_CHOISI_ENTREPRISE, notification.getTypeNotification());
                Assert.assertEquals(StatutNotification.PAS_VUE, notification.getStatutNotification());
            }
        }
    }

    private DemandeAnnonceDTO createDemandeAnnonceDTO(String hashID, String login, TypeCompte typeCompte) {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID(hashID);
        demandeAnnonceDTO.setLoginDemandeur(login);
        demandeAnnonceDTO.setTypeCompteDemandeur(typeCompte);
        return demandeAnnonceDTO;
    }

    private void creationVerificationAnnonce() {
        String loginDeJohnny = "johnny06";
        Integer isCreationOK = annonceServiceREST.creationAnnonce(creationAnnonceDTO);

        // On utilise le DAO de l'annonce et de l'user pour vérifier le tout
        Client johnny = clientDAO.getClientByLoginName(loginDeJohnny);
        List<Annonce> annoncesDeJohnny = annonceDAO.getAnnoncesByLogin(loginDeJohnny);

        // On test que la creation de l'annonce et du client en bdd est ok
        Assert.assertTrue(isCreationOK == CodeRetourService.RETOUR_OK);
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
