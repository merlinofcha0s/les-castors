package fr.batimen.ws.mapper;

import fr.batimen.dto.AnnonceDTO;
import fr.batimen.ws.entity.Annonce;
import org.modelmapper.PropertyMap;

/**
 * Created by Casaucau on 22/04/2015.
 */
public class AnnonceMap extends PropertyMap<AnnonceDTO, Annonce>{

    @Override
    protected void configure() {
        map().setHashID(source.getHashID());
        skip(destination.getId());
    }
}