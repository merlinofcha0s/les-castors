--- Sur le schéma ---
GRANT USAGE ON SCHEMA public TO batimen_usr;

--- Sur les sequences ---
GRANT UPDATE ON 
public.hibernate_sequence
TO batimen_usr;

--- Sur les tables ---
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE
public.Client,
public.Adresse,
public.Annonce,
public.Artisan,
public.Entreprise,
public.Notation,
public.Paiement
TO batimen_usr;