package fr.batimen.web.client.panel.nouveau;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import fr.batimen.dto.CreationAnnonceDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.web.client.behaviour.ErrorHighlightBehavior;
import fr.batimen.web.client.behaviour.border.RequiredBorderBehaviour;

public class Etape3InscriptionForm extends Form<CreationAnnonceDTO> {

	private static final long serialVersionUID = 2500892594731116597L;

	private DropDownChoice<Civilite> civiliteField;

	public Etape3InscriptionForm(String id, IModel<CreationAnnonceDTO> model) {
		super(id, model);

		civiliteField = new DropDownChoice<Civilite>("civilite", Arrays.asList(Civilite.values()));
		civiliteField.setMarkupId("civilite");
		civiliteField.setRequired(true);
		civiliteField.add(new RequiredBorderBehaviour());
		civiliteField.add(new ErrorHighlightBehavior());

		this.add(civiliteField);
	}
}
