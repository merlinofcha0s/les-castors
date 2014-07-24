package fr.batimen.dto.helper;

import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.SousCategorieMetierDTO;

public class CategorieLoader {

    private static CategorieMetierDTO electricite;
    private static CategorieMetierDTO plomberie;
    private static CategorieMetierDTO espaceVert;
    private static CategorieMetierDTO decorationMaconnerie;
    private static CategorieMetierDTO grosOeuvre;
    private static CategorieMetierDTO equipement;

    public static CategorieMetierDTO getCategorieElectricite() {
        if (electricite == null) {
            electricite = new CategorieMetierDTO("Electricité");
            SousCategorieMetierDTO sousElectrique = new SousCategorieMetierDTO("Installation électrique");
            electricite.addSousCategorie(sousElectrique);
        }
        return electricite;
    }

    public static CategorieMetierDTO getCategoriePlomberie() {
        if (plomberie == null) {
            plomberie = new CategorieMetierDTO("Plomberie");
            SousCategorieMetierDTO sousPlomberie = new SousCategorieMetierDTO("Tuyauterie");
            plomberie.addSousCategorie(sousPlomberie);
        }
        return plomberie;
    }

    public static CategorieMetierDTO getCategorieEspaceVert() {
        if (espaceVert == null) {
            espaceVert = new CategorieMetierDTO("Espace Vert");
            SousCategorieMetierDTO sousEspaceVert = new SousCategorieMetierDTO("Jardinage");
            espaceVert.addSousCategorie(sousEspaceVert);
        }
        return espaceVert;
    }

    public static CategorieMetierDTO getCategorieDecorationMaconnerie() {
        if (decorationMaconnerie == null) {
            decorationMaconnerie = new CategorieMetierDTO("Décoration / Maçonnerie");
            SousCategorieMetierDTO sousDecorationMaçonnerie = new SousCategorieMetierDTO("Peinture");
            decorationMaconnerie.addSousCategorie(sousDecorationMaçonnerie);
        }
        return decorationMaconnerie;
    }

    public static CategorieMetierDTO getCategorieGrosOeuvre() {
        if (grosOeuvre == null) {
            grosOeuvre = new CategorieMetierDTO("Gros oeuvre");
            SousCategorieMetierDTO sousGrosOeuvre = new SousCategorieMetierDTO("Fondation");
            grosOeuvre.addSousCategorie(sousGrosOeuvre);
        }
        return grosOeuvre;
    }

    public static CategorieMetierDTO getCategorieEquipement() {
        if (equipement == null) {
            equipement = new CategorieMetierDTO("Equipement");
            SousCategorieMetierDTO sousEquipement = new SousCategorieMetierDTO("Alarme");
            equipement.addSousCategorie(sousEquipement);
        }
        return equipement;
    }

}
