package fr.castor.web.client.component;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Created by Casaucau on 08/06/2015.
 */
public class CastorAjaxPagingNavigator extends AjaxPagingNavigator {

    public CastorAjaxPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }
}
