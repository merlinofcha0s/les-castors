package fr.castor.web.client.extend.connected;

import fr.castor.dto.AvisDTO;
import fr.castor.dto.CategorieMetierDTO;
import fr.castor.dto.EntrepriseDTO;
import fr.castor.dto.constant.Categorie;
import fr.castor.dto.enums.StatutJuridique;
import fr.castor.web.app.constants.ParamsConstant;
import fr.castor.web.client.component.*;
import fr.castor.web.client.master.MasterPage;
import fr.castor.dto.comparator.AvisComparatorParDate;
import fr.castor.ws.client.service.ArtisanServiceREST;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by Casaucau on 02/06/2015.
 */
public class Entreprise extends MasterPage {

    private static final String REFRESH_TOOLTIP_ON_CATEGORIE_TYPE = "$('.categorie-entreprise').tooltip()";
    @Inject
    private ArtisanServiceREST artisanServiceREST;
    //SIRET
    private String idEntreprise;
    private EntrepriseDTO entrepriseDTO;

    private LoadableDetachableModel<String> specialiteModel;
    private LoadableDetachableModel<String> siretModel;
    private LoadableDetachableModel<StatutJuridique> statutJuridiqueModel;
    private LoadableDetachableModel<Integer> nombreEmployesModel;
    private LoadableDetachableModel<String> dateCreationModel;
    private LoadableDetachableModel<Integer> nbAnnonceRemporteModel;
    private LoadableDetachableModel<String> titleEntrepriseModel;

    private boolean hasClickPlusDAvis = false;

    public Entreprise() {
        super("", "", "Entreprise partenaire", true, "img/bg_title1.jpg");
    }

    public Entreprise(PageParameters params) {
        this();
        idEntreprise = params.get(ParamsConstant.ID_ENTREPRISE_PARAM).toString();
        loadEntrepriseData();
        initComposants();
        initModelInformationsGenerales();
        initInformationsGenerales();
        initCategorieEntreprise();
        initContactEntreprise();
        initAvisContainer();
        initPhotoChantierTemoinContainer();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));
        response.render(OnDomReadyHeaderItem.forScript(REFRESH_TOOLTIP_ON_CATEGORIE_TYPE));
    }

    private void initComposants() {
        Profil profil = new Profil("profil", entrepriseDTO.getIsVerifier());
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, contactezNous, commentaire);
    }

    private void loadEntrepriseData() {
        entrepriseDTO = artisanServiceREST.getEntrepriseInformationBySiret(idEntreprise);

        Collections.sort(entrepriseDTO.getNotationsDTO(), new AvisComparatorParDate());
    }

    private void initInformationsGenerales() {
        Label titleEntreprise = new Label("titreEntreprise", titleEntrepriseModel);
        titleEntreprise.setMarkupId("titreEntreprise");
        Label specialite = new Label("specialite", specialiteModel);
        specialite.setMarkupId(specialite.getId());
        Label siret = new Label("siret", siretModel);
        siret.setMarkupId(siret.getId());
        Label statutJuridique = new Label("statutJuridique", statutJuridiqueModel);
        statutJuridique.setMarkupId(statutJuridique.getId());
        Label nombreEmployes = new Label("nombreEmployes", nombreEmployesModel);
        nombreEmployes.setMarkupId(nombreEmployes.getId());
        Label dateCreation = new Label("dateCreation", dateCreationModel);
        dateCreation.setMarkupId(dateCreation.getId());
        Label nbAnnonceRemporte = new Label("nbAnnonceRemporte", nbAnnonceRemporteModel);
        nbAnnonceRemporte.setMarkupId(nbAnnonceRemporte.getId());

        add(titleEntreprise, specialite, siret, statutJuridique, nombreEmployes, dateCreation, nbAnnonceRemporte);
    }

    private void initModelInformationsGenerales() {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        specialiteModel = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (entrepriseDTO.getSpecialite() == null || entrepriseDTO.getSpecialite().isEmpty()) {
                   return "non renseignée";
                } else {
                    return entrepriseDTO.getSpecialite();
                }
            }
        };
        siretModel = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return entrepriseDTO.getSiret();
            }
        };


        statutJuridiqueModel = new LoadableDetachableModel<StatutJuridique>() {
            @Override
            protected StatutJuridique load() {
                return entrepriseDTO.getStatutJuridique();
            }
        };


        nombreEmployesModel = new LoadableDetachableModel<Integer>() {
            @Override
            protected Integer load() {
                return entrepriseDTO.getNbEmployees();
            }
        };

        dateCreationModel = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (entrepriseDTO.getDateCreation() == null) {
                    return "non renseignée";
                } else {
                    return sdf.format(entrepriseDTO.getDateCreation());
                }
            }
        };

        nbAnnonceRemporteModel = new LoadableDetachableModel<Integer>() {
            @Override
            protected Integer load() {
                return entrepriseDTO.getNbAnnonce();
            }
        };

        titleEntrepriseModel = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return entrepriseDTO.getNomComplet();
            }
        };

    }

    private void initCategorieEntreprise() {
        ListView<CategorieMetierDTO> categorieMetierDTOListView = new ListView<CategorieMetierDTO>("categorieMetier", entrepriseDTO.getCategoriesMetier()) {
            @Override
            protected void populateItem(ListItem<CategorieMetierDTO> item) {
                CategorieMetierDTO categorieMetierDTO = item.getModelObject();

                if (categorieMetierDTO.getCategorieMetier().equals(Categorie.ELECTRICITE_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Electricité"));
                    item.add(new AttributeModifier("title", "Electricité"));
                    item.add(new AttributeModifier("class", "icon-Lightning font-glyph-entreprise categorie-entreprise"));
                }

                if (categorieMetierDTO.getCategorieMetier().equals(Categorie.PLOMBERIE_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Plomberie"));
                    item.add(new AttributeModifier("title", "Plomberie"));
                    item.add(new AttributeModifier("class", "icons8-plumbing font-glyph-entreprise categorie-entreprise icons-8-entreprise"));
                }

                if (categorieMetierDTO.getCategorieMetier().equals(Categorie.ESPACE_VERT_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Espaces verts"));
                    item.add(new AttributeModifier("title", "Espaces verts"));
                    item.add(new AttributeModifier("class", "icons8-garden-shears font-glyph-entreprise categorie-entreprise icons-8-entreprise"));
                }

                if (categorieMetierDTO.getCategorieMetier().equals(Categorie.DECORATION_MACONNERIE_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Décoration / Maconnerie"));
                    item.add(new AttributeModifier("title", "Décoration / Maconnerie"));
                    item.add(new AttributeModifier("class", "icon-Tool font-glyph-entreprise categorie-entreprise"));
                }

                if (categorieMetierDTO.getCategorieMetier().equals(Categorie.MENUISERIE_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Menuiserie"));
                    item.add(new AttributeModifier("title", "Menuiserie"));
                    item.add(new AttributeModifier("class", "icon-Forrst font-glyph-entreprise categorie-entreprise"));
                }
            }
        };

        add(categorieMetierDTOListView);
    }

    private void initContactEntreprise() {
        StringBuilder adresseComplete = new StringBuilder(entrepriseDTO.getAdresseEntreprise().getAdresse());
        adresseComplete.append(" ");

        if (entrepriseDTO.getAdresseEntreprise().getComplementAdresse() != null && !entrepriseDTO.getAdresseEntreprise().getComplementAdresse().isEmpty()) {
            adresseComplete.append(entrepriseDTO.getAdresseEntreprise().getComplementAdresse()).append(" ");
        }

        adresseComplete.append(entrepriseDTO.getAdresseEntreprise().getCodePostal()).append(" ")
                .append(entrepriseDTO.getAdresseEntreprise().getVille()).append(" ")
                .append(entrepriseDTO.getAdresseEntreprise().getDepartement());

        Label adresse = new Label("adresse", adresseComplete.toString());
        Label telephone = new Label("telephone", entrepriseDTO.getArtisan().getNumeroTel());
        SmartLinkLabel mail = new SmartLinkLabel("mail", entrepriseDTO.getArtisan().getEmail());

        add(adresse, telephone, mail);
    }

    private void initPhotoChantierTemoinContainer(){
        PhotosContainer photosChantierTemoin = new PhotosContainer("photoChantierTemoin", entrepriseDTO.getPhotosChantiersTemoins(), "Chantiers témoins", "h2", false){
            @Override
            public boolean isVisible() {
                return !entrepriseDTO.getPhotosChantiersTemoins().isEmpty();
            }
        };
        add(photosChantierTemoin);
    }

    private void initAvisContainer(){
        final WebMarkupContainer containerAvisClientListView = new WebMarkupContainer("containerAvisClientListView");

        final LoadableDetachableModel<List<AvisDTO>> avisClientListViewModel = new LoadableDetachableModel<List<AvisDTO>>() {

            @Override
            protected List<AvisDTO> load() {
                return entrepriseDTO.getNotationsDTO();
            }
        };

        final ListView<AvisDTO> avisClientListView = new ListView<AvisDTO>("avisClientListView", avisClientListViewModel) {
            @Override
            protected void populateItem(ListItem<AvisDTO> item) {
                RaterCastor rater = new RaterCastor("rater",  item.getModelObject(), false);
                item.add(rater);
            }
        };

        containerAvisClientListView.add(avisClientListView);
        containerAvisClientListView.setOutputMarkupId(true);
        containerAvisClientListView.setMarkupId("containerAvisClientListView");

        final AjaxLink plusDAvis = new AjaxLink("plusDAvis") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                entrepriseDTO.setNotationsDTO(artisanServiceREST.getEntrepriseNotationBySiret(idEntreprise));
                hasClickPlusDAvis = true;
                Collections.sort(entrepriseDTO.getNotationsDTO(), new AvisComparatorParDate());
                avisClientListViewModel.setObject(entrepriseDTO.getNotationsDTO());

                target.add(containerAvisClientListView, this);
            }

            @Override
            public boolean isVisible() {
                return !hasClickPlusDAvis;
            }
        };

        plusDAvis.setOutputMarkupId(true);

        RaterStarsCastor moyenneRater = new RaterStarsCastor("moyenneRater");
        moyenneRater.setIsReadOnly(true);

        moyenneRater.setNumberOfStars(entrepriseDTO.getMoyenneAvis().intValue());

        add(containerAvisClientListView, moyenneRater, plusDAvis);
    }
}