package fr.castor.dto.constant;

import fr.castor.dto.CategorieMetierDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire permettant de gérer les catégories
 *
 * @author Casaucau Cyril
 */
public class Categorie {

    public static final short ELECTRICITE_CODE = 0;
    public static final short PLOMBERIE_CODE = 1;
    public static final short ESPACE_VERT_CODE = 2;
    public static final short DECORATION_MACONNERIE_CODE = 3;
    public static final short MENUISERIE_CODE = 4;

    public static final String ELECTRICITE_NAME = "Electricité";
    public static final String PLOMBERIE_NAME = "Plomberie";
    public static final String ESPACE_VERT_NAME = "Espace vert";
    public static final String DECORATION_MACONNERIE_NAME = "Maçonnerie";
    public static final String MENUISERIE_NAME = "Menuiserie";

    private Categorie() {
        super();
    }


    public static String getNameByCode(int code) {
        switch (code) {
            case 0:
                return ELECTRICITE_NAME;
            case 1:
                return PLOMBERIE_NAME;
            case 2:
                return ESPACE_VERT_NAME;
            case 3:
                return DECORATION_MACONNERIE_NAME;
            case 4:
                return MENUISERIE_NAME;
            default:
                return "";
        }
    }

    public static String getIcon(Short codeCategorieMetier) {
        if (codeCategorieMetier.equals(ELECTRICITE_CODE)) {
            return "icon-Lightning";
        } else if (codeCategorieMetier.equals(PLOMBERIE_CODE)) {
            return "icons8-plumbing";
        } else if (codeCategorieMetier.equals(ESPACE_VERT_CODE)) {
            return "icons8-garden-shears";
        } else if (codeCategorieMetier.equals(MENUISERIE_CODE)) {
            return "icon-Forrst";
        } else {
            return "icon-Tool";
        }
    }

    public static CategorieMetierDTO getElectricite() {
        return new CategorieMetierDTO(ELECTRICITE_CODE);
    }

    public static CategorieMetierDTO getPlomberie() {
        return new CategorieMetierDTO(PLOMBERIE_CODE);
    }

    public static CategorieMetierDTO getMaconnerie() {
        return new CategorieMetierDTO(DECORATION_MACONNERIE_CODE);
    }

    public static CategorieMetierDTO getEspaceVert() {
        return new CategorieMetierDTO(ESPACE_VERT_CODE);
    }

    public static CategorieMetierDTO getMenuiserie() {
        return new CategorieMetierDTO(MENUISERIE_CODE);
    }

    public static synchronized List<CategorieMetierDTO> getAllCategories() {
        List<CategorieMetierDTO> allCategories = new ArrayList<CategorieMetierDTO>();
        allCategories.add(getElectricite());
        allCategories.add(getPlomberie());
        allCategories.add(getEspaceVert());
        allCategories.add(getMaconnerie());
        return allCategories;
    }

    public static CategorieMetierDTO getCategorieByCode(Short codeCategorie) {
        switch (codeCategorie) {
            case 0:
                return getElectricite();
            case 1:
                return getPlomberie();
            case 2:
                return getEspaceVert();
            case 3:
                return getMaconnerie();
            case 4:
                return getMenuiserie();
            default:
                throw new UnsupportedOperationException();
        }
    }
}