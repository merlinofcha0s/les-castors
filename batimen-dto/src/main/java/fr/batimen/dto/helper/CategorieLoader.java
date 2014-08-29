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

    private CategorieLoader() {

    }

    public static synchronized CategorieMetierDTO getCategorieElectricite() {
        if (electricite == null) {
            electricite = new CategorieMetierDTO("Electricité");
            SousCategorieMetierDTO sousElectrique = new SousCategorieMetierDTO("Installation électrique");
            electricite.addSousCategorie(sousElectrique);
        }
        return electricite;
    }

    public static synchronized CategorieMetierDTO getCategoriePlomberie() {
        if (plomberie == null) {
            plomberie = new CategorieMetierDTO("Plomberie");
            SousCategorieMetierDTO sousPlomberie = new SousCategorieMetierDTO("Tuyauterie");
            plomberie.addSousCategorie(sousPlomberie);
        }
        return plomberie;
    }

    public static synchronized CategorieMetierDTO getCategorieEspaceVert() {
        if (espaceVert == null) {
            espaceVert = new CategorieMetierDTO("Espace Vert");
            SousCategorieMetierDTO sousEspaceVert = new SousCategorieMetierDTO("Jardinage");
            espaceVert.addSousCategorie(sousEspaceVert);
        }
        return espaceVert;
    }

    public static synchronized CategorieMetierDTO getCategorieDecorationMaconnerie() {
        if (decorationMaconnerie == null) {
            decorationMaconnerie = new CategorieMetierDTO("Décoration / Maçonnerie");
            SousCategorieMetierDTO sousDecorationMaconnerie = new SousCategorieMetierDTO("Peinture");
            decorationMaconnerie.addSousCategorie(sousDecorationMaconnerie);
        }
        return decorationMaconnerie;
    }

    public static synchronized CategorieMetierDTO getCategorieGrosOeuvre() {
        if (grosOeuvre == null) {
            grosOeuvre = new CategorieMetierDTO("Gros oeuvre");
            SousCategorieMetierDTO sousGrosOeuvre = new SousCategorieMetierDTO("Fondation");
            grosOeuvre.addSousCategorie(sousGrosOeuvre);
        }
        return grosOeuvre;
    }

    public static synchronized CategorieMetierDTO getCategorieEquipement() {
        if (equipement == null) {
            equipement = new CategorieMetierDTO("Equipement");
            SousCategorieMetierDTO sousEquipement = new SousCategorieMetierDTO("Alarme");
            equipement.addSousCategorie(sousEquipement);
        }
        return equipement;
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

}
