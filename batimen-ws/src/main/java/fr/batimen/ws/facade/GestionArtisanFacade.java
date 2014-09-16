package fr.batimen.ws.facade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.core.constant.WsPath;
import fr.batimen.dto.aggregate.CreationPartenaireDTO;
import fr.batimen.ws.dao.ArtisanDAO;
import fr.batimen.ws.entity.Artisan;
import fr.batimen.ws.helper.JsonHelper;
import fr.batimen.ws.interceptor.BatimenInterceptor;

/**
 * Facade REST de gestion des artisans
 * 
 * @author Casaucau Cyril
 * 
 */
@Stateless(name = "GestionArtisanFacade")
@LocalBean
@Path(WsPath.GESTION_PARTENAIRE_SERVICE_PATH)
@RolesAllowed(Constant.USERS_ROLE)
@Produces(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Consumes(JsonHelper.JSON_MEDIA_TYPE_AND_UTF_8_CHARSET)
@Interceptors(value = { BatimenInterceptor.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionArtisanFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionArtisanFacade.class);

    @Inject
    private ArtisanDAO artisanDAO;

    @POST
    @Path(WsPath.GESTION_PARTENAIRE_SERVICE_CREATION_PARTENAIRE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer creationArtisan(CreationPartenaireDTO nouveauPartenaire) {

        Artisan artisanExiste = artisanDAO.getArtisanByEmail(nouveauPartenaire.getArtisan().getEmail());

        // On check que l'artisan n'existe pas déjà
        if (!artisanExiste.getEmail().isEmpty()) {
            return Constant.CODE_SERVICE_RETOUR_KO;
        }

        Artisan nouveauArtisan = new Artisan();

        ModelMapper mapper = new ModelMapper();
        mapper.map(nouveauPartenaire.getArtisan(), nouveauArtisan);

        return 0;
    }
}
