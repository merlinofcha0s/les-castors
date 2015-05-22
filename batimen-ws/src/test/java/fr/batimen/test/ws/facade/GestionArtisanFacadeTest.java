package fr.batimen.test.ws.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.aggregate.MesAnnoncesDTO;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Assert;
import org.junit.Test;

import fr.batimen.core.constant.CodeRetourService;
import fr.batimen.core.security.HashHelper;
import fr.batimen.dto.CategorieMetierDTO;
import fr.batimen.dto.PermissionDTO;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.dto.enums.Civilite;
import fr.batimen.dto.enums.StatutJuridique;
import fr.batimen.dto.enums.TypeCompte;
import fr.batimen.dto.helper.CategorieLoader;
import fr.batimen.test.ws.AbstractBatimenWsTest;
import fr.batimen.ws.client.service.ArtisanServiceREST;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.dao.EntrepriseDAO;
import fr.batimen.ws.entity.Adresse;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.entity.Entreprise;
import fr.batimen.ws.entity.Permission;

public class GestionArtisanFacadeTest extends AbstractBatimenWsTest {

    @Inject
    private ArtisanDAO artisanDAO;

    @Inject
    private EntrepriseDAO entrepriseDAO;

    @Inject
    private ArtisanServiceREST artisanServiceREST;

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
        nouveauPartenaire.getArtisan().setPassword(HashHelper.hashScrypt("lolmdr06"));
        nouveauPartenaire.getArtisan().setPrenom("David");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setTypeCompte(TypeCompte.ARTISAN);

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

        Integer retourService = artisanServiceREST.creationNouveauPartenaire(nouveauPartenaire);

        Assert.assertNotNull(retourService);
        Assert.assertEquals(CodeRetourService.RETOUR_OK, retourService);

        Artisan artisanEnregistre = artisanDAO.getArtisanByEmail(nouveauPartenaire.getArtisan().getEmail());
        Assert.assertNotNull(artisanEnregistre);
        Assert.assertEquals("plombier@tuyaux.com", artisanEnregistre.getEmail());

        Entreprise entreprise = entrepriseDAO.getEntrepriseByArtisan(artisanEnregistre.getLogin());
        Assert.assertNotNull(entreprise);
        Assert.assertEquals("Entreprise de la plomberie", entreprise.getNomComplet());

        Adresse adresseEntreprise = entreprise.getAdresse();
        Assert.assertNotNull(adresseEntreprise);
        Assert.assertEquals("250 chemin du plombier", adresseEntreprise.getAdresse());

        Set<Permission> permissions = artisanEnregistre.getPermission();

        Assert.assertNotNull(permissions.iterator().next());
        Assert.assertEquals(TypeCompte.ARTISAN, permissions.iterator().next().getTypeCompte());
    }

    @Test
    @UsingDataSet("datasets/in/entreprises_informations.yml")
    public void getEntrepriseInformation(){
        EntrepriseDTO entrepriseDTO = artisanServiceREST.getEntrepriseInformationByArtisanLogin("pebronneArtisanne");

        Assert.assertNotNull(entrepriseDTO);
        Assert.assertEquals("Pebronne enterprise", entrepriseDTO.getNomComplet());
        Assert.assertEquals("106 chemin du pébron", entrepriseDTO.getAdresseEntreprise().getAdresse());
    }
}