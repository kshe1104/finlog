package com.finance.finlog.domain.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.finlog.domain.transaction.dto.TransactionResponse;
import com.finance.finlog.domain.transaction.service.TransactionService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Disabled("미완성 테스트")
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private final Long mockUserId = 1L;

    @Test
    @DisplayName("1. 거래 내역 목록 조회 테스트(파라미터 없을 때, 현재 연/월 기본값 검증)")
    void getTransactionTest() throws Exception{
        // given(준비) : 서비스가 리턴해줄 가짜 내역 리스트 만들기
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

    }
}
