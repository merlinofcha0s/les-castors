package fr.batimen.web.client.component;

import org.odlabs.wiquery.ui.position.PositionAlignmentOptions;
import org.odlabs.wiquery.ui.position.PositionOptions;
import org.odlabs.wiquery.ui.position.PositionRelation;
import org.odlabs.wiquery.ui.tooltip.TooltipBehavior;

/**
 * 
 * Décore l'attribut title de tous les composants de la page.
 * 
 * 
 * @author Casaucau Cyril
 * 
 * @see http://api.jqueryui.com/tooltip/
 * 
 */
public class BatimenToolTip {

	private static TooltipBehavior tooltipBehavior;

	private BatimenToolTip() {

	}

	public static TooltipBehavior getTooltipBehaviour() {
		if (tooltipBehavior == null) {
			tooltipBehavior = new TooltipBehavior();

			// Defines which position on the element being positioned to align
			// with the target element: "horizontal vertical" alignment. A
			// single value such as "right" will be normalized to
			// "right center", "top" will be normalized to "center top"
			// (following CSS convention). Acceptable horizontal values: "left",
			// "center", "right". Acceptable vertical values: "top", "center",
			// "bottom". Example: "left top" or "center center". Each dimension
			// can also contain offsets, in pixels or percent, e.g.,
			// "right+10 top-25%". Percentage offsets are relative to the
			// element being positioned.
			PositionAlignmentOptions my = new PositionAlignmentOptions(PositionRelation.CENTER, 0,
					PositionRelation.BOTTOM, 38);

			// Defines which position on the target element to align the
			// positioned element against: "horizontal vertical" alignment
			PositionAlignmentOptions at = new PositionAlignmentOptions(PositionRelation.CENTER, 10,
					PositionRelation.BOTTOM, 0);

			PositionOptions positionOptions = new PositionOptions();
			positionOptions.setMy(my);
			positionOptions.setAt(at);

			tooltipBehavior.setPositionOptions(positionOptions);
		}

		return tooltipBehavior;
	}
}
