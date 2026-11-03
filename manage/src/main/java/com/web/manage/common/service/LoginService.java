package com.web.manage.common.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.manage.common.domain.SessionVO;
import com.web.manage.common.mapper.LoginMapper;


@Service
public class LoginService {

	@Autowired
	private LoginMapper loginMapper;

	public int chkLogin(HashMap<String, Object> loginMap) {
        
        return loginMapper.chkLogin(loginMap);
    }

    public SessionVO getUserRetrieve(HashMap<String, Object> loginMap) {
        
        return loginMapper.getUserRetrieve(loginMap);
    }

    public SessionVO getUserRetrieveById(String userId) {
        return loginMapper.getUserRetrieveById(userId);
    }

    public String getTotpSecret(String userId) {
        return loginMapper.getTotpSecret(userId);
    }

    public boolean enrollTotp(String userId, String secret, long counter) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("secret", secret);
        parameters.put("counter", counter);
        return loginMapper.enrollTotp(parameters) == 1;
    }

    public boolean acceptTotpCounter(String userId, long counter) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("counter", counter);
        return loginMapper.acceptTotpCounter(parameters) == 1;
    }

    public boolean isTotpLocked(String userId) {
        return loginMapper.isTotpLocked(userId) == 1;
    }

    public void recordTotpFailure(String userId) {
        loginMapper.recordTotpFailure(userId);
    }

    public String getAuthGrp(String userId) {
        return loginMapper.getAuthGrp(userId);
    }

    public List<HashMap<String, Object>> getMenuRetrieve(SessionVO loginUserVo) {
       return loginMapper.getMenuRetrieve(loginUserVo);
    }

    public List<HashMap<String, Object>> getMenuRetrieve2nd(SessionVO loginUserVo) {
        return loginMapper.getMenuRetrieve2nd(loginUserVo);
    }

    public List<HashMap<String, Object>> getMenuRetrieve3rd(SessionVO loginUserVo) {
        return loginMapper.getMenuRetrieve3rd(loginUserVo);
    }    
}
