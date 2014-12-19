--- Sur le schéma ---
GRANT USAGE ON SCHEMA public TO batimen_usr;

--- Sur les sequences ---
GRANT USAGE,SELECT,UPDATE ON 
public.annonce_id_seq,
public.adresse_id_seq,
public.artisan_id_seq,
public.categoriemetier_id_seq,
public.client_id_seq,
public.entreprise_id_seq,
public.notation_id_seq,
public.notification_id_seq,
public.paiement_id_seq,
public.permission_id_seq
TO batimen_usr;

--- Sur les tables ---
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE
public.Client,
public.Adresse,
public.Annonce,
public.Artisan,
public.Entreprise,
public.Notation,
public.Paiement,
public.annonce_artisan,
public.categoriemetier,
public.notification,
public.permission
TO batimen_usr;