package fr.castor.test.ws.facade;


import fr.castor.core.constant.CodeRetourService;
import fr.castor.dto.CategorieMetierDTO;
import fr.castor.dto.DemandeAnnonceDTO;
import fr.castor.dto.ImageDTO;
import fr.castor.dto.NotificationDTO;
import fr.castor.dto.aggregate.*;
import fr.castor.dto.constant.Categorie;
import fr.castor.dto.enums.EtatAnnonce;
import fr.castor.dto.enums.TypeCompte;
import fr.castor.dto.enums.TypeNotification;
import fr.castor.test.ws.AbstractBatimenWsTest;
import fr.castor.test.ws.helper.DataHelper;
import fr.castor.test.ws.service.AnnonceServiceTest;
import fr.castor.test.ws.service.AnnonceServiceTest;
import fr.castor.ws.client.service.AnnonceServiceREST;
import fr.castor.ws.dao.AnnonceDAO;
import fr.castor.ws.dao.ArtisanDAO;
import fr.castor.ws.dao.ClientDAO;
import fr.castor.ws.entity.Annonce;
import fr.castor.ws.entity.Artisan;
import fr.castor.ws.enums.PropertiesFileWS;
import fr.castor.ws.service.NotificationService;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

    @Inject
    private AnnonceServiceTest annonceServiceTest;

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
        annonceServiceTest.creationVerificationAnnonce(creationAnnonceDTO);
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
        annonceServiceTest.creationVerificationAnnonce(creationAnnonceDTO);
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
    public void testCreationAnnonceIsSignedInWithImage() throws IOException {
        creationAnnonceDTO.setIsSignedUp(true);

        Properties seleniumProperties = PropertiesFileWS.IMAGE.getProperties();
        StringBuilder adresseToImg = new StringBuilder(seleniumProperties.getProperty("app.temp.img.dir.test"));
        adresseToImg.append("castor.jpg");
        File castorFile = new File(adresseToImg.toString());
        creationAnnonceDTO.getPhotos().add(castorFile);

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
        annonceServiceTest.creationVerificationAnnonce(creationAnnonceDTO);
        Integer isCreationOK = annonceServiceREST.creationAnnonce(creationAnnonceDTO);
        // Le service doit remonter une erreur
        Assert.assertTrue(isCreationOK == CodeRetourService.ANNONCE_RETOUR_DUPLICATE);
    }

    /**
     * Cas de test : récupération de l'annonce d'un client par un client.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetAnnonceByIDForAffichage() {
        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.createDemandeAnnonceDTO(
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

        Assert.assertFalse(annonceAffichage.getAnnonce().getMotCles().isEmpty());
        annonceAffichage.getAnnonce().getMotCles().forEach(motCleDTO -> Assert.assertFalse(motCleDTO.getCategoriesMetier().isEmpty()));
    }

    /**
     * Cas de test : Récuperation d'une annonce par son id a partir d'un artisan
     * qui n'est pas inscrit à cette derniere.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetAnnonceByIDWithArtisanNotInscritAtAnnonce() {
        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.createDemandeAnnonceDTO(
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
        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.createDemandeAnnonceDTO(
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
        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.createDemandeAnnonceDTO(
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
                .setId("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
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
                .setId("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
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
                .setId("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
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
        demandeAnnonceDTO.setId("lolmdrp");
        demandeAnnonceDTO.setLoginDemandeur("pebronne");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.CLIENT);
        demandeAnnonceDTO.setLoginArtisanChoisi("pebronneChoisi");
        demandeAnnonceDTO.setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);

        Integer updateOK = annonceServiceREST.selectOneEnterprise(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, updateOK);

        annonceServiceTest.assertEntrepriseForChoixArtisanWithTransaction(false);
    }

    /**
     * Cas de test : Selection d'un artisan par un admin pour une annonce.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testAjoutChoixArtisanByAdminForAnnonce() {
        AnnonceSelectEntrepriseDTO demandeAnnonceDTO = new AnnonceSelectEntrepriseDTO();
        demandeAnnonceDTO.setId("lolmdrp");
        demandeAnnonceDTO.setLoginDemandeur("admin");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.ADMINISTRATEUR);
        demandeAnnonceDTO.setLoginArtisanChoisi("pebronneChoisi");
        demandeAnnonceDTO.setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);

        Integer updateOK = annonceServiceREST.selectOneEnterprise(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_OK, updateOK);

        annonceServiceTest.assertEntrepriseForChoixArtisanWithTransaction(false);
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
                .setId("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        demandeAnnonceDTO.setLoginDemandeur("doe");
        demandeAnnonceDTO.setTypeCompteDemandeur(TypeCompte.CLIENT);
        demandeAnnonceDTO.setLoginArtisanChoisi("pebronneChoisi");
        demandeAnnonceDTO.setAjoutOuSupprimeArtisan(AnnonceSelectEntrepriseDTO.AJOUT_ARTISAN);

        Integer updateKO = annonceServiceREST.selectOneEnterprise(demandeAnnonceDTO);

        Assert.assertEquals(CodeRetourService.RETOUR_KO, updateKO);

        annonceServiceTest.assertEntrepriseForChoixArtisanWithTransaction(true);
    }

    /**
     * Cas de test : l'artisan s'inscrit à l'annonce, il recoit une
     * notification, le tout doit etre correctement enregistré dans la BDD
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testInscriptionArtisanNominal() {

        String loginArtisan = "pebronneChoisi";

        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.initAndGetDemandeAnnonceDTO(
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
                .getLogin(), TypeCompte.CLIENT, 0, 15);
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

        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.initAndGetDemandeAnnonceDTO("lolmdrp", loginArtisan, TypeCompte.ARTISAN);

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

        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.initAndGetDemandeAnnonceDTO(
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

        DemandeAnnonceDTO demandeAnnonceDTO = annonceServiceTest.initAndGetDemandeAnnonceDTO("toto", loginArtisan, TypeCompte.ARTISAN);

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
        Annonce annonce = annonceServiceTest.testDesinscriptionArtisan("pebronne");
        Assert.assertEquals(1, annonce.getArtisans().size());
    }

    /**
     * Cas de test : Un administrateur ne veut pas d'un artisan et le desinscrit
     * d'une annonce
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testDesinscriptionArtisanAvecIDValidByAdmin() {
        Annonce annonce = annonceServiceTest.testDesinscriptionArtisan("admin");
        Assert.assertEquals(1, annonce.getArtisans().size());
    }

    /**
     * Cas de test : Un administrateur ne veut pas d'un artisan et le desinscrit
     * d'une annonce
     */
    @Test
    @UsingDataSet("datasets/in/annonce_5_artisans.yml")
    public void testDesinscriptionArtisanAvecIDValidReactivationAnnonce() {
        Annonce annonce = annonceServiceTest.testDesinscriptionArtisan("pebronne");
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
        AvisArtisanDTO noterArtisanDTO = annonceServiceTest.createNotationDTO("pebronne");

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
        AvisArtisanDTO noterArtisanDTO = annonceServiceTest.createNotationDTO("admin");

        Integer codeRetour = annonceServiceREST.noterUnArtisan(noterArtisanDTO);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetour);
    }

    /**
     * Cas de test : Un client veut modifier son annonce, tout se passe comme prévu
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/out/modification_annonce_par_client.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "motcle_fk", "annonce_fk"})
    public void testModificationAnnonceParClient() {
        annonceServiceTest.testModificationAnnonce(TypeCompte.CLIENT, "pebronne", CodeRetourService.RETOUR_OK);
    }

    /**
     * Cas de test : Un admin veut modifier l'annonce d'un client tout se passe comme prévu.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/out/modification_annonce_par_client.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "motcle_fk", "annonce_fk"})
    public void testModificationAnnonceParAdmin() {
        annonceServiceTest.testModificationAnnonce(TypeCompte.ADMINISTRATEUR, "admin", CodeRetourService.RETOUR_OK);
    }

    /**
     * Cas de test : Un client ne possédant pas l'annonce veut la modifier, On lui refuse.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/in/annonces_by_id.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "motcle_fk", "annonce_fk"})
    public void testModificationAnnonceParClientNotAllowed() {
        annonceServiceTest.testModificationAnnonce(TypeCompte.CLIENT, "bertrand", CodeRetourService.ANNONCE_RETOUR_INTROUVABLE);
    }

    /**
     * Cas de test : Un client veut rajouter des photos à son annonce, tout se passe comme prévu
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/out/ajout_photo_annonce.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "url"})
    public void testAjoutPhotoParClient() throws IOException {
        annonceServiceTest.testAjoutPhoto("pebronne", 2);
    }

    /**
     * Cas de test : Un client veut rajouter des photos à son annonce, mais il a déjà cinq photos a son annonce, le webservice refuse.
     */
    @Test
    @UsingDataSet("datasets/in/annonce_limite_photo.yml")
    @ShouldMatchDataSet(value = "datasets/in/annonce_limite_photo.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "url"})
    public void testAjoutPhotoParClientTropDePhoto() throws IOException {
        annonceServiceTest.testAjoutPhoto("pebronne", 5);
    }

    /**
     * Cas de test : Un client veut rajouter des photos à son annonce, tout se passe comme prévu
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/out/ajout_photo_annonce.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "url"})
    public void testAjoutPhotoParAdmin() throws IOException {
        annonceServiceTest.testAjoutPhoto("admin", 2);
    }

    /**
     * Cas de test : Un client veut rajouter des photos à son annonce, mais il ne la possede pas, on lui refuse l'accés.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    @ShouldMatchDataSet(value = "datasets/in/annonces_by_id.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "url"})
    public void testAjoutPhotoClientNotAllowed() throws IOException {
        annonceServiceTest.testAjoutPhoto("bertrand", 0);
    }

    /**
     * Cas de test : Un client veut récuperer les photos liées à son annonce, tout se passe comme prévu.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetPhotoAnnonceByClient() {
        List<ImageDTO> imageDTOs = annonceServiceTest.testGetPhoto("pebronne");

        Assert.assertNotNull(imageDTOs);
        Assert.assertEquals(1, imageDTOs.size());

        for (ImageDTO imageDTO : imageDTOs) {
            Assert.assertFalse(imageDTO.getUrl().isEmpty());
        }
    }

    /**
     * Cas de test : Un admin veut récuperer les photos liées à son annonce, tout se passe comme prévu.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetPhotoAnnonceByAdmin() {
        List<ImageDTO> imageDTOs = annonceServiceTest.testGetPhoto("admin");

        Assert.assertNotNull(imageDTOs);
        Assert.assertEquals(1, imageDTOs.size());

        for (ImageDTO imageDTO : imageDTOs) {
            Assert.assertFalse(imageDTO.getUrl().isEmpty());
        }
    }

    /**
     * Cas de test : Un client veut récuperer les photos liées à son annonce, mais celui ci ne la possede pas.
     */
    @Test
    @UsingDataSet("datasets/in/annonces_by_id.yml")
    public void testGetPhotoAnnonceByClientNotAllowed() {
        List<ImageDTO> imageDTOs = annonceServiceTest.testGetPhoto("bertrand");
        Assert.assertNotNull(imageDTOs);
        Assert.assertEquals(0, imageDTOs.size());
    }

    /**
     * Cas de test : Un client veut supprimer une des photos liées à son annonce, tout se passe comme prévu.
     */
    @Test
    @UsingDataSet("datasets/in/annonce_suppression_img.yml")
    @ShouldMatchDataSet(value = "datasets/in/annonce_suppression_img.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "url"})
    public void testSuppressPhotoAnnonceByClient() throws IOException {
        List<ImageDTO> imageDTOs = annonceServiceTest.testSuppressionPhoto("pebronne", true);
        Assert.assertNotNull(imageDTOs);
        Assert.assertEquals(2, imageDTOs.size());
    }

    /**
     * Cas de test : Un admin veut supprimer une des photos liées à son annonce, tout se passe comme prévu.
     */
    @Test
    @UsingDataSet("datasets/in/annonce_suppression_img.yml")
    @ShouldMatchDataSet(value = "datasets/in/annonce_suppression_img.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "url"})
    public void testSuppressPhotoAnnonceByAdmin() throws IOException {
        List<ImageDTO> imageDTOs = annonceServiceTest.testSuppressionPhoto("admin", true);
        Assert.assertNotNull(imageDTOs);
        Assert.assertEquals(2, imageDTOs.size());
    }

    /**
     * Cas de test : Un client veut supprimer une des photos liées à son annonce, mais il n'a pas les droits
     * On lui refuse l'accés
     */
    @Test
    @UsingDataSet("datasets/in/annonce_suppression_img.yml")
    @ShouldMatchDataSet(value = "datasets/in/annonce_suppression_img.yml", excludeColumns = {"id",
            "datemaj", "datecreation", "datenotation", "datenotification", "url"})
    public void testSuppressPhotoAnnonceByClientNotAllowed() throws IOException {
        List<ImageDTO> imageDTOs = annonceServiceTest.testSuppressionPhoto("bertrand", false);
        Assert.assertNotNull(imageDTOs);
        Assert.assertEquals(0, imageDTOs.size());
    }

    /**
     * Cas de test : Un artisan veut rechecher des annonces
     */
    @Test
    @UsingDataSet("datasets/in/search_annonce.yml")
    public void testRechercheAnnonceNominal() {
        List<CategorieMetierDTO> categorieMetierDTOs = new ArrayList<>();
        categorieMetierDTOs.add(Categorie.getElectricite());
        annonceServiceTest.searchAnnonce("pebronneArtisanne", categorieMetierDTOs, 2);
    }

    /**
     * Cas de test : Un admin veut rechecher des annonces
     */
    @Test
    @UsingDataSet("datasets/in/search_annonce.yml")
    public void testRechercheAnnonceParAdmin() {
        List<CategorieMetierDTO> categorieMetierDTOs = new ArrayList<>();
        categorieMetierDTOs.add(Categorie.getElectricite());
        annonceServiceTest.searchAnnonce("admin", categorieMetierDTOs, 2);
    }

    /**
     * Cas de test : Un admin veut rechecher des annonces dans plusieurs catégories
     */
    @Test
    @UsingDataSet("datasets/in/search_annonce.yml")
    public void testRechercheAnnonceParArtisanParPlusieursCategories() {
        List<CategorieMetierDTO> categorieMetierDTOs = new ArrayList<>();
        categorieMetierDTOs.add(Categorie.getPlomberie());
        categorieMetierDTOs.add(Categorie.getMaconnerie());
        categorieMetierDTOs.add(Categorie.getElectricite());
        annonceServiceTest.searchAnnonce("admin", categorieMetierDTOs, 3);
    }
}