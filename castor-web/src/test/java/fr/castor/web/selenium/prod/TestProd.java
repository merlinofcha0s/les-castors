package fr.castor.web.selenium.prod;

import fr.castor.web.selenium.common.AbstractITTest;
import org.junit.Test;

/**
 * Test éxecuté juste après une mise en prod.
 * On s'assure que la connexion entre la webapp et le webservice fonctionne correctement.
 */
public class TestProd extends AbstractITTest{
    @Override
    public void prepareDB() throws Exception {

    }

    @Test
    public void testConnexionUserProd(){
        driver.get("lescastors.fr");
        connexionApplication("roberta", AbstractITTest.BON_MOT_DE_PASSE, Boolean.TRUE);
    }
}
