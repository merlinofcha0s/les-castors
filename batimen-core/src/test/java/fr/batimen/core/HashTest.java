package fr.batimen.core;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.security.HashHelper;

public class HashTest {

	private static final Logger logger = LoggerFactory.getLogger(HashTest.class);

	@Test
	public void testHash() {

		String password = "lollollol";

		if (logger.isDebugEnabled()) {
			logger.debug("pwd : " + password);
		}

		String hashedPassword = HashHelper.hashString(password);

		if (logger.isDebugEnabled()) {
			logger.debug("hash : " + hashedPassword);
		}

		Assert.assertTrue(HashHelper.check(password, hashedPassword));
	}

}
