package com.web.manage.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.service.LoginService;
import com.web.manage.common.service.TotpSecretEncryptionService;
import com.web.manage.common.service.TotpService;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	private static final String PENDING_USER_ID = "S_TOTP_PENDING_USER_ID";
	private static final String PENDING_SECRET = "S_TOTP_PENDING_SECRET";
	private static final String PENDING_EXPIRES_AT = "S_TOTP_PENDING_EXPIRES_AT";
	private static final long PENDING_SESSION_TIMEOUT_MILLIS = 5 * 60 * 1000L;

	@Autowired
	private LoginService loginService;

	@Autowired
	private TotpService totpService;

	@Autowired
	private TotpSecretEncryptionService secretEncryptionService;

	@RequestMapping("/login")
	public String login() {
		return "pages/login";
	}
	@RequestMapping("/chkLogin")
	public @ResponseBody ReturnDataVO chkLogin(@RequestBody HashMap<String, Object> loginMap, HttpServletRequest request) {
		ReturnDataVO result = new ReturnDataVO();
		SessionVO loginUserVo = loginService.getUserRetrieve(loginMap);
		if(loginUserVo != null ){
			// 2중인증 적용시해제
			/*
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null) {
				oldSession.invalidate();
			}
			HttpSession session = request.getSession(true);
			session.setAttribute(PENDING_USER_ID, loginUserVo.getUserId());
			session.setAttribute(PENDING_EXPIRES_AT, System.currentTimeMillis() + PENDING_SESSION_TIMEOUT_MILLIS);

			String encryptedSecret = loginService.getTotpSecret(loginUserVo.getUserId());
			if (encryptedSecret == null || encryptedSecret.isBlank()) {
				String secret = totpService.generateSecret();
				session.setAttribute(PENDING_SECRET, secret);
				result.setResultCode("ENROLL");
				result.setResultMsg("인증 앱에 QR 코드를 등록하고 6자리 코드를 입력해 주세요.");
				result.setReturnVal(secret);
			} else {
				result.setResultCode("VERIFY");
				result.setResultMsg("인증 앱의 6자리 코드를 입력해 주세요.");
			}
			*/
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null) {
				oldSession.invalidate();
			}
			return completeLogin(loginUserVo.getUserId(), null, request);
		} else {
			result.setResultCode("99");
			result.setResultMsg("아이디 또는 비밀번호가 일치하지 않습니다.");
		}

		return result;
	}

	// 2중인증 적용시해제
	// @PostMapping("/verifyTotp")
	public @ResponseBody ReturnDataVO verifyTotp(@RequestBody Map<String, String> requestBody,
			HttpServletRequest request) {
		ReturnDataVO result = new ReturnDataVO();
		HttpSession session = getValidPendingSession(request);
		if (session == null) {
			return pendingSessionExpired();
		}

		String userId = (String) session.getAttribute(PENDING_USER_ID);
		if (loginService.isTotpLocked(userId)) {
			result.setResultCode("99");
			result.setResultMsg("인증 시도 횟수를 초과했습니다. 5분 후 다시 시도해 주세요.");
			return result;
		}

		String encryptedSecret = loginService.getTotpSecret(userId);
		if (encryptedSecret == null || encryptedSecret.isBlank()) {
			result.setResultCode("99");
			result.setResultMsg("2단계 인증 정보가 없습니다. 다시 로그인해 등록해 주세요.");
			return result;
		}

		String secret = secretEncryptionService.decrypt(encryptedSecret);
		Long counter = totpService.findMatchingCounter(secret, requestBody.get("code"), totpService.currentEpochSecond());
		if (counter == null || !loginService.acceptTotpCounter(userId, counter)) {
			loginService.recordTotpFailure(userId);
			result.setResultCode("99");
			result.setResultMsg("인증 코드가 올바르지 않거나 이미 사용되었습니다.");
			return result;
		}

		return completeLogin(userId, session, request);
	}

	// 2중인증 적용시해제
	// @PostMapping("/enrollTotp")
	public @ResponseBody ReturnDataVO enrollTotp(@RequestBody Map<String, String> requestBody,
			HttpServletRequest request) {
		ReturnDataVO result = new ReturnDataVO();
		HttpSession session = getValidPendingSession(request);
		if (session == null) {
			return pendingSessionExpired();
		}

		String userId = (String) session.getAttribute(PENDING_USER_ID);
		String secret = (String) session.getAttribute(PENDING_SECRET);
		if (secret == null || loginService.isTotpLocked(userId)) {
			result.setResultCode("99");
			result.setResultMsg("인증 설정을 계속할 수 없습니다. 처음부터 다시 로그인해 주세요.");
			return result;
		}

		Long counter = totpService.findMatchingCounter(secret, requestBody.get("code"), totpService.currentEpochSecond());
		if (counter == null) {
			loginService.recordTotpFailure(userId);
			result.setResultCode("99");
			result.setResultMsg("인증 코드가 올바르지 않습니다.");
			return result;
		}

		String encryptedSecret = secretEncryptionService.encrypt(secret);
		if (!loginService.enrollTotp(userId, encryptedSecret, counter)) {
			result.setResultCode("99");
			result.setResultMsg("인증 설정을 완료하지 못했습니다. 다시 로그인해 주세요.");
			return result;
		}

		return completeLogin(userId, session, request);
	}

	// 2중인증 적용시해제
	// @GetMapping("/totp/qr")
	public ResponseEntity<byte[]> totpQr(HttpServletRequest request) throws WriterException, IOException {
		HttpSession session = getValidPendingSession(request);
		if (session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String secret = (String) session.getAttribute(PENDING_SECRET);
		String userId = (String) session.getAttribute(PENDING_USER_ID);
		if (secret == null || userId == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		String uri = totpService.createProvisioningUri(userId, secret);
		BitMatrix matrix = new QRCodeWriter().encode(uri, BarcodeFormat.QR_CODE, 240, 240);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "PNG", output);
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG)
				.header("Cache-Control", "no-store")
				.body(output.toByteArray());
	}

	// 2중인증 적용시해제
	// @PostMapping("/totp/cancel")
	public @ResponseBody ReturnDataVO cancelTotp(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		ReturnDataVO result = new ReturnDataVO();
		result.setResultCode("00");
		return result;
	}

	private HttpSession getValidPendingSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute(PENDING_USER_ID) == null) {
			return null;
		}
		Long expiresAt = (Long) session.getAttribute(PENDING_EXPIRES_AT);
		if (expiresAt == null || System.currentTimeMillis() > expiresAt) {
			session.invalidate();
			return null;
		}
		return session;
	}

	private ReturnDataVO pendingSessionExpired() {
		ReturnDataVO result = new ReturnDataVO();
		result.setResultCode("99");
		result.setResultMsg("로그인 대기 시간이 만료되었습니다. 다시 로그인해 주세요.");
		return result;
	}

	private ReturnDataVO completeLogin(String userId, HttpSession pendingSession, HttpServletRequest request) {
		ReturnDataVO result = new ReturnDataVO();
		SessionVO loginUserVo = loginService.getUserRetrieveById(userId);
		if (loginUserVo == null) {
			if (pendingSession != null) {
				pendingSession.invalidate();
			}
			result.setResultCode("99");
			result.setResultMsg("사용자 정보를 확인할 수 없습니다. 다시 로그인해 주세요.");
			return result;
		}

		String authGrp = loginService.getAuthGrp(loginUserVo.getUserId());
		loginUserVo.setAuthGrpCd(authGrp);

		List<HashMap<String, Object>> menuList = new ArrayList<>();
		List<HashMap<String, Object>> menu2ndList = new ArrayList<>();
		List<HashMap<String, Object>> menu3rdList = new ArrayList<>();
		menuList = loginService.getMenuRetrieve(loginUserVo);
		loginUserVo.setMenu(menuList);
		menu2ndList = loginService.getMenuRetrieve2nd(loginUserVo);
		loginUserVo.setMenu2nd(menu2ndList);
		menu3rdList = loginService.getMenuRetrieve3rd(loginUserVo);
		loginUserVo.setMenu3rd(menu3rdList);

		if (pendingSession != null) {
			pendingSession.invalidate();
		}
		HttpSession session = request.getSession(true);
		session.setAttribute("userId", loginUserVo.getUserId());
		session.setAttribute("S_USER", loginUserVo);
		session.setAttribute("S_LOGIN_YN", "Y");
		// 2중인증 적용시해제
		// session.setAttribute("S_TOTP_VERIFIED", "Y");
		session.setAttribute("theme", "default");
		result.setResultCode("00");
		result.setResultMsg("로그인 성공");
		return result;
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "pages/login";
	}
	
}
