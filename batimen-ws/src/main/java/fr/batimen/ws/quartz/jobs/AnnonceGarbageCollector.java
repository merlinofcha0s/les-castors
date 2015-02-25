package fr.batimen.ws.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnonceGarbageCollector implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceGarbageCollector.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        LOGGER.debug("Hello form gc annonce !!!");
    }

}
