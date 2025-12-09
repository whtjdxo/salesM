package com.web.manage.deposit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.deposit.domain.DepositExcelDataVO;
import com.web.manage.deposit.domain.DepositExcelRowDataVO;
import com.web.manage.deposit.domain.ProcDepositDataVO;
import com.web.manage.deposit.domain.ProcDepositVO;
import com.web.manage.deposit.domain.ProcTransDepositVO;
import com.web.manage.deposit.mapper.DepositMapper;
import com.web.manage.loan.domain.LoanMstVO;
import com.web.manage.loan.domain.LoanRepayScheduleVO;

import ch.qos.logback.classic.Logger;

@Service
public class DepositService {

    @Autowired
    private DepositMapper depositMapper;
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);
    public int getQueryTotalCnt() {
        return depositMapper.getQueryTotalCnt();
    }

    public List<HashMap<String, Object>> getDepositSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepositSummary(hashmapParam);
    }

    public HashMap<String, Object> getDepositSummaryTotal(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepositSummaryTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepoCardSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoCardSummary(hashmapParam);
    }

    public HashMap<String, Object> getChainDepoStatus(HashMap<String, Object> hashmapParam) {
        return depositMapper.getChainDepoStatus(hashmapParam);
    }
    
    public List<HashMap<String, Object>> getDepoResvList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoResvList(hashmapParam);
    }

     public HashMap<String, Object> getSearchConfDate(HashMap<String, String> hashmapParam) {
        return depositMapper.getSearchConfDate(hashmapParam);
    }
    public HashMap<String, Object> getDepositConfDate(HashMap<String, String> hashmapParam) {
        return depositMapper.getDepositConfDate(hashmapParam);
    }

    //  transaction 처리 는 Procedure 에서 처리하도록 함
    public ReturnDataVO callProcChangeResvDate(ProcDepositVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            depositMapper.callProcChangeResvDate(procVo);            
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
            // 로깅 처리
            // logger.error("Scrap transaction processing failed", e);
        }
        return result; 
    }
 
    public ReturnDataVO callProcDepositAdjust(ProcDepositVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            depositMapper.callProcDepositAdjust(procVo);            
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
            // 로깅 처리
            // logger.error("Scrap transaction processing failed", e);
        }
        return result; 
    }

    public ReturnDataVO callProcDepositDelete(ProcDepositDataVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            depositMapper.callProcDeleteDeposit(procVo);            
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage()); 
        }
        return result; 
    }

    public List<HashMap<String, Object>> getDepoAdjustSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustSummary(hashmapParam);
    }

    public HashMap<String, Object> getDepoAdjustSummTotal(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustSummTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepoAdjustCardSummary(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustCardSummary(hashmapParam);
    }

    public List<HashMap<String, Object>> getDepoAdjustList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getDepoAdjustList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainDepositList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getChainDepositList(hashmapParam);
    }

    public List<HashMap<String, Object>> getChainMnulDepositList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getChainMnulDepositList(hashmapParam);
    }

    public HashMap<String, Object> getChainDepositTotal(HashMap<String, Object> hashmapParam) {
        return depositMapper.getChainDepositTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getCreditDepositList(HashMap<String, Object> hashmapParam) {
        return depositMapper.getCreditDepositList(hashmapParam);
    }

    public HashMap<String, Object> getCreditDepositTotal(HashMap<String, Object> hashmapParam) {
        return depositMapper.getCreditDepositTotal(hashmapParam);
    } 

    public ReturnDataVO callProcDepoAdjustCancel(ProcDepositVO procVo) {
        // return withdrawMapper.callProcRemitMain(procVo);
        ReturnDataVO result = new ReturnDataVO();
        try {
            depositMapper.callProcDepoAdjustCancel(procVo);            
            if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                result.setResultCode("S000");
                result.setResultMsg(procVo.getResultMsg());
            } else {
                result.setResultCode("F000");
                result.setResultMsg(procVo.getResultMsg());
                return result;
            } 
        } catch (Exception e) {
            result.setResultCode("F500");
            result.setResultMsg("시스템 오류가 발생했습니다: " + e.getMessage());
            // 로깅 처리
            // logger.error("Scrap transaction processing failed", e);
        }
        return result; 
    }


    @Transactional
    public ReturnDataVO uploadExcelData(DepositExcelDataVO excelDataVo) {     
        ReturnDataVO result = new ReturnDataVO();   
        int totCount = 0;
        int succCount = 0;
        int failCount = 0;
        int dupCount = 0;
        try {
            if(excelDataVo.getExcelData() == null || excelDataVo.getExcelData().size() == 0) {
                throw new RuntimeException("No data to upload.");
            } 
            // 회사 계좌번호 조회
            HashMap<String, Object> hparam = new HashMap<>();
            
            hparam.put("corpType", excelDataVo.getCorp_type());
            if("CH".equals(excelDataVo.getCorp_type())) {
                hparam.put("corpCd", excelDataVo.getChain_no());
            } else if ("OP".equals(excelDataVo.getCorp_type())) {
                hparam.put("corpCd", excelDataVo.getCorp_cd()); 
            }
            String corpAccountNo = depositMapper.getCorpAccountNo(hparam);

            for (DepositExcelRowDataVO rowData : excelDataVo.getExcelData()) {
                rowData.setCorpCd(excelDataVo.getCorp_cd());
                rowData.setCorpType(excelDataVo.getCorp_type());
                rowData.setBankCd(excelDataVo.getBank_cd());
                rowData.setEntUserId(excelDataVo.getEnt_user_id());                
                rowData.setAccountNo(corpAccountNo);
                rowData.setInAmt("".equals(rowData.getInAmt()) ? "0" : rowData.getInAmt().replace(",", ""));
                rowData.setOutAmt("".equals(rowData.getOutAmt()) ? "0" : rowData.getOutAmt().replace(",", ""));
                rowData.setRemainAmt("".equals(rowData.getRemainAmt()) ? "0" : rowData.getRemainAmt().replace(",", ""));

                // if (!depositMapper.excelUploadBankData(rowData)) {
                //     throw new RuntimeException("Deposit Manual Excel Data  insertion failed.");
                // }
                try {
                    depositMapper.excelUploadBankData(rowData);
                    succCount++;
                } catch (DuplicateKeyException e) {
                    dupCount++;
                    // throw new RuntimeException("중복 데이터가 존재합니다."); 
                    //중복데이터는 건너뛰고 계속 진행
                    logger.error("Duplicate key error during Excel data upload: ", e);

                } catch (DataAccessException e) {
                    // 기타 SQL 관련 예외
                    throw new RuntimeException("Error during Excel data upload: " + e.getMessage());
                }
                totCount++;
            }

            // 모든 데이터가 성공적으로 삽입된 경우 TRANS_DEPOSIT 실행
            if (succCount > 0) {
                ProcTransDepositVO procVo = new ProcTransDepositVO();
                procVo.setUserId(excelDataVo.getEnt_user_id());
                procVo.setChainNo(excelDataVo.getCorp_cd());

                depositMapper.callProcTransDepositBatch(procVo);
                if (procVo.getResultCode() == 0) { // 성공 코드 가정 (프로시저 정의에 따라 조정)
                    // return true;
                    result.setResultCode("S000");
                    result.setResultMsg("Excel data uploaded successfully. Total: " + totCount + ", Success: " + succCount + ", Duplicates: " + dupCount + ", Failed: " + failCount);
                } else {
                    // result.setResultCode("F000");
                    // result.setResultMsg("Excel data upload failed during deposit processing: " + procVo.getResultMsg());
                    logger.error("Error during Excel data upload: ", procVo.getResultMsg());    
                    throw new RuntimeException(procVo.getResultMsg());
                }
            } else {
                result.setResultCode("F000");
                result.setResultMsg("처리된 데이터가 없습니다. Total: " + totCount + ", Success: " + succCount + ", Duplicates: " + dupCount + ", Failed: " + failCount);  
            }
            return result;
        } catch (DataAccessException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            result.setResultCode("F500");
            result.setResultMsg("DB 처리 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        } catch (Exception e) {
            // 기타 예외 처리 rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  
            result.setResultCode("F500");
            result.setResultMsg("Error during Excel data upload: " + e.getMessage());
            return result;
            // logger.error("Error during Excel data upload: ", e);
            // throw e; // 트랜잭션 롤백을 위해 예외 재발생
        } 
    }

}