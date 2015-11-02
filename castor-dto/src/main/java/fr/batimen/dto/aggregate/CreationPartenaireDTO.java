package fr.batimen.dto.aggregate;

import fr.batimen.dto.AbstractDTO;
import fr.batimen.dto.AdresseDTO;
import fr.batimen.dto.ClientDTO;
import fr.batimen.dto.EntrepriseDTO;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreationPartenaireDTO extends AbstractDTO {

    private static final long serialVersionUID = 1681975187684307229L;

    @Valid
    private final ClientDTO artisan = new ClientDTO();

    @Valid
    private final EntrepriseDTO entreprise = new EntrepriseDTO();

    @Valid
    private final AdresseDTO adresse = new AdresseDTO();
    private final List<String> villesPossbles = new ArrayList<>();
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
     * @param numeroEtape the numeroEtape to set
     */
    public void setNumeroEtape(int numeroEtape) {
        this.numeroEtape = numeroEtape;
    }

    public List<String> getVillesPossbles() {
        return villesPossbles;
    }

    /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#hashCode()
         */
    @Override
    public int hashCode() {
        return Objects.hashCode(Objects.hash(this.entreprise.getSiret(), this.entreprise.getNomComplet(),
                this.artisan.getEmail()));
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
            return Objects.equals(this.entreprise.getSiret(), other.entreprise.getSiret())
                    && Objects.equals(this.entreprise.getNomComplet(), other.entreprise.getNomComplet())

                    && Objects.equals(this.artisan.getEmail(), other.artisan.getEmail());
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreationPartenaireDTO{");
        sb.append("artisan=").append(artisan);
        sb.append(", entreprise=").append(entreprise);
        sb.append(", adresse=").append(adresse);
        sb.append(", numeroEtape=").append(numeroEtape);
        sb.append(", villesPossbles=").append(villesPossbles);
        sb.append('}');
        return sb.toString();
    }
}