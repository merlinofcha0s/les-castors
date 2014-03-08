package fr.batimen.web.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.constant.Constant;
import fr.batimen.dto.CreationAnnonceDTO;

public class AnnonceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnonceService.class);

	private AnnonceService() {

	}

	public static Integer creationAnnonce(CreationAnnonceDTO nouvelleAnnonce) {

		return Constant.CODE_SERVICE_RETOUR_OK;

	}

}
