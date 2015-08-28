package fr.batimen.dto.constant;

/**
 * Created by Casaucau on 25/08/2015.
 */
public class Categorie {

    public static final Short ELECTRICITE_CODE = 0;
    public static final Short PLOMBERIE_CODE = 1;
    public static final Short ESPACE_VERT_CODE = 2;
    public static final Short DECORATION_MACONNERIE_CODE = 3;
    public static final Short MENUISERIE_CODE = 4;

    public static final String ELECTRICITE_NAME = "Electricité";
    public static final String PLOMBERIE_NAME = "Plomberie";
    public static final String ESPACE_VERT_NAME = "Espace vert";
    public static final String DECORATION_MACONNERIE_NAME = "Maçonnerie";
    public static final String MENUISERIE_NAME = "Menuiserie";


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
}
