package fr.castor.ws.mapper;

import fr.castor.dto.AnnonceDTO;
import fr.castor.ws.entity.Annonce;
import org.modelmapper.PropertyMap;

/**
 * Mapper qui permet de gérer avec précision le transformation de AnnonceDTO à Annonce
 * Created by Casaucau on 22/04/2015.
 *
 * @author Casaucau Cyril
 */
public class AnnonceMap extends PropertyMap<AnnonceDTO, Annonce>{

    @Override
    protected void configure() {
        map().setHashID(source.getHashID());
        skip(destination.getId());
        skip(destination.getMotcles());
    }
}