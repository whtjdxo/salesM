package com.web.manage.loan.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.config.interceptor.AuthInterceptor;
import com.web.manage.loan.domain.LoanMstVO;
import com.web.manage.loan.domain.LoanRepayScheduleVO;
import com.web.manage.loan.mapper.LoanMapper;

import ch.qos.logback.classic.Logger;

@Service
public class LoanService {
    static final Logger logger = (Logger) LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private LoanMapper loanMapper;

    public int getQueryTotalCnt() {
        return loanMapper.getQueryTotalCnt();
    }
    public List<HashMap<String, Object>> getLoanSummary(HashMap<String, Object> hashmapParam) {
        return loanMapper.getLoanSummary(hashmapParam);
    }
    public HashMap<String, Object> getLoanSummTotal(HashMap<String, Object> hashmapParam) {
        return loanMapper.getLoanSummTotal(hashmapParam);
    }   
    public List<HashMap<String, Object>> getChainLoanList(HashMap<String, Object> hashmapParam) {
        return loanMapper.getChainLoanList(hashmapParam);
    }

    public HashMap<String, Object> getChainLoanListTotal(HashMap<String, Object> hashmapParam) {
        return loanMapper.getChainLoanListTotal(hashmapParam);
    }

    public List<HashMap<String, Object>> getLoanRepaymentList(HashMap<String, Object> hashmapParam) {
        return loanMapper.getLoanRepaymentList(hashmapParam);
    }

    public List<LoanRepayScheduleVO> getLoanRepaymentVOs(
													BigDecimal loanPrincAmt, 
													BigDecimal intRate, 
													int loanDays,
													LocalDate loanSDate) {
        
        List<LoanRepayScheduleVO> schedule = new ArrayList<>();

        // 일일 이자율 계산
        BigDecimal dailyInterestRate = intRate
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);

        // 총 상환 회차 수 (1일 1회차 기준)
        int totalPayments = loanDays;

        // 일별 상환액 계산 (원리금 균등 상환)
        BigDecimal dailyPayment = calcDailyRepayment(loanPrincAmt, dailyInterestRate, totalPayments);

        BigDecimal balanceAmt = loanPrincAmt;
        LocalDate scDate = loanSDate;

        for (int scSeq = 1; scSeq <= totalPayments; scSeq++) {
            // 이자 계산
            BigDecimal dailyRepayIntAmt = balanceAmt.multiply(dailyInterestRate)
                    .setScale(0, RoundingMode.DOWN);

            // 원금 상환액 계산
            BigDecimal dailyRepayPrincAmt = dailyPayment.subtract(dailyRepayIntAmt)
                    .setScale(0, RoundingMode.HALF_UP);

            // 잔액 조정
            balanceAmt = balanceAmt.subtract(dailyRepayPrincAmt)
                    .setScale(0, RoundingMode.HALF_UP);

            // 마지막 회차 조정 (잔액이 0이 되도록)
            if (scSeq == totalPayments) {
                dailyRepayPrincAmt = dailyRepayPrincAmt.add(balanceAmt);
                balanceAmt = BigDecimal.ZERO;
            }

            // 상환 내역 추가 
            LoanRepayScheduleVO repayVo = new LoanRepayScheduleVO();
            repayVo.setSc_seq(String.valueOf(scSeq));
            repayVo.setSc_date(scDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            repayVo.setRepay_tot_amt(dailyPayment.toPlainString());
            repayVo.setRepay_princ_amt(dailyRepayPrincAmt.toPlainString());
            repayVo.setRepay_int_amt(dailyRepayIntAmt.toPlainString());
            repayVo.setBalance_amt(balanceAmt.toPlainString()); 
            
            repayVo.setRecv_tot_amt("0");
            repayVo.setRecv_princ_amt("0");
            repayVo.setRecv_int_amt("0");

            repayVo.setRemain_tot_amt(dailyPayment.toPlainString());
            repayVo.setRemain_princ_amt(dailyRepayPrincAmt.toPlainString());
            repayVo.setRemain_int_amt(dailyRepayIntAmt.toPlainString());
             
            schedule.add(repayVo);

            // 다음 날짜로 이동
            scDate = scDate.plusDays(1);
        }
        printRepaySchdule(schedule, loanPrincAmt, intRate);            
        return schedule;
    }

    private static BigDecimal calcDailyRepayment(
													BigDecimal repayPrincAmt, 
													BigDecimal dailyInterestRate, 
													int totalPayments) {
        
        // 일별 상환액 = P * [r(1+r)^n] / [(1+r)^n - 1]
        BigDecimal numerator = dailyInterestRate
                .multiply(BigDecimal.ONE.add(dailyInterestRate).pow(totalPayments));
        
        BigDecimal denominator = BigDecimal.ONE.add(dailyInterestRate)
                .pow(totalPayments)
                .subtract(BigDecimal.ONE);
        
        return repayPrincAmt.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }

     
	private static void printRepaySchdule( 
                                    List<LoanRepayScheduleVO> schedule,             
									BigDecimal repayPrincAmt,             
									BigDecimal intRate) {        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

        System.out.println("\n대출 상환 스케줄");
        System.out.println("원금: " + currencyFormat.format(repayPrincAmt));
        System.out.println("연 이자율: " + percentFormat.format(intRate.divide(BigDecimal.valueOf(100))));
        System.out.println("대출 기간: " + schedule.size() + "일");
        System.out.println("매일 상환액: " + decimalFormat.format(new BigDecimal(schedule.get(0).getRepay_tot_amt())));
        System.out.println("총 상환액: " + schedule.get(0).getRepay_tot_amt());
        BigDecimal totalInterest = schedule.stream()
            .map(vo -> new BigDecimal(vo.getRepay_int_amt()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("총 이자: " + currencyFormat.format(totalInterest));
        
        System.out.println("\n상세 상환 스케줄");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s %-12s %-15s %-15s %-15s %-15s\n", 
                "회차", "납부일", "상환액", "원금", "이자", "잔액");
        System.out.println("--------------------------------------------------------------------------------------------------");
        
        for (LoanRepayScheduleVO payment : schedule) {
            System.out.printf("%-8s %-12s %-15s %-15s %-15s %-15s\n",
                    payment.getSc_seq(),
                    payment.getSc_date(),
                    decimalFormat.format(new BigDecimal(payment.getRepay_tot_amt())),
                    decimalFormat.format(new BigDecimal(payment.getRepay_princ_amt())),
                    decimalFormat.format(new BigDecimal(payment.getRepay_int_amt())),
                    decimalFormat.format(new BigDecimal(payment.getBalance_amt()))
                );
        }
    } 

    public boolean insertLoanMst(LoanMstVO loanMstVo ) {
        return loanMapper.insertLoanMst(loanMstVo); 
    }
    public boolean updateLoanMst(LoanMstVO loanMstVo ) {
        return loanMapper.updateLoanMst(loanMstVo); 
    }

    public boolean insertLoanRepaySchedule(LoanRepayScheduleVO repaySchedule ) {
        return loanMapper.insertLoanRepaySchedule(repaySchedule); 
    }
 
}
