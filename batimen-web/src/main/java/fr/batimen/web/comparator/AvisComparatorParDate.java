package fr.batimen.web.comparator;

import fr.batimen.dto.AvisDTO;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Casaucau on 15/06/2015.
 */
public class AvisComparatorParDate implements Comparator<AvisDTO>, Serializable {
    @Override
    public int compare(AvisDTO o1, AvisDTO o2) {
        return o2.getDateAvis().compareTo(o1.getDateAvis());
    }
}
