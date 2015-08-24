package fr.batimen.web.client.extend.nouveau.communs;

import java.util.List;

/**
 * Ressources Javascript commune
 *
 * @author Casaucau Cyril
 */
public class JSCommun {

    /**
     * Crée la string JS qui permet d'initialiser un TypeAHEAD avec ses données.
     *
     * @param motsCles Les mots clés que l'on veut autocomplété
     * @param selector Le champs sur lequel on veut activé le typeAhead.
     * @return La string généré.
     */
    public static String buildSourceTypeAhead(List<String> motsCles, String selector) {
        StringBuilder sourceTypeAhead = new StringBuilder("$('");
        sourceTypeAhead.append(selector).append("').typeahead({ source: [");
        boolean firstTime = true;

        for (String motcle : motsCles) {
            if (firstTime) {
                firstTime = false;
                sourceTypeAhead.append("'").append(motcle).append("'");
            } else {
                sourceTypeAhead.append(",'").append(motcle).append("'");
            }
        }
        sourceTypeAhead.append("]});");

        return sourceTypeAhead.toString();
    }

    /**
     * Crée la string JS qui permet d'initialiser un TypeAHEAD avec ses données.
     *
     * @param motsCles Les mots clés que l'on veut autocomplété
     * @param selector Le champs sur lequel on veut activé le typeAhead.
     * @return La string généré.
     */
    public static String buildSourceTypeAheadForMotCles(List<String> motsCles, String selector, String callback) {
        StringBuilder sourceTypeAhead = new StringBuilder("$('");
        sourceTypeAhead.append(selector).append("').typeahead({ source: [");
        boolean firstTime = true;

        for (String motcle : motsCles) {
            if (firstTime) {
                firstTime = false;
                sourceTypeAhead.append("'").append(motcle).append("'");
            } else {
                sourceTypeAhead.append(",'").append(motcle).append("'");
            }
        }
        sourceTypeAhead.append("],");
        sourceTypeAhead.append("updater: function(item){ var motCle = item; ");
        sourceTypeAhead.append(callback);
        sourceTypeAhead.append(" return item; }");
        sourceTypeAhead.append("});");

        return sourceTypeAhead.toString();
    }
}