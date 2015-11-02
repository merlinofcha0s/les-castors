package fr.batimen.test.ws.jobs;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.dto.enums.EtatAnnonce;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.dao.AnnonceDAO;
import fr.batimen.ws.entity.Annonce;
import fr.batimen.ws.service.AnnonceService;

public class QuartzJobsTest extends AbstractBatimenWsTest {

    @Inject
    private AnnonceService annonceService;

    @Inject
    private AnnonceDAO annonceDAO;

    @Test
    @UsingDataSet("datasets/in/jobs_annonce_peremption.yml")
    public void testDesactivationAnnoncePerimeeNominal() {
        Integer codeRetour = annonceService.desactivateAnnoncePerime();

        Assert.assertEquals(CodeRetourService.RETOUR_OK, codeRetour);

        List<Annonce> annonces = annonceDAO.getAnnoncesByLogin("pebronne");

        boolean verifAnnonce1 = false;
        boolean verifAnnonce2 = false;

        for (Annonce annonce : annonces) {
            if (annonce.getEtatAnnonce().equals(EtatAnnonce.DESACTIVE)) {
                if (annonce
                        .getHashID()
                        .equals("88263227a51224d8755b21e729e1d10c0569b10f98749264ddf66fb65b53519fb863cf44092880247f2841d6335473a5d99402ae0a4d9d94f665d97132dcbc21")) {
                    verifAnnonce1 = true;
                } else if (annonce.getHashID().equals("lolmdrp")) {
                    verifAnnonce2 = true;
                }
            }
        }
        Assert.assertTrue(verifAnnonce1);
        Assert.assertTrue(verifAnnonce2);
    }
}
