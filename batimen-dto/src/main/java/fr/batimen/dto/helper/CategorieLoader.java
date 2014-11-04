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
    private static CategorieMetierDTO grosOeuvre;
    private static CategorieMetierDTO equipement;
    private static CategorieMetierDTO toutesCategories;

    public static final Short electriciteCode = 0;
    public static final Short plomberieCode = 1;
    public static final Short espaceVertCode = 2;
    public static final Short decorationMaconnerieCode = 3;
    public static final Short grosOeuvreCode = 4;
    public static final Short equipementCode = 5;

    private CategorieLoader() {

    }

    public static synchronized CategorieMetierDTO getCategorieElectricite() {
        if (electricite == null) {
            electricite = new CategorieMetierDTO("Electricité", electriciteCode);
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
            plomberie = new CategorieMetierDTO("Plomberie", plomberieCode);
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
            espaceVert = new CategorieMetierDTO("Espace Vert", espaceVertCode);
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
            decorationMaconnerie = new CategorieMetierDTO("Décoration / Maçonnerie", decorationMaconnerieCode);
            SousCategorieMetierDTO sousDecorationMaconnerie = new SousCategorieMetierDTO("Peinture");
            decorationMaconnerie.addSousCategorie(sousDecorationMaconnerie);
        }
        return decorationMaconnerie;
    }

    public static synchronized CategorieMetierDTO getCategorieGrosOeuvre() {
        if (grosOeuvre == null) {
            grosOeuvre = new CategorieMetierDTO("Gros oeuvre", grosOeuvreCode);
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

            grosOeuvre.addSousCategorie(porteFenetre);
            grosOeuvre.addSousCategorie(pluieDeLumiere);
            grosOeuvre.addSousCategorie(portailFerronerie);
            grosOeuvre.addSousCategorie(serrurier);
            grosOeuvre.addSousCategorie(storeVolet);
            grosOeuvre.addSousCategorie(vitrerie);
            grosOeuvre.addSousCategorie(peinture);
            grosOeuvre.addSousCategorie(revetementDecoratif);
            grosOeuvre.addSousCategorie(papierPeintTapisserie);
            grosOeuvre.addSousCategorie(lambris);
            grosOeuvre.addSousCategorie(toiture);
            grosOeuvre.addSousCategorie(charpente);
            grosOeuvre.addSousCategorie(etancheite);
            grosOeuvre.addSousCategorie(gouttiere);
            grosOeuvre.addSousCategorie(traitementBoisCharpente);
            grosOeuvre.addSousCategorie(combles);
            grosOeuvre.addSousCategorie(constructionBois);
            grosOeuvre.addSousCategorie(constructionMaison);
            grosOeuvre.addSousCategorie(extensionMaison);
            grosOeuvre.addSousCategorie(fondation);
            grosOeuvre.addSousCategorie(ascenseur);
            grosOeuvre.addSousCategorie(carrelage);
            grosOeuvre.addSousCategorie(surelevation);
            grosOeuvre.addSousCategorie(constructionAnnexes);
        }
        return grosOeuvre;
    }

    public static synchronized CategorieMetierDTO getCategorieEquipement() {
        if (equipement == null) {
            equipement = new CategorieMetierDTO("Equipement", equipementCode);
            SousCategorieMetierDTO sousEquipement = new SousCategorieMetierDTO("Alarme");
            equipement.addSousCategorie(sousEquipement);
        }
        return equipement;
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
        allCategories.add(getCategorieEquipement());
        allCategories.add(getCategorieEspaceVert());
        allCategories.add(getCategorieGrosOeuvre());
        allCategories.add(getCategoriePlomberie());

        return allCategories;
    }

    public static String getIconForCategorie(Short codeCategorieMetier) {
        if (codeCategorieMetier.equals(electriciteCode)) {
            return "icon-Lightning";
        } else if (codeCategorieMetier.equals(plomberieCode)) {
            return "icons8-plumbing";
        } else if (codeCategorieMetier.equals(espaceVertCode)) {
            return "icons8-garden-shears";
        } else if (codeCategorieMetier.equals(decorationMaconnerieCode)) {
            return "icon-Tool";
        } else if (codeCategorieMetier.equals(grosOeuvreCode)) {
            return "icon-House";
        } else {
            return "icon-Satellite1";
        }
    }
}
