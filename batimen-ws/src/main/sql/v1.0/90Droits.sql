--- Sur le schéma ---
GRANT USAGE ON SCHEMA public TO batimen_usr;

--- Sur les sequences ---
GRANT UPDATE ON 
public.hibernate_sequence
TO batimen_usr;

--- Sur les tables ---
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE
public.users
TO batimen_usr;