package fr.batimen.test.ws.service;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.ImageDTO;
import fr.batimen.dto.NotationDTO;
import fr.batimen.dto.NotificationDTO;
import fr.batimen.dto.aggregate.*;
import fr.batimen.dto.enums.*;
import fr.batimen.ws.client.service.AnnonceServiceREST;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.ClientDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.entity.Client;
import fr.batimen.ws.service.NotificationService;
import org.junit.Assert;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.List;

/**
 * Classe contenant le code de test quand celui ci est utilisé a plusieurs endroits.
 *
 *
 * Created by Casaucau on 30/04/2015.
 */
@ApplicationScoped
@Named
public class AnnonceServiceTest {

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

    public Integer testSuppressionPhoto(String loginDemandeur) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setUrl("https://res.cloudinary.com/lescastors/image/upload/v1427874120/test/zbeod6tioi6yrphpco39.jpg");
        SuppressionPhotoDTO suppressionPhotoDTO = new SuppressionPhotoDTO();
        suppressionPhotoDTO.setLoginDemandeur(loginDemandeur);
        suppressionPhotoDTO.setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        suppressionPhotoDTO.setImageASupprimer(imageDTO);
        return annonceServiceREST.suppressionPhoto(suppressionPhotoDTO);
    }

    public List<ImageDTO> testGetPhoto(String login) {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        demandeAnnonceDTO.setLoginDemandeur(login);

        return annonceServiceREST.getPhotos(demandeAnnonceDTO);
    }

    public void testAjoutPhoto(String login, int codeRetourAttendu) {
        // On recupére la photo dans les ressources de la webapp de test
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("img/castor.jpg").getFile());

        AjoutPhotoDTO ajoutPhotoDTO = new AjoutPhotoDTO();
        ajoutPhotoDTO.setLoginDemandeur(login);
        ajoutPhotoDTO.setHashID("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21");
        ajoutPhotoDTO.getImages().add(file);

        int codeRetour = annonceServiceREST.ajouterPhoto(ajoutPhotoDTO);

        Assert.assertNotNull(codeRetour);
        Assert.assertEquals(codeRetourAttendu, codeRetour);
    }

    public void testModificationAnnonce(TypeCompte typeCompte, String loginDemandeur, Integer codeRetourServiceAttendu) {
        DemandeAnnonceDTO demandeAnnonceDTO = createDemandeAnnonceDTO(
                "88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21",
                "pebronne", typeCompte);

        AnnonceAffichageDTO annonceAffichage = annonceServiceREST.getAnnonceByIDForAffichage(demandeAnnonceDTO);
        Assert.assertNotNull(annonceAffichage);

        ModificationAnnonceDTO modificationAnnonceDTO = new ModificationAnnonceDTO();
        modificationAnnonceDTO.setAnnonce(annonceAffichage.getAnnonce());
        modificationAnnonceDTO.getAnnonce().setHashID(demandeAnnonceDTO.getHashID());
        modificationAnnonceDTO.setAdresse(annonceAffichage.getAdresse());
        modificationAnnonceDTO.setLoginDemandeur(loginDemandeur);

        modificationAnnonceDTO.getAnnonce().setTypeContact(TypeContact.TELEPHONE);
        modificationAnnonceDTO.getAnnonce().setTypeTravaux(TypeTravaux.RENOVATION);
        modificationAnnonceDTO.getAnnonce().setDelaiIntervention(DelaiIntervention.SIX_MOIS);

        Integer codeRetourOK = annonceServiceREST.modifierAnnonce(modificationAnnonceDTO);

        Assert.assertNotNull(codeRetourOK);
        Assert.assertEquals(codeRetourServiceAttendu, codeRetourOK);
    }

    public NoterArtisanDTO createNotationDTO(String loginDemandeur) {
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

    public Annonce testDesinscriptionArtisan(String loginDemandeur) {
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

    public DemandeAnnonceDTO initAndGetDemandeAnnonceDTO(String hash, String loginDemandeur, TypeCompte typeCompte) {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID(hash);
        demandeAnnonceDTO.setLoginDemandeur(loginDemandeur);
        demandeAnnonceDTO.setTypeCompteDemandeur(typeCompte);
        return demandeAnnonceDTO;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void assertEntrepriseForChoixArtisanWithTransaction(boolean isSuppression) {
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

    public DemandeAnnonceDTO createDemandeAnnonceDTO(String hashID, String login, TypeCompte typeCompte) {
        DemandeAnnonceDTO demandeAnnonceDTO = new DemandeAnnonceDTO();
        demandeAnnonceDTO.setHashID(hashID);
        demandeAnnonceDTO.setLoginDemandeur(login);
        demandeAnnonceDTO.setTypeCompteDemandeur(typeCompte);
        return demandeAnnonceDTO;
    }

    public void creationVerificationAnnonce(CreationAnnonceDTO creationAnnonceDTO) {
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
