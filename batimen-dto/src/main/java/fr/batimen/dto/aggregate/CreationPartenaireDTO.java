package fr.batimen.dto.aggregate;

import javax.validation.Valid;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.AdresseDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.EntrepriseDTO;

public class CreationPartenaireDTO extends AbstractDTO {

    private static final long serialVersionUID = 1681975187684307229L;

    @Valid
    private ClientDTO artisan;
    @Valid
    private EntrepriseDTO entreprise;
    @Valid
    private AdresseDTO adresse;

    /**
     * @return the artisan
     */
    public ClientDTO getArtisan() {
        return artisan;
    }

    /**
     * @return the entreprise
     */
    public EntrepriseDTO getEntreprise() {
        return entreprise;
    }

    /**
     * @param artisan
     *            the artisan to set
     */
    public void setArtisan(ClientDTO artisan) {
        this.artisan = artisan;
    }

    /**
     * @param entreprise
     *            the entreprise to set
     */
    public void setEntreprise(EntrepriseDTO entreprise) {
        this.entreprise = entreprise;
    }

}
