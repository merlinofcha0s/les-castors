package fr.batimen.test.ws.facade;

import javax.inject.Inject;

import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;

import fr.batimen.dto.AvisDTO;
import fr.batimen.dto.aggregate.MonProfilDTO;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ClientServiceREST;

public class GestionClientFacadeTest extends AbstractBatimenWsTest {

    @Inject
    private ClientServiceREST clientsServiceREST;

    /**
     * Cas de test : Le client se rend sur la page "mon profil" <br/>
     * Ce test verifie que les données remontent de maniere correctes
     * 
     */
    @Test
    @UsingDataSet("datasets/in/mon_profil.yml")
    public void testGetInfoForMonProfil() {
        MonProfilDTO monProfilDTO = clientsServiceREST.getMesInfosForMonProfil("pebronne");

        Assert.assertEquals(Long.valueOf("2"), monProfilDTO.getNbAnnonce());

        Boolean isDataCorrectForNotation1 = Boolean.FALSE;
        Boolean isDataCorrectForNotation2 = Boolean.FALSE;

        Assert.assertEquals(2, monProfilDTO.getNotations().size());

        for (AvisDTO notation : monProfilDTO.getNotations()) {
            if (notation.getScore().equals(Double.valueOf("3")) && notation.getCommentaire().equals("Bon Travail")
                    && notation.getNomEntreprise().equals("Pebronne enterprise")) {
                isDataCorrectForNotation1 = Boolean.TRUE;
            }
            if (notation.getScore().equals(Double.valueOf("5")) && notation.getCommentaire().equals("Excellent")
                    && notation.getNomEntreprise().equals("Pebronne enterprise")) {
                isDataCorrectForNotation2 = Boolean.TRUE;
            }
        }

        Assert.assertTrue(isDataCorrectForNotation1);
        Assert.assertTrue(isDataCorrectForNotation2);
    }
}
