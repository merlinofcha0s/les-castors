package fr.batimen.web.client.component;

import org.odlabs.wiquery.ui.datepicker.ArrayOfDayNames;
import org.odlabs.wiquery.ui.datepicker.ArrayOfMonthNames;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

public class CastorDatePicker<T> extends DatePicker<T> {

    private static final long serialVersionUID = -5011785809353752263L;

    public CastorDatePicker(String id) {
        super(id);
        ArrayOfDayNames days = new ArrayOfDayNames("Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam");
        setDayNamesMin(days);
        ArrayOfMonthNames months = new ArrayOfMonthNames("Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre");
        setMonthNames(months);
    }

}
