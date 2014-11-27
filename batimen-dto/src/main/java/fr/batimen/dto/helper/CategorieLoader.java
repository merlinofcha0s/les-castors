package fr.batimen.dto.helper;

import java.util.ArrayList;
import java.util.List;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.SousCategorieMetierDTO;

/**
 * Chargeur de catégorie, initialise et donne les differentes categorie / sous
 * catégorie metier.
 * 
 * @author Casaucau Cyril
 * 
 */
public class CategorieLoader {

    private static CategorieMetierDTO electricite;
    private static CategorieMetierDTO plomberie;
    private static CategorieMetierDTO espaceVert;
    private static CategorieMetierDTO decorationMaconnerie;
    private static CategorieMetierDTO toutesCategories;

    public static final Short ELECTRICITE_CODE = 0;
    public static final Short PLOMBERIE_CODE = 1;
    public static final Short ESPACE_VERT_CODE = 2;
    public static final Short DECORATION_MACONNERIE_CODE = 3;

    private CategorieLoader() {

    }

    public static synchronized CategorieMetierDTO getCategorieElectricite() {
        if (electricite == null) {
            electricite = new CategorieMetierDTO("Electricité", ELECTRICITE_CODE);
            SousCategorieMetierDTO tableauxElectriques = new SousCategorieMetierDTO("Tableaux électriques");
            SousCategorieMetierDTO reseauxBranchementElectrique = new SousCategorieMetierDTO(
                    "Réseaux-branchements électriques");
            SousCategorieMetierDTO domotiques = new SousCategorieMetierDTO("Domotiques");
            SousCategorieMetierDTO interphone = new SousCategorieMetierDTO("Interphone");
            SousCategorieMetierDTO antenneSatellite = new SousCategorieMetierDTO("Antenne et Satellite");
            SousCategorieMetierDTO alarme = new SousCategorieMetierDTO("Alarme");
            SousCategorieMetierDTO videoSurveillance = new SousCategorieMetierDTO("Vidéo surveillance");
            SousCategorieMetierDTO desenfumage = new SousCategorieMetierDTO("Desenfumage");

            electricite.addSousCategorie(tableauxElectriques);
            electricite.addSousCategorie(reseauxBranchementElectrique);
            electricite.addSousCategorie(domotiques);
            electricite.addSousCategorie(interphone);
            electricite.addSousCategorie(antenneSatellite);
            electricite.addSousCategorie(alarme);
            electricite.addSousCategorie(videoSurveillance);
            electricite.addSousCategorie(desenfumage);

        }
        return electricite;
    }

    public static synchronized CategorieMetierDTO getCategoriePlomberie() {
        if (plomberie == null) {
            plomberie = new CategorieMetierDTO("Plomberie", PLOMBERIE_CODE);
            SousCategorieMetierDTO sousPlomberie = new SousCategorieMetierDTO("Plomberie");
            SousCategorieMetierDTO petitTravaux = new SousCategorieMetierDTO("Petit travaux");
            SousCategorieMetierDTO installation = new SousCategorieMetierDTO("Installation");
            SousCategorieMetierDTO cumulus = new SousCategorieMetierDTO("Cumulus");
            SousCategorieMetierDTO chauffeEau = new SousCategorieMetierDTO("Chauffe eau / solaire");
            SousCategorieMetierDTO adoucisseurEau = new SousCategorieMetierDTO("Adoucisseur d'eau");
            SousCategorieMetierDTO chaudiere = new SousCategorieMetierDTO("Chaudière");
            SousCategorieMetierDTO salleDeBain = new SousCategorieMetierDTO("Salle de bain");
            SousCategorieMetierDTO sanitaires = new SousCategorieMetierDTO("Sanitaires");
            SousCategorieMetierDTO wc = new SousCategorieMetierDTO("W.C");
            SousCategorieMetierDTO sauna = new SousCategorieMetierDTO("Sauna");
            SousCategorieMetierDTO hamman = new SousCategorieMetierDTO("Hamman");
            SousCategorieMetierDTO climatisation = new SousCategorieMetierDTO("Climatisation");
            SousCategorieMetierDTO chambreFroide = new SousCategorieMetierDTO("Chambre froide");
            SousCategorieMetierDTO vmc = new SousCategorieMetierDTO("VMC");

            plomberie.addSousCategorie(sousPlomberie);
            plomberie.addSousCategorie(petitTravaux);
            plomberie.addSousCategorie(installation);
            plomberie.addSousCategorie(cumulus);
            plomberie.addSousCategorie(chauffeEau);
            plomberie.addSousCategorie(adoucisseurEau);
            plomberie.addSousCategorie(chaudiere);
            plomberie.addSousCategorie(salleDeBain);
            plomberie.addSousCategorie(sanitaires);
            plomberie.addSousCategorie(wc);
            plomberie.addSousCategorie(sauna);
            plomberie.addSousCategorie(hamman);
            plomberie.addSousCategorie(climatisation);
            plomberie.addSousCategorie(chambreFroide);
            plomberie.addSousCategorie(vmc);
        }
        return plomberie;
    }

    public static synchronized CategorieMetierDTO getCategorieEspaceVert() {
        if (espaceVert == null) {
            espaceVert = new CategorieMetierDTO("Espace Vert", ESPACE_VERT_CODE);
            SousCategorieMetierDTO cloture = new SousCategorieMetierDTO("Cloture");
            SousCategorieMetierDTO elagage = new SousCategorieMetierDTO("Elagage, Debroussaillage");
            SousCategorieMetierDTO arrosageAutomatique = new SousCategorieMetierDTO("Arrosage automatique");
            SousCategorieMetierDTO pepinieriste = new SousCategorieMetierDTO("Pépinieriste");
            SousCategorieMetierDTO entretienJardin = new SousCategorieMetierDTO("Entretien jardin");
            SousCategorieMetierDTO creationPiscine = new SousCategorieMetierDTO("Création piscine");
            SousCategorieMetierDTO poseLiner = new SousCategorieMetierDTO("Pose d'un liner");
            SousCategorieMetierDTO localTechnique = new SousCategorieMetierDTO("Local technique");
            SousCategorieMetierDTO entretienPiscine = new SousCategorieMetierDTO("Entretien piscine");
            SousCategorieMetierDTO couverturePiscine = new SousCategorieMetierDTO("Couverture piscine");
            SousCategorieMetierDTO poseMargelle = new SousCategorieMetierDTO("Pose d'une margelle");

            espaceVert.addSousCategorie(cloture);
            espaceVert.addSousCategorie(elagage);
            espaceVert.addSousCategorie(arrosageAutomatique);
            espaceVert.addSousCategorie(pepinieriste);
            espaceVert.addSousCategorie(entretienJardin);
            espaceVert.addSousCategorie(entretienJardin);
            espaceVert.addSousCategorie(creationPiscine);
            espaceVert.addSousCategorie(poseLiner);
            espaceVert.addSousCategorie(localTechnique);
            espaceVert.addSousCategorie(entretienPiscine);
            espaceVert.addSousCategorie(couverturePiscine);
            espaceVert.addSousCategorie(poseMargelle);
        }
        return espaceVert;
    }

    public static synchronized CategorieMetierDTO getCategorieDecorationMaconnerie() {
        if (decorationMaconnerie == null) {
            decorationMaconnerie = new CategorieMetierDTO("Décoration / Maçonnerie", DECORATION_MACONNERIE_CODE);

            SousCategorieMetierDTO sousDecorationMaconnerie = new SousCategorieMetierDTO("Peinture");
            SousCategorieMetierDTO porteFenetre = new SousCategorieMetierDTO("Porte / Fenètre");
            SousCategorieMetierDTO pluieDeLumiere = new SousCategorieMetierDTO("Pluie de lumière (Vélux)");
            SousCategorieMetierDTO portailFerronerie = new SousCategorieMetierDTO("Portail / Ferronerie");
            SousCategorieMetierDTO serrurier = new SousCategorieMetierDTO("Serrurier");
            SousCategorieMetierDTO storeVolet = new SousCategorieMetierDTO("Store / Volet");
            SousCategorieMetierDTO vitrerie = new SousCategorieMetierDTO("Vitrerie");
            SousCategorieMetierDTO peinture = new SousCategorieMetierDTO("Peinture");
            SousCategorieMetierDTO revetementDecoratif = new SousCategorieMetierDTO("Revétement décoratif");
            SousCategorieMetierDTO papierPeintTapisserie = new SousCategorieMetierDTO("Papier peint / Tapisserie");
            SousCategorieMetierDTO lambris = new SousCategorieMetierDTO("lambris");
            SousCategorieMetierDTO toiture = new SousCategorieMetierDTO("Toiture");
            SousCategorieMetierDTO charpente = new SousCategorieMetierDTO("Charpente en bois");
            SousCategorieMetierDTO etancheite = new SousCategorieMetierDTO("Etanchéité");
            SousCategorieMetierDTO gouttiere = new SousCategorieMetierDTO("Gouttière");
            SousCategorieMetierDTO traitementBoisCharpente = new SousCategorieMetierDTO("Traitement bois charpente");
            SousCategorieMetierDTO combles = new SousCategorieMetierDTO("Combles");
            SousCategorieMetierDTO constructionBois = new SousCategorieMetierDTO("Construction bois");
            SousCategorieMetierDTO constructionMaison = new SousCategorieMetierDTO("Construction maison");
            SousCategorieMetierDTO extensionMaison = new SousCategorieMetierDTO("Extension maison");
            SousCategorieMetierDTO fondation = new SousCategorieMetierDTO("Fondation");
            SousCategorieMetierDTO ascenseur = new SousCategorieMetierDTO("Ascenseur");
            SousCategorieMetierDTO carrelage = new SousCategorieMetierDTO("Carrelage");
            SousCategorieMetierDTO surelevation = new SousCategorieMetierDTO("Surélévation");
            SousCategorieMetierDTO constructionAnnexes = new SousCategorieMetierDTO("Construction annexes");

            decorationMaconnerie.addSousCategorie(porteFenetre);
            decorationMaconnerie.addSousCategorie(pluieDeLumiere);
            decorationMaconnerie.addSousCategorie(portailFerronerie);
            decorationMaconnerie.addSousCategorie(serrurier);
            decorationMaconnerie.addSousCategorie(storeVolet);
            decorationMaconnerie.addSousCategorie(vitrerie);
            decorationMaconnerie.addSousCategorie(peinture);
            decorationMaconnerie.addSousCategorie(revetementDecoratif);
            decorationMaconnerie.addSousCategorie(papierPeintTapisserie);
            decorationMaconnerie.addSousCategorie(lambris);
            decorationMaconnerie.addSousCategorie(toiture);
            decorationMaconnerie.addSousCategorie(charpente);
            decorationMaconnerie.addSousCategorie(etancheite);
            decorationMaconnerie.addSousCategorie(gouttiere);
            decorationMaconnerie.addSousCategorie(traitementBoisCharpente);
            decorationMaconnerie.addSousCategorie(combles);
            decorationMaconnerie.addSousCategorie(constructionBois);
            decorationMaconnerie.addSousCategorie(constructionMaison);
            decorationMaconnerie.addSousCategorie(extensionMaison);
            decorationMaconnerie.addSousCategorie(fondation);
            decorationMaconnerie.addSousCategorie(ascenseur);
            decorationMaconnerie.addSousCategorie(carrelage);
            decorationMaconnerie.addSousCategorie(surelevation);
            decorationMaconnerie.addSousCategorie(constructionAnnexes);
            decorationMaconnerie.addSousCategorie(sousDecorationMaconnerie);
        }
        return decorationMaconnerie;
    }

    public static synchronized CategorieMetierDTO getCategorieAll() {
        if (toutesCategories == null) {
            toutesCategories = new CategorieMetierDTO("Toutes les catégories", (short) 6);
        }
        return toutesCategories;
    }

    public static synchronized List<CategorieMetierDTO> getAllCategories() {
        List<CategorieMetierDTO> allCategories = new ArrayList<CategorieMetierDTO>();
        allCategories.add(getCategorieDecorationMaconnerie());
        allCategories.add(getCategorieElectricite());
        allCategories.add(getCategorieEspaceVert());
        allCategories.add(getCategoriePlomberie());

        return allCategories;
    }

    public static String getIconForCategorie(Short codeCategorieMetier) {
        if (codeCategorieMetier.equals(ELECTRICITE_CODE)) {
            return "icon-Lightning";
        } else if (codeCategorieMetier.equals(PLOMBERIE_CODE)) {
            return "icons8-plumbing";
        } else if (codeCategorieMetier.equals(ESPACE_VERT_CODE)) {
            return "icons8-garden-shears";
        } else {
            return "icon-Tool";
        }
    }

    public static CategorieMetierDTO getCategorieByCode(Short codeCategorie) {
        switch (codeCategorie) {
        case 0:
            return CategorieLoader.getCategorieElectricite();
        case 1:
            return CategorieLoader.getCategoriePlomberie();
        case 2:
            return CategorieLoader.getCategorieEspaceVert();
        case 3:
            return CategorieLoader.getCategorieDecorationMaconnerie();
        default:
            return new CategorieMetierDTO();
        }
    }
}
