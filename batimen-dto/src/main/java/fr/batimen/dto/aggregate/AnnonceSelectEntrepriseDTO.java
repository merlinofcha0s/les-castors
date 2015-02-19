package fr.batimen.dto.aggregate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.batimen.dto.DemandeAnnonceDTO;
import fr.batimen.dto.helper.DeserializeJsonHelper;

public class AnnonceSelectEntrepriseDTO extends DemandeAnnonceDTO {

    private static final long serialVersionUID = -558189502163875839L;

    @NotNull
    private String loginArtisanChoisi;

    @NotNull
    private int ajoutOuSupprimeArtisan;

    public final static int AJOUT_ARTISAN = 1;
    public final static int SUPPRESSION_ARTISAN = 2;

    /**
     * @return the siret
     */
    public String getLoginArtisanChoisi() {
        return loginArtisanChoisi;
    }

    /**
     * @param loginArtisanChoisi
     *            the siret to set
     */
    public void setLoginArtisanChoisi(String loginArtisanChoisi) {
        this.loginArtisanChoisi = loginArtisanChoisi;
    }

    /**
     * @return the ajoutOuSupprime
     */
    public int getAjoutOuSupprimeArtisan() {
        return ajoutOuSupprimeArtisan;
    }

    /**
     * @param ajoutOuSupprimeArtisan
     *            the ajoutOuSupprime to set
     */
    public void setAjoutOuSupprimeArtisan(int ajoutOuSupprimeArtisan) {
        this.ajoutOuSupprimeArtisan = ajoutOuSupprimeArtisan;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(loginArtisanChoisi));
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

        if (object instanceof AnnonceSelectEntrepriseDTO) {
            AnnonceSelectEntrepriseDTO other = (AnnonceSelectEntrepriseDTO) object;
            return Objects.equals(this.getLoginArtisanChoisi(), other.getLoginArtisanChoisi());
        }
        return false;
    }

    public static AnnonceSelectEntrepriseDTO deserializeDemAnnonceSelectEntrepriseDTO(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        return gson.fromJson(json, AnnonceSelectEntrepriseDTO.class);
    }

    public static List<AnnonceSelectEntrepriseDTO> deserializeDemAnnonceSelectEntrepriseDTOList(String json) {
        Gson gson = DeserializeJsonHelper.createGsonObject();
        Type collectionType = new TypeToken<List<AnnonceSelectEntrepriseDTO>>() {
        }.getType();
        return gson.fromJson(json, collectionType);
    }

}
