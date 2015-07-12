package fr.batimen.web.client.component;

import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;

import java.util.Date;

public class CastorDatePicker extends TextField<Date> {

    private static final long serialVersionUID = -5011785809353752263L;

    private String id;

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forUrl("js/bootstrap-datepicker.js"));
        response.render(CssContentHeaderItem.forUrl("css/datepicker.css"));

        StringBuilder initDatePicker = new StringBuilder("$('#");
        initDatePicker.append(id).append("').datepicker({format: 'dd/mm/yyyy', weekStart: 1});");

        response.render(OnDomReadyHeaderItem.forScript(initDatePicker));
    }

    public CastorDatePicker(String id, String markupId) {
        super(id);
        this.id = markupId;
        setMarkupId(markupId);
    }

}
