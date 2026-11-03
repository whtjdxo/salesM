package com.web.manage.common.service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TotpSecretEncryptionService {

	private static final int IV_LENGTH = 12;
	private static final int TAG_LENGTH_BITS = 128;
	private static final SecureRandom RANDOM = new SecureRandom();

	private final SecretKeySpec key;

	public TotpSecretEncryptionService(@Value("${app.totp.encryption-key:}") String encodedKey) {
		if (encodedKey == null || encodedKey.isBlank()) {
			throw new IllegalStateException("TOTP_ENCRYPTION_KEY 환경 변수를 설정해야 합니다.");
		}
		byte[] keyBytes;
		try {
			keyBytes = Base64.getDecoder().decode(encodedKey);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("TOTP_ENCRYPTION_KEY는 Base64 형식이어야 합니다.", e);
		}
		if (keyBytes.length != 32) {
			throw new IllegalStateException("TOTP_ENCRYPTION_KEY는 32바이트 키를 Base64로 인코딩해야 합니다.");
		}
		this.key = new SecretKeySpec(keyBytes, "AES");
	}

	public String encrypt(String value) {
		try {
			byte[] iv = new byte[IV_LENGTH];
			RANDOM.nextBytes(iv);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
			byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(ByteBuffer.allocate(iv.length + encrypted.length)
					.put(iv).put(encrypted).array());
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("TOTP 비밀키를 암호화하지 못했습니다.", e);
		}
	}

	public String decrypt(String value) {
		try {
			byte[] payload = Base64.getDecoder().decode(value);
			if (payload.length <= IV_LENGTH) {
				throw new IllegalArgumentException("암호화된 TOTP 비밀키의 길이가 올바르지 않습니다.");
			}
			ByteBuffer buffer = ByteBuffer.wrap(payload);
			byte[] iv = new byte[IV_LENGTH];
			buffer.get(iv);
			byte[] encrypted = new byte[buffer.remaining()];
			buffer.get(encrypted);

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
			return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
		} catch (GeneralSecurityException | IllegalArgumentException e) {
			throw new IllegalStateException("TOTP 비밀키를 복호화하지 못했습니다.", e);
		}
	}
}
