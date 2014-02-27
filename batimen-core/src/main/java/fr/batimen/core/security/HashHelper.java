package fr.batimen.core.security;

import com.lambdaworks.crypto.SCryptUtil;

public class HashHelper {

	private HashHelper() {

	}

	private static int CPU_COST = 32;
	private static int MEMORY_COST = 64;
	private static int PARALLELIZATION_COST = 64;

	// TODO Pas sur que ces valeurs soient correct en 2013, a checker.

	/**
	 * Génère un hash de manière sécurisée
	 * 
	 * @param input
	 *            La chaine de caractere que l'on veut hasher
	 * @return Le hash
	 */
	public static String hashString(String input) {
		return SCryptUtil.scrypt(input, CPU_COST, MEMORY_COST, PARALLELIZATION_COST);
	}

	/**
	 * Regarde si le hash corresponds au password.
	 * 
	 * @param password
	 *            le password de l'utilisateur
	 * @param hash
	 *            le hash qui se trouve dans la BDD
	 * @return True si le password correspond au hash
	 */
	public static boolean check(String password, String hash) {
		return SCryptUtil.check(password, hash);
	}

}