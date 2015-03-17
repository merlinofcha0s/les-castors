package fr.batimen.ws.quartz.jobs;

import java.util.Properties;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.ws.enums.PropertiesFileWS;
import fr.batimen.ws.service.AnnonceService;

public class AnnonceGarbageCollector implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceGarbageCollector.class);

    @Inject
    private AnnonceService annonceService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (LOGGER.isDebugEnabled()) {
            Properties castorProperties = PropertiesFileWS.CASTOR.getProperties();
            int nbJourAvantPeremption = Integer.valueOf(castorProperties.getProperty("prop.temps.peremption.annonce"));
            Integer nbMaxArtisanParAnnonce = Integer.valueOf(castorProperties
                    .getProperty("prop.nb.max.artisan.annonce"));
            StringBuilder debugString = new StringBuilder(
                    "Demarrage du batch de desactivation des annonces avec plus de ");
            debugString.append(nbJourAvantPeremption).append(" jours et un nombre maximum d'artisans de : ")
                    .append(nbMaxArtisanParAnnonce);
            LOGGER.debug(debugString.toString());
        }

        annonceService.desactivateAnnoncePerime();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Fin du traitement batch de désactivation des annonces périmées");
        }
    }
}
