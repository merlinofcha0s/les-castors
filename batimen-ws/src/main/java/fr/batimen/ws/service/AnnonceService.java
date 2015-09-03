package fr.batimen.ws.service;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.exception.BackendException;
import fr.batimen.core.exception.DuplicateEntityException;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.*;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.dto.helper.DeserializeJsonHelper;
import fr.batimen.ws.dao.*;
import fr.batimen.ws.entity.*;
import fr.batimen.ws.enums.PropertiesFileWS;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.*;

/**
 * Classe de gestion des annonces
 *
 * @author Casaucau Cyril
 */
@Stateless(name = "AnnonceService")
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AnnonceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceService.class);
    private final ModelMapper mapper = new ModelMapper();
    @Inject
    private AdresseDAO adresseDAO;
    @Inject
    private ClientDAO clientDAO;
    @Inject
    private PermissionDAO permissionDAO;
    @Inject
    private AnnonceDAO annonceDAO;
    @Inject
    private CategorieMetierDAO categorieMetierDAO;
    @Inject
    private MotCleDAO motCleDAO;

    /**
     * Crée une entité annonce a partir d'une DTO CreationAnnonce.
     *
     * @param nouvelleAnnonceDTO L'objet provenant du backend contenant les infos de l'annonce.
     * @return Entité Annonce
     * @throws BackendException
     */
    public Annonce remplirAnnonce(CreationAnnonceDTO nouvelleAnnonceDTO) throws BackendException,
            DuplicateEntityException {

        Annonce nouvelleAnnonce = new Annonce();

        // On rempli, on persiste et on bind l'adresse à l'annonce.
        nouvelleAnnonce.setAdresseChantier(remplirAndPersistAdresse(nouvelleAnnonceDTO));

        if (nouvelleAnnonceDTO.getIsSignedUp()) {
            isSignedUp(nouvelleAnnonceDTO, nouvelleAnnonce);
        } else {
            isNotSignedUp(nouvelleAnnonceDTO, nouvelleAnnonce);
        }

        // On rempli l'entité annonce grace à la DTO
        nouvelleAnnonce.setDateCreation(new Date());
        nouvelleAnnonce.setDateMAJ(new Date());
        nouvelleAnnonce.setDelaiIntervention(nouvelleAnnonceDTO.getDelaiIntervention());
        nouvelleAnnonce.setDescription(nouvelleAnnonceDTO.getDescription());
        nouvelleAnnonce.setNbConsultation(0);
        nouvelleAnnonce.setTypeContact(nouvelleAnnonceDTO.getTypeContact());
        nouvelleAnnonce.setTypeTravaux(nouvelleAnnonceDTO.getTypeTravaux());

        return nouvelleAnnonce;
    }

    /**
     * Cette méthode à sert aller chercher un client qui est deja inscit pour le
     * binder à l'annonce.
     *
     * @param nouvelleAnnonceDTO objet provenant du front
     * @param nouvelleAnnonce    entité qui sera persisté
     */
    private void isSignedUp(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) throws BackendException,
            DuplicateEntityException {
        Client client = clientDAO.getClientByLoginName(nouvelleAnnonceDTO.getClient().getLogin());
        if (client != null) {
            nouvelleAnnonce.setDemandeur(client);
            client.getDevisDemandes().add(nouvelleAnnonce);
            clientDAO.saveClient(client);
            nouvelleAnnonce.setEtatAnnonce(EtatAnnonce.ACTIVE);
        } else {
            throw new BackendException("Impossible de retrouver le client : "
                    + nouvelleAnnonceDTO.getClient().getLogin());
        }
    }

    /**
     * Methode qui permet de populer l'entité client grace à la
     * CreationAnnonceDTO et a la persister dans la BDD. Dans le cas d'une
     * inscription.
     *
     * @param nouvelleAnnonceDTO objet provenant du front
     * @param nouvelleAnnonce    entité qui sera persisté
     * @throws BackendException en cas de probleme de sauvegarde dans la BDD
     */
    private void isNotSignedUp(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) throws BackendException,
            DuplicateEntityException {
        Client nouveauClient = remplirClient(nouvelleAnnonceDTO, nouvelleAnnonce);
        clientDAO.saveNewClient(nouveauClient);
        // On set les permissions
        for (PermissionDTO permission : nouvelleAnnonceDTO.getClient().getPermissions()) {
            Permission nouvellePermission = new Permission();
            nouvellePermission.setTypeCompte(permission.getTypeCompte());
            nouvellePermission.setClient(nouveauClient);
            permissionDAO.creationPermission(nouvellePermission);
        }
        if (nouveauClient != null) {
            nouvelleAnnonce.setDemandeur(nouveauClient);
            nouvelleAnnonce.setEtatAnnonce(EtatAnnonce.EN_ATTENTE);
        }
    }

    /**
     * Rempli une entité Adresse grace à la DTO de création de l'annonce, puis
     * la persiste
     *
     * @param nouvelleAnnonceDTO
     * @return
     * @throws BackendException
     */
    private Adresse remplirAndPersistAdresse(CreationAnnonceDTO nouvelleAnnonceDTO) throws BackendException {

        // On crée la nouvelle adresse qui sera rattaché a l'annonce.
        Adresse adresseAnnonce = new Adresse();

        adresseAnnonce.setAdresse(nouvelleAnnonceDTO.getAdresse());
        adresseAnnonce.setComplementAdresse(nouvelleAnnonceDTO.getComplementAdresse());
        adresseAnnonce.setCodePostal(nouvelleAnnonceDTO.getCodePostal());
        adresseAnnonce.setVille(nouvelleAnnonceDTO.getVille());
        adresseAnnonce.setDepartement(nouvelleAnnonceDTO.getDepartement());

        adresseDAO.saveAdresse(adresseAnnonce);

        return adresseAnnonce;

    }

    /**
     * Rempli une entité Client grace à la DTO de création de l'annonce
     *
     * @param nouvelleAnnonceDTO
     * @param nouvelleAnnonce
     * @return
     */
    private Client remplirClient(CreationAnnonceDTO nouvelleAnnonceDTO, Annonce nouvelleAnnonce) {

        // On crée la nouveau client qui vient de poster la nouvelle annonce.
        Client nouveauClient = new Client();
        nouveauClient.setDateInscription(new Date());

        // On crée la liste des annonces.
        Set<Annonce> annoncesNouveauClient = new HashSet<Annonce>();
        annoncesNouveauClient.add(nouvelleAnnonce);

        // On bind le client à son annonce.
        nouveauClient.setDevisDemandes(annoncesNouveauClient);
        // On enregistre les infos du client dans l'entité client
        mapper.map(nouvelleAnnonceDTO.getClient(), nouveauClient);

        nouveauClient.setIsActive(false);

        StringBuilder loginAndEmail = new StringBuilder(nouvelleAnnonceDTO.getClient().getLogin());
        loginAndEmail.append(nouvelleAnnonceDTO.getClient().getEmail());

        nouveauClient.setCleActivation(HashHelper.convertToBase64(HashHelper.hashSHA256(loginAndEmail.toString())));

        return nouveauClient;
    }

    public void remplirSelAndHash(Annonce nouvelleAnnonce) {
        String salt = HashHelper.generateSalt();
        nouvelleAnnonce.setSelHashID(salt);
        nouvelleAnnonce.setHashID(HashHelper.hashID(nouvelleAnnonce.getId(), salt));
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public AnnonceAffichageDTO doMappingAnnonceAffichageDTO(Annonce annonce, AnnonceAffichageDTO annonceAffichageDTO,
                                                            Boolean isArtisan, Boolean isArtisanInscrit, Boolean isAdmin) {

        if (isArtisanInscrit || isAdmin) {
            annonceAffichageDTO.setTelephoneClient(annonce.getDemandeur().getNumeroTel());
            annonceAffichageDTO.setEmailClient(annonce.getDemandeur().getEmail());
        }

        AnnonceDTO annonceDTO = new AnnonceDTO();
        AdresseDTO adresseDTO = new AdresseDTO();

        annonceAffichageDTO.setAnnonce(annonceDTO);
        annonceAffichageDTO.setAdresse(adresseDTO);
        annonceAffichageDTO.setIsArtisanInscrit(isArtisanInscrit);
        // Remplissage données de l'annonce
        mapper.map(annonce, annonceDTO);
        annonceAffichageDTO.getAnnonce().setLoginOwner(annonce.getDemandeur().getLogin());
        mapper.map(annonce.getAdresseChantier(), adresseDTO);

        for (Image image : annonce.getImages()) {
            ImageDTO imageDTO = new ImageDTO();
            mapper.map(image, imageDTO);
            annonceAffichageDTO.getImages().add(imageDTO);
        }

        if (isArtisan) {
            return annonceAffichageDTO;
        }

        // Informations sur les artisans inscrits à l'annonce.
        // N'est envoyé vers le frontend que si et seulement
        // c'est un client qui a fait la demande.
        for (Artisan artisan : annonce.getArtisans()) {
            // Creation des objets de transferts
            ClientDTO artisanDTO = new ClientDTO();
            EntrepriseDTO entrepriseDTO = new EntrepriseDTO();
            // Transfert des données des entités vers les DTOs
            mapper.map(artisan, artisanDTO);
            mapper.map(artisan.getEntreprise(), entrepriseDTO);
            // On met en place les liens
            entrepriseDTO.setArtisan(artisanDTO);
            annonceAffichageDTO.getEntreprises().add(entrepriseDTO);
        }

        if (annonce.getEntrepriseSelectionnee() != null) {
            // Si il y a une entreprise selectionnée
            // Creation des objets de transferts
            ClientDTO artisanSelectionneDTO = new ClientDTO();
            EntrepriseDTO entrepriseSelectionneDTO = new EntrepriseDTO();
            // Transfert des données des entités vers les DTOs
            mapper.map(annonce.getEntrepriseSelectionnee(), entrepriseSelectionneDTO);
            mapper.map(annonce.getEntrepriseSelectionnee().getArtisan(), artisanSelectionneDTO);
            entrepriseSelectionneDTO.setArtisan(artisanSelectionneDTO);
            annonceAffichageDTO.setEntrepriseSelectionnee(entrepriseSelectionneDTO);
        }

        return annonceAffichageDTO;
    }

    public Integer inscrireArtisan(DemandeAnnonceDTO demandeAnnonceDTO, Annonce annonce, Artisan artisan) {

        // Si le quotas max est atteint => on sort du service
        if (annonce.getEtatAnnonce() == EtatAnnonce.QUOTA_MAX_ATTEINT) {
            return CodeRetourService.ANNONCE_RETOUR_QUOTA_DEVIS_ATTEINT;
        }

        // Si c'est le dernier artisan avant qu'on atteigne le quota, on change
        // l'etat de l'annonce.
        Properties propertiesCastor = PropertiesFileWS.CASTOR.getProperties();
        int nbMaxArtisanParAnnonce = Integer.valueOf(propertiesCastor.getProperty("prop.nb.max.artisan.annonce"));

        if (annonce.getArtisans().size() == nbMaxArtisanParAnnonce - 1) {
            annonce.setEtatAnnonce(EtatAnnonce.QUOTA_MAX_ATTEINT);
        }

        for (Artisan artisanInscrit : annonce.getArtisans()) {
            if (artisanInscrit.getLogin().equals(artisan.getLogin())) {
                return CodeRetourService.ANNONCE_RETOUR_ARTISAN_DEJA_INSCRIT;
            }
        }

        annonce.getArtisans().add(artisan);
        artisan.getAnnonces().add(annonce);

        return CodeRetourService.RETOUR_OK;
    }

    public Integer desactivateAnnoncePerime() {
        // Récuperation des properties
        Properties castorProperties = PropertiesFileWS.CASTOR.getProperties();
        int nbJourAvantPeremption = Integer.valueOf(castorProperties.getProperty("prop.temps.peremption.annonce"));
        Integer nbMaxArtisanParAnnonce = Integer.valueOf(castorProperties.getProperty("prop.nb.max.artisan.annonce"));

        Calendar calJourPeremptionAnnonce = Calendar.getInstance(Locale.FRANCE);
        calJourPeremptionAnnonce.add(Calendar.DAY_OF_MONTH, -nbJourAvantPeremption);

        annonceDAO.desactiveAnnoncePerime(calJourPeremptionAnnonce.getTime(), nbMaxArtisanParAnnonce);

        return CodeRetourService.RETOUR_OK;
    }

    /**
     * Permet de récuperer les annonces d'un client à partir de son login <br/>
     * Service servant principalement a la page mes annonces
     *
     * @param login l'identifiant de l'utilisateur
     * @return La liste des annonces de cet utilisateur
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnnonceDTO> getAnnoncesByClientLoginForMesAnnonces(String login, boolean isArtisan) {
        // On escape les ""
        String loginEscaped = DeserializeJsonHelper.parseString(login);
        // On recupere les annonces de l'utilisateur
        List<Object[]> queryAnnoncesResult = annonceDAO.getAnnoncesByLoginForMesAnnonces(loginEscaped, isArtisan);
        // On crée la liste qui accueuillera les DTO
        List<AnnonceDTO> annoncesDTO = new ArrayList<AnnonceDTO>();

        for (Object[] annonce : queryAnnoncesResult) {
            // On crée le nouvel objet
            AnnonceDTO annonceDTO = new AnnonceDTO();
            // On transfert les données d'un objet a l'autre
            annonceDTO.setDescription((String) annonce[0]);
            annonceDTO.setEtatAnnonce((EtatAnnonce) annonce[1]);
            Long nbDevis = (Long) annonce[2];
            annonceDTO.setNbDevis(nbDevis);
            annonceDTO.setHashID(String.valueOf(annonce[3]));

            // On ajoute à la liste
            annoncesDTO.add(annonceDTO);
        }

        return annoncesDTO;
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void remplirMotCle(Annonce annonce, List<MotCleDTO> motCleDTOs) {

        annonce.getMotcles().clear();
        motCleDAO.deleteAllMotsCles(annonce.getMotcles());


        motCleDTOs.forEach(motCleDTO -> {

            //boolean motCleNotPresent = annonce.getMotcles().stream().noneMatch(motCle -> motCle.getMotCle().equals(motCleDTO.getMotCle()));

            MotCle motCle = new MotCle();
            motCle.setMotCle(motCleDTO.getMotCle());
            motCle.setAnnonce(annonce);

            motCleDTO.getCategoriesMetier().forEach(categorie -> {
                CategorieMetier categorieMetier = new CategorieMetier();
                categorieMetier.setCategorieMetier(categorie.getCategorieMetier());
                categorieMetier.setMotCle(motCle);
                categorieMetierDAO.persistCategorieMetier(categorieMetier);
                motCle.getCategoriesMetier().add(categorieMetier);
            });

            motCleDAO.persistMotCle(motCle);
            annonce.getMotcles().add(motCle);

        });
    }
}