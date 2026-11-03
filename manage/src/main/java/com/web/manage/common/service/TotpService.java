package com.web.manage.common.service;

import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class TotpService {

	private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
	private static final Pattern SIX_DIGIT_CODE = Pattern.compile("\\d{6}");
	private static final int TIME_STEP_SECONDS = 30;
	private static final int CODE_DIGITS = 6;
	private static final SecureRandom RANDOM = new SecureRandom();

	public String generateSecret() {
		byte[] secret = new byte[20];
		RANDOM.nextBytes(secret);
		return encodeBase32(secret);
	}

	public Long findMatchingCounter(String secret, String code, long epochSecond) {
		if (code == null || !SIX_DIGIT_CODE.matcher(code).matches()) {
			return null;
		}

		long currentCounter = epochSecond / TIME_STEP_SECONDS;
		for (int offset : new int[] { 0, -1, 1 }) {
			long counter = currentCounter + offset;
			if (counter >= 0 && MessageDigest.isEqual(
					code.getBytes(StandardCharsets.US_ASCII),
					codeAtCounter(secret, counter).getBytes(StandardCharsets.US_ASCII))) {
				return counter;
			}
		}
		return null;
	}

	public String createProvisioningUri(String userId, String secret) {
		String issuer = "매출정산 관리시스템";
		String label = URLEncoder.encode(issuer + ":" + userId, StandardCharsets.UTF_8);
		String encodedIssuer = URLEncoder.encode(issuer, StandardCharsets.UTF_8);
		return "otpauth://totp/" + label
				+ "?secret=" + secret
				+ "&issuer=" + encodedIssuer
				+ "&algorithm=SHA1&digits=" + CODE_DIGITS
				+ "&period=" + TIME_STEP_SECONDS;
	}

	String codeAtCounter(String secret, long counter) {
		try {
			byte[] key = decodeBase32(secret);
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(key, "HmacSHA1"));
			byte[] hash = mac.doFinal(ByteBuffer.allocate(Long.BYTES).putLong(counter).array());

			int offset = hash[hash.length - 1] & 0x0f;
			int binary = ((hash[offset] & 0x7f) << 24)
					| ((hash[offset + 1] & 0xff) << 16)
					| ((hash[offset + 2] & 0xff) << 8)
					| (hash[offset + 3] & 0xff);
			int otp = (int) (binary % 1_000_000);
			return String.format(Locale.ROOT, "%06d", otp);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("TOTP 코드를 생성하지 못했습니다.", e);
		}
	}

	private String encodeBase32(byte[] input) {
		StringBuilder output = new StringBuilder((input.length * 8 + 4) / 5);
		int buffer = 0;
		int bitsLeft = 0;
		for (byte value : input) {
			buffer = (buffer << 8) | (value & 0xff);
			bitsLeft += 8;
			while (bitsLeft >= 5) {
				output.append(BASE32_ALPHABET.charAt((buffer >> (bitsLeft - 5)) & 0x1f));
				bitsLeft -= 5;
			}
		}
		if (bitsLeft > 0) {
			output.append(BASE32_ALPHABET.charAt((buffer << (5 - bitsLeft)) & 0x1f));
		}
		return output.toString();
	}

	private byte[] decodeBase32(String input) {
		String normalized = input.trim().replace("=", "").toUpperCase(Locale.ROOT);
		byte[] output = new byte[normalized.length() * 5 / 8];
		int buffer = 0;
		int bitsLeft = 0;
		int index = 0;
		for (char value : normalized.toCharArray()) {
			int valueIndex = BASE32_ALPHABET.indexOf(value);
			if (valueIndex < 0) {
				throw new IllegalArgumentException("잘못된 TOTP 비밀키입니다.");
			}
			buffer = (buffer << 5) | valueIndex;
			bitsLeft += 5;
			if (bitsLeft >= 8) {
				output[index++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xff);
				bitsLeft -= 8;
			}
		}
		byte[] decoded = new byte[index];
		System.arraycopy(output, 0, decoded, 0, index);
		return decoded;
	}

	public long currentEpochSecond() {
		return Instant.now().getEpochSecond();
	}
}
