package com.web.manage.common.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class TotpServiceTest {

	private final TotpService totpService = new TotpService();

	@Test
	void generatesRfc6238SixDigitCodes() {
		String secret = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ";

		assertEquals("287082", totpService.codeAtCounter(secret, 59 / 30));
		assertEquals("081804", totpService.codeAtCounter(secret, 1_111_111_109L / 30));
		assertEquals("050471", totpService.codeAtCounter(secret, 1_111_111_111L / 30));
		assertEquals("005924", totpService.codeAtCounter(secret, 1_234_567_890L / 30));
		assertEquals("279037", totpService.codeAtCounter(secret, 2_000_000_000L / 30));
		assertEquals("353130", totpService.codeAtCounter(secret, 20_000_000_000L / 30));
	}

	@Test
	void acceptsOnlySixDigitCodesWithinOneTimeStepOfCurrentTime() {
		String secret = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ";

		assertEquals(1L, totpService.findMatchingCounter(secret, "287082", 59));
		assertNull(totpService.findMatchingCounter(secret, "28708", 59));
		assertNull(totpService.findMatchingCounter(secret, "000000", 59));
	}

	@Test
	void encryptsAndDecryptsStoredAuthenticatorSecrets() {
		TotpSecretEncryptionService encryptionService =
				new TotpSecretEncryptionService("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");
		String secret = totpService.generateSecret();

		String encrypted = encryptionService.encrypt(secret);

		assertNotEquals(secret, encrypted);
		assertEquals(secret, encryptionService.decrypt(encrypted));
	}
}
