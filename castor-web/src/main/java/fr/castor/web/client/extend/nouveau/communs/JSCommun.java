package fr.castor.web.client.extend.nouveau.communs;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

/**
 * Ressources Javascript commune
 *
 * @author Casaucau Cyril
 */
public class JSCommun {

    private JSCommun() {

    }

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

        remplisMotsClesString(motsCles, sourceTypeAhead);
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

        remplisMotsClesString(motsCles, sourceTypeAhead);

        sourceTypeAhead.append("],");
        sourceTypeAhead.append("updater: function(item){ var motCle = item; ");
        sourceTypeAhead.append(callback);
        sourceTypeAhead.append(" return item; }");
        sourceTypeAhead.append("});");

        return sourceTypeAhead.toString();
    }

    private static void remplisMotsClesString(List<String> motsCles, StringBuilder sourceTypeAhead) {
        boolean firstTime = true;
        for (String motcle : motsCles) {
            if (firstTime) {
                firstTime = false;
                sourceTypeAhead.append("'").append(motcle).append("'");
            } else {
                sourceTypeAhead.append(",'").append(motcle).append("'");
            }
        }
    }

    public static void scrollToTop(AjaxRequestTarget target) {
        target.appendJavaScript("$(window).scrollTop(0);");
    }
}