package fr.batimen.test.ws.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ArtisanService;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.entity.Adresse;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Entreprise;
import fr.batimen.ws.entity.Permission;

public class GestionArtisanFacadeTest extends AbstractBatimenWsTest {

    @Inject
    private ArtisanDAO artisanDAO;

    @Test
    public void nouveauPartenaireTestNominal() {

        CreationPartenaireDTO nouveauPartenaire = new CreationPartenaireDTO();
        // Adresse
        nouveauPartenaire.getAdresse().setAdresse("250 chemin du plombier");
        nouveauPartenaire.getAdresse().setComplementAdresse("res de la fuite");
        nouveauPartenaire.getAdresse().setCodePostal("06800");
        nouveauPartenaire.getAdresse().setVille("Tuyaux ville");
        nouveauPartenaire.getAdresse().setDepartement(50);

        // Artisan
        nouveauPartenaire.getArtisan().setCivilite(Civilite.MONSIEUR);
        nouveauPartenaire.getArtisan().setEmail("plombier@tuyaux.com");
        nouveauPartenaire.getArtisan().setLogin("plombier06");
        nouveauPartenaire.getArtisan().setNom("Dupont");
        nouveauPartenaire.getArtisan().setNumeroTel("0645789655");
        nouveauPartenaire.getArtisan().setPassword(HashHelper.hashString("lolmdr06"));
        nouveauPartenaire.getArtisan().setPrenom("David");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setTypeCompte(TypeCompte.ARTISAN_DEFAULT);

        nouveauPartenaire.getArtisan().getPermissions().add(permissionDTO);

        // Entreprise
        nouveauPartenaire.getEntreprise().setDateCreation(new Date());

        List<CategorieMetierDTO> categoriesMetier = new ArrayList<CategorieMetierDTO>();
        categoriesMetier.add(CategorieLoader.getCategoriePlomberie());

        nouveauPartenaire.getEntreprise().getCategoriesMetier().addAll(categoriesMetier);
        nouveauPartenaire.getEntreprise().setNbEmployees(2);
        nouveauPartenaire.getEntreprise().setNomComplet("Entreprise de la plomberie");
        nouveauPartenaire.getEntreprise().setSiret("43394298400017");

        nouveauPartenaire.getEntreprise().setStatutJuridique(StatutJuridique.SARL);

        Integer retourService = ArtisanService.creationNouveauPartenaire(nouveauPartenaire);

        Assert.assertNotNull(retourService);
        Assert.assertEquals(Constant.CODE_SERVICE_RETOUR_OK, retourService);

        Artisan artisanEnregistre = artisanDAO.getArtisanByEmail(nouveauPartenaire.getArtisan().getEmail());
        Assert.assertNotNull(artisanEnregistre);
        Assert.assertEquals("plombier@tuyaux.com", artisanEnregistre.getEmail());

        Entreprise entreprise = artisanEnregistre.getEntreprise();
        Assert.assertNotNull(entreprise);
        Assert.assertEquals("Entreprise de la plomberie", entreprise.getNomComplet());

        Adresse adresseEntreprise = entreprise.getAdresse();
        Assert.assertNotNull(adresseEntreprise);
        Assert.assertEquals("250 chemin du plombier", adresseEntreprise.getAdresse());

        List<Permission> permissions = artisanEnregistre.getPermission();
        Assert.assertNotNull(permissions.get(0));
        Assert.assertEquals(TypeCompte.ARTISAN_DEFAULT, permissions.get(0).getTypeCompte());
    }
}
