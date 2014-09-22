package fr.batimen.dto.aggregate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.AdresseDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.EntrepriseDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class CreationPartenaireDTO extends AbstractDTO {

    private static final long serialVersionUID = 1681975187684307229L;

    @Valid
    private final ClientDTO artisan = new ClientDTO();

    @Valid
    private final EntrepriseDTO entreprise = new EntrepriseDTO();

    @Valid
    private final AdresseDTO adresse = new AdresseDTO();

    private int numeroEtape;

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
     * @return the adresse
     */
    public AdresseDTO getAdresse() {
        return adresse;
    }

    /**
     * @return the numeroEtape
     */
    public int getNumeroEtape() {
        return numeroEtape;
    }

    /**
     * @param numeroEtape
     *            the numeroEtape to set
     */
    public void setNumeroEtape(int numeroEtape) {
        this.numeroEtape = numeroEtape;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(
        /*
         * this.entreprise.getSiret(), this .entreprise.getNomComplet() ,
         */this.artisan.getEmail()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof CreationPartenaireDTO) {
            CreationPartenaireDTO other = (CreationPartenaireDTO) object;
            return /*
                    * Objects.equals(this.entreprise.getSiret(),
                    * other.entreprise.getSiret()) &&
                    * Objects.equals(this.entreprise.getNomComplet(),
                    * other.entreprise.getNomComplet())
                    * 
                    * &&
                    */Objects.equals(this.artisan.getEmail(), other.artisan.getEmail());
        }
        return false;
    }

    public static CreationPartenaireDTO deserializeCreationPartenaireDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, CreationPartenaireDTO.class);
    }

    public static List<CreationPartenaireDTO> deserializeCreationPartenaireDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<CreationPartenaireDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
