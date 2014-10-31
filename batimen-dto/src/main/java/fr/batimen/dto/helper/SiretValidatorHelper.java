package fr.batimen.dto.helper;

public class SiretValidatorHelper {

    private SiretValidatorHelper() {

    }

    public static boolean isSiretValide(String siretToValidate) {

        if (!siretToValidate.isEmpty()) {
            int total = 0;
            int digit = 0;

            for (int i = 0; i < siretToValidate.length(); i++) {
                /**
                 * Recherche les positions impaires : 1er, 3è, 5è, etc... que
                 * l'on multiplie par 2
                 * 
                 * petite différence avec la définition ci-dessus car ici on
                 * travail de gauche à droite
                 */

                if ((i % 2) == 0) {
                    digit = Integer.parseInt(String.valueOf(siretToValidate.charAt(i))) * 2;
                    /**
                     * si le résultat est >9 alors il est composé de deux digits
                     * tous les digits devant
                     * 
                     * s'additionner et ne pouvant être >19 le calcule devient :
                     * 1 + (digit -10) ou : digit - 9
                     */

                    if (digit > 9) {
                        digit -= 9;
                    }
                } else {
                    digit = Integer.parseInt(String.valueOf(siretToValidate.charAt(i)));
                }
                total += digit;
            }
            /** Si la somme est un multiple de 10 alors le SIRET est valide */
            return (total % 10) == 0;
        } else {
            return false;
        }
    }
}
