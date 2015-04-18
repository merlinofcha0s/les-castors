package fr.batimen.web.client.extend.member.client;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.SousCategorieMetierDTO;
import fr.batimen.dto.aggregate.AnnonceAffichageDTO;
import fr.batimen.dto.aggregate.CreationAnnonceDTO;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.client.component.Commentaire;
import fr.batimen.web.client.component.ContactezNous;
import fr.batimen.web.client.component.Profil;
import fr.batimen.web.client.extend.nouveau.devis.Etape3AnnonceForm;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * TODO Voir comment charger correctement la sous categorie (alternative à l'event)
 * TODO Modifier le design pour que ca rentre dans une seule colonne.
 *
 * Created by Casaucau on 17/04/2015.
 */
public class ModifierAnnonce extends MasterPage{

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifierAnnonce.class);

    private AnnonceAffichageDTO annonceAffichageDTO;
    private CompoundPropertyModel propertyModelModificationAnnonce;

    public ModifierAnnonce(){
        this((AnnonceAffichageDTO) null);
    }

    public ModifierAnnonce(AnnonceAffichageDTO annonceAffichageDTO){
        super("Modifier mon annonce", "lol", "Modifier mon annonce", true, "img/bg_title1.jpg");
        this.annonceAffichageDTO = annonceAffichageDTO;

        if(annonceAffichageDTO == null){
            this.setResponsePage(MesAnnonces.class);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init de la page de modification de mon profil");
        }

        initData();
        initComposant();
    }

    public ModifierAnnonce(PageParameters params, AnnonceAffichageDTO annonceAffichageDTO) {
        this(annonceAffichageDTO);
    }

    public ModifierAnnonce(PageParameters params) {
        this((AnnonceAffichageDTO) null);
    }


    private void initComposant() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des composants de la page de modification de mon annonce");
        }
        Profil profil = new Profil("profil");
        Etape3AnnonceForm etape3AnnonceForm = new Etape3AnnonceForm("formQualification", propertyModelModificationAnnonce);

        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, etape3AnnonceForm, contactezNous, commentaire);
    }

    private void initData() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Init des datas de la page de modification de mon annonce");
        }

        CreationAnnonceDTO creationAnnonceDTO = new CreationAnnonceDTO();
        ModelMapper mapper = new ModelMapper();
        mapper.map(annonceAffichageDTO.getAdresse(), creationAnnonceDTO);
        mapper.map(annonceAffichageDTO.getAnnonce(), creationAnnonceDTO);

        CategorieMetierDTO categorieMetierDTO = CategorieLoader.getCategorieByCode(annonceAffichageDTO.getAnnonce().getCategorieMetier());
        creationAnnonceDTO.setCategorieMetier(categorieMetierDTO);

        SousCategorieMetierDTO sousCategorie = new SousCategorieMetierDTO();

        for(SousCategorieMetierDTO sousCategorieMetierDTO : categorieMetierDTO.getSousCategories()){
            if(sousCategorieMetierDTO.getName().equals(annonceAffichageDTO.getAnnonce().getSousCategorieMetier())){
                sousCategorie = new SousCategorieMetierDTO(annonceAffichageDTO.getAnnonce().getSousCategorieMetier());
            }
        }

        if(sousCategorie != null){
            creationAnnonceDTO.setSousCategorie(sousCategorie);
        }

        propertyModelModificationAnnonce = new CompoundPropertyModel<CreationAnnonceDTO>(creationAnnonceDTO);
    }
}
