package fr.batimen.web.client.extend.connected;

import fr.batimen.dto.*;
import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.web.app.constants.ParamsConstant;
import fr.batimen.web.client.component.*;
import fr.batimen.web.client.master.MasterPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Casaucau on 02/06/2015.
 */
public class Entreprise extends MasterPage {

    //SIRET
    private String idEntreprise;

    private static final String REFRESH_TOOLTIP_ON_CATEGORIE_TYPE = "$('.categorie-entreprise').tooltip()";

    private EntrepriseDTO entrepriseDTO;

    private Model<String> specialiteModel;
    private Model<String> siretModel;
    private Model<StatutJuridique> statutJuridiqueModel;
    private Model<Integer> nombreEmployesModel;
    private Model<String> dateCreationModel;

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
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forUrl("css/font_icons8.css"));
        response.render(OnDomReadyHeaderItem.forScript(REFRESH_TOOLTIP_ON_CATEGORIE_TYPE));
    }

    private void initComposants() {
        Profil profil = new Profil("profil", entrepriseDTO.isVerified());
        ContactezNous contactezNous = new ContactezNous("contactezNous");
        Commentaire commentaire = new Commentaire("commentaire");

        this.add(profil, contactezNous, commentaire);
    }

    private void loadEntrepriseData() {
        entrepriseDTO = new EntrepriseDTO();
        entrepriseDTO.setDateCreation(new Date());
        entrepriseDTO.setSiret("73282932000074");
        entrepriseDTO.setStatutJuridique(StatutJuridique.SARL);
        entrepriseDTO.setNomComplet("Entreprise de toto");
        entrepriseDTO.setNbEmployees(10);
        entrepriseDTO.setSpecialite("Peinture sur bois");

        entrepriseDTO.getCategoriesMetier().add(CategorieLoader.getCategorieDecorationMaconnerie());
        entrepriseDTO.getCategoriesMetier().add(CategorieLoader.getCategoriePlomberie());

        AdresseDTO adresseDTO = new AdresseDTO();
        adresseDTO.setAdresse("250 chemin de toto");
        adresseDTO.setComplementAdresse("Residence de toto");
        adresseDTO.setDepartement(06);
        adresseDTO.setCodePostal("06800");
        adresseDTO.setVille("Toto ville");

        entrepriseDTO.setAdresseEntreprise(adresseDTO);

        ClientDTO client = new ClientDTO();
        client.setEmail("toto@entreprise.com");
        client.setNumeroTel("0615124585");

        entrepriseDTO.setArtisan(client);

        NotationDTO notationDTO = new NotationDTO();
        notationDTO.setCommentaire("Bon travaux, artisan sérieux, Bon travaux, artisan sérieux, Bon travaux, artisan sérieux, Bon travaux, artisan sérieux, Bon travaux, artisan sérieux, Bon travaux, artisan sérieux");
        notationDTO.setNomPrenomOrLoginClient("Robert Patoulachi");
        notationDTO.setScore((double)4);

        NotationDTO notationDTO2 = new NotationDTO();
        notationDTO2.setCommentaire("Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay");
        notationDTO2.setNomPrenomOrLoginClient("Robert Patoulachi");
        notationDTO2.setScore((double)5);

        NotationDTO notationDTO6 = new NotationDTO();
        notationDTO6.setCommentaire("Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay");
        notationDTO6.setNomPrenomOrLoginClient("Robert Patoulachi");
        notationDTO6.setScore((double)5);

        NotationDTO notationDTO3 = new NotationDTO();
        notationDTO3.setCommentaire("Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay");
        notationDTO3.setNomPrenomOrLoginClient("Robert Patoulachi");
        notationDTO3.setScore((double)5);

        NotationDTO notationDTO4 = new NotationDTO();
        notationDTO4.setCommentaire("Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay");
        notationDTO4.setNomPrenomOrLoginClient("Robert Patoulachi");
        notationDTO4.setScore((double)1);

        NotationDTO notationDTO5 = new NotationDTO();
        notationDTO5.setCommentaire("Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulay, Mega boulayy");
        notationDTO5.setNomPrenomOrLoginClient("Robert Patoulachi");
        notationDTO5.setScore((double)1);

        entrepriseDTO.getNotationsDTO().add(notationDTO);
        entrepriseDTO.getNotationsDTO().add(notationDTO2);
        entrepriseDTO.getNotationsDTO().add(notationDTO3);
        entrepriseDTO.getNotationsDTO().add(notationDTO4);
        entrepriseDTO.getNotationsDTO().add(notationDTO5);
        entrepriseDTO.getNotationsDTO().add(notationDTO6);

        entrepriseDTO.setIsVerified(false);
    }

    private void initInformationsGenerales() {
        Label specialite = new Label("specialite", specialiteModel);
        Label siret = new Label("siret", siretModel);
        Label statutJuridique = new Label("statutJuridique", statutJuridiqueModel);
        Label nombreEmployes = new Label("nombreEmployes", nombreEmployesModel);
        Label dateCreation = new Label("dateCreation", dateCreationModel);

        add(specialite, siret, statutJuridique, nombreEmployes, dateCreation);
    }

    private void initModelInformationsGenerales() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        specialiteModel = new Model<>();
        siretModel = new Model<>();
        statutJuridiqueModel = new Model<>();
        nombreEmployesModel = new Model<>();
        dateCreationModel = new Model<>();

        if (entrepriseDTO.getSpecialite() == null || entrepriseDTO.getSpecialite().isEmpty()) {
            specialiteModel.setObject("non renseignée");
        } else {
            specialiteModel.setObject(entrepriseDTO.getSpecialite());
        }

        if (entrepriseDTO.getDateCreation() == null) {
            dateCreationModel.setObject("non renseignée");
        } else {
            dateCreationModel.setObject(sdf.format(entrepriseDTO.getDateCreation()));
        }

        siretModel.setObject(entrepriseDTO.getSiret());
        statutJuridiqueModel.setObject(entrepriseDTO.getStatutJuridique());
        nombreEmployesModel.setObject(entrepriseDTO.getNbEmployees());
    }

    private void initCategorieEntreprise() {
        ListView<CategorieMetierDTO> categorieMetierDTOListView = new ListView<CategorieMetierDTO>("categorieMetier", entrepriseDTO.getCategoriesMetier()) {
            @Override
            protected void populateItem(ListItem<CategorieMetierDTO> item) {
                CategorieMetierDTO categorieMetierDTO = item.getModelObject();

                if (categorieMetierDTO.getCodeCategorieMetier().equals(CategorieLoader.ELECTRICITE_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Electricité"));
                    item.add(new AttributeModifier("title", "Electricité"));
                    item.add(new AttributeModifier("class", "icon-Lightning font-glyph-entreprise categorie-entreprise"));
                }

                if (categorieMetierDTO.getCodeCategorieMetier().equals(CategorieLoader.PLOMBERIE_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Plomberie"));
                    item.add(new AttributeModifier("title", "Plomberie"));
                    item.add(new AttributeModifier("class", "icons8-plumbing font-glyph-entreprise categorie-entreprise icons-8-entreprise"));
                }

                if (categorieMetierDTO.getCodeCategorieMetier().equals(CategorieLoader.ESPACE_VERT_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Espaces verts"));
                    item.add(new AttributeModifier("title", "Espaces verts"));
                    item.add(new AttributeModifier("class", "icons8-garden-shears font-glyph-entreprise categorie-entreprise icons-8-entreprise"));
                }

                if (categorieMetierDTO.getCodeCategorieMetier().equals(CategorieLoader.DECORATION_MACONNERIE_CODE)) {
                    item.add(new AttributeModifier("data-original-title", "Décoration / Maconnerie"));
                    item.add(new AttributeModifier("title", "Décoration / Maconnerie"));
                    item.add(new AttributeModifier("class", "icon-Tool font-glyph-entreprise categorie-entreprise"));
                }
            }
        };

        add(categorieMetierDTOListView);
    }

    private void initContactEntreprise() {
        StringBuilder adresseComplete = new StringBuilder(entrepriseDTO.getAdresseEntreprise().getAdresse());
        adresseComplete.append(" ");

        if (!entrepriseDTO.getAdresseEntreprise().getComplementAdresse().isEmpty()) {
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

    private void initAvisContainer(){
        PageableListView<NotationDTO> notationDTOListView = new PageableListView<NotationDTO>("avisClientContainer", entrepriseDTO.getNotationsDTO(), 2) {
            @Override
            protected void populateItem(ListItem<NotationDTO> item) {
                RaterCastor rater = new RaterCastor("rater",  item.getModelObject(), false);
                item.add(rater);
            }
        };

        CastorAjaxPagingNavigator pagerAvis = new CastorAjaxPagingNavigator("pagerAvis", notationDTOListView);

        RaterStarsCastor moyenneRater = new RaterStarsCastor("moyenneRater");
        moyenneRater.setIsReadOnly(true);

        Double moyenneAvis = 0.0;
        int nbAvis = 0;
        for(NotationDTO notationDTO : entrepriseDTO.getNotationsDTO()){
            moyenneAvis += notationDTO.getScore();
            nbAvis++;
        }

        moyenneAvis = moyenneAvis / nbAvis;
        moyenneRater.setNumberOfStars(moyenneAvis.intValue());

        add(notationDTOListView, pagerAvis, moyenneRater);
    }
}