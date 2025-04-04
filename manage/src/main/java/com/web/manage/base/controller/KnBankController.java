package com.web.manage.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.manage.base.domain.CorpVO;
import com.web.manage.base.domain.KnBankRegAccountVO;
import com.web.manage.common.domain.ReturnDataVO;
import com.web.manage.common.domain.SessionVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/base/trans/transKnBank/")
public class KnBankController {
    @RequestMapping("view")
    public String view() {
        return "pages/base/transKnBank";
    }


    @RequestMapping(value = "/send", method = RequestMethod.POST)    
    public @ResponseBody ReturnDataVO corpCreate(@ModelAttribute("KnBankRegAccountVO") @Valid KnBankRegAccountVO knBankVo, BindingResult bindingResult, HttpSession session) {
        ReturnDataVO result = new ReturnDataVO();
        try {
            String dtRecord = String.format("%-9s%-8s%-2s%-4s%-3s%-1s%6s%8s%6s%-4s%-4s%8s%6s%-15s%-3s%-13s%-1s%7s%-2s%-16s%-1s%2s%-6s%8s%-1s%-4s%-1s%-13s%-20s%-16s%-10s%-3s%-7s%-20s%-1s%-1s%-4s%7s%-49s",
                knBankVo.getCmRecordGb(),
                knBankVo.getCmCompCd(),
                knBankVo.getCmBankCd2(),
                knBankVo.getCmMsgCode(),
                knBankVo.getCmWorkGb(),
                knBankVo.getCmSendCnt(),
                knBankVo.getCmTransNo(),
                knBankVo.getCmTransDt(),
                knBankVo.getCmTransTm(),
                knBankVo.getCmRetrunCd(),
                knBankVo.getCmBankReturnCd(),
                knBankVo.getCmSearchDt(),
                knBankVo.getCmSearchNo(),
                knBankVo.getCmBankTransNo(),
                knBankVo.getCmBankCd3(),
                knBankVo.getCmBufferField(),
                knBankVo.getRecordGb(),
                knBankVo.getSeq(),
                knBankVo.getBankCd2(),
                knBankVo.getAccounNo(),
                knBankVo.getReqGb(),
                knBankVo.getAutoPayDt(),
                knBankVo.getBranchCd6(),
                knBankVo.getReqDt(),
                knBankVo.getResult(),
                knBankVo.getResultCd(),
                knBankVo.getIdChk(),
                knBankVo.getIdNo(),
                knBankVo.getPayerNo(),
                knBankVo.getCorpUseInof(),
                knBankVo.getOrgCd(),
                knBankVo.getBankCd3(),
                knBankVo.getBranchCd7(),
                knBankVo.getUseOrganCd(),
                knBankVo.getDepositorGb(),
                knBankVo.getAgreeFileGb(),
                knBankVo.getFileExtNm(),
                knBankVo.getFileSize(),
                knBankVo.getBuffer()
                ); 

                // String todayDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
                // String currentTime = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
                // // System.out.println("Today's date: " + todayDate);
                //     SessionVO member = (SessionVO) session.getAttribute("S_USER");
                //     corpVO.setEnt_user_id(member.getUserId());
        
                //     if (corpService.corpCreate(corpVO)) {
                //         System.out.println("corpCreate success");
                //         result.setResultCode("S000");
                //         result.setResultMsg("corp creation successful.");
                //     } else {
                //         System.out.println("corpCreate fail");
                //         result.setResultCode("F000");
                //         result.setResultMsg("corp creation failed.");
                //     }
        } catch (Exception e) {
            result.setResultCode("F000");
            result.setResultMsg("corp creation failed.");
            e.printStackTrace();
        }
        return result;
        }
            
            
                private String formatCString(String str, int i) { 
                    if (str == null) {
                        str = "";
                    }
                    if (str.length() > i) {
                        return str.substring(0, i);
                    }
                    return String.format("%-" + i + "s", str);
                }


                private String formatNString(String str, int i) { 
                    if (str == null) {
                        str = "";
                    }
                    if (str.length() > i) {
                        return str.substring(0, i);
                    }
                    return String.format("%" + i + "s", str);
                }

    


}
