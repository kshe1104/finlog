package com.finance.finlog.domain.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.entity.CategoryType;
import com.finance.finlog.domain.transaction.dto.MonthlyStatResponse;
import com.finance.finlog.domain.transaction.dto.TransactionRequest;
import com.finance.finlog.domain.transaction.dto.TransactionResponse;
import com.finance.finlog.domain.transaction.entity.Transaction;
import com.finance.finlog.domain.transaction.entity.TransactionType;
import com.finance.finlog.domain.transaction.service.TransactionService;
import com.finance.finlog.domain.user.entity.Provider;
import com.finance.finlog.domain.user.entity.Role;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.global.config.TestSecurityConfig;
import org.hibernate.annotations.Synchronize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TestSecurityConfig.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    @DisplayName("월별 거래 내역을 조회한다")
    void getTransactions_success() throws Exception {
        // given
        TransactionResponse response = createMockTransactionResponse();
        given(transactionService.getTransactions(any(), anyInt(), anyInt()))
                .willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/transactions")
                        .param("year", "2024")
                        .param("month", "3")
                        .requestAttr("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].amount").value(50000));
    }

    @Test
    @DisplayName("월별 통계를 조회한다")
    void getMonthlyStat_success() throws Exception {
        // given
        MonthlyStatResponse stat = new MonthlyStatResponse(
                2024, 3,
                new BigDecimal("500000"),
                new BigDecimal("300000")
        );
        given(transactionService.getMonthlyStat(any(), anyInt(), anyInt()))
                .willReturn(stat);

        // when & then
        mockMvc.perform(get("/api/transactions/stats")
                        .param("year", "2024")
                        .param("month", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalIncome").value(500000))
                .andExpect(jsonPath("$.data.totalExpense").value(300000))
                .andExpect(jsonPath("$.data.balance").value(200000));
    }

    @Test
    @DisplayName("거래를 등록한다")
    void createTransaction_success() throws Exception {
        // given
        TransactionRequest request = new TransactionRequest();
        setField(request, "type", TransactionType.EXPENSE);
        setField(request, "amount", new BigDecimal("50000"));
        setField(request, "description", "점심");
        setField(request, "transactionDate", LocalDate.of(2024, 3, 15));
        setField(request, "categoryId", 1L);

        TransactionResponse response = createMockTransactionResponse();
        given(transactionService.createTransaction(any(), any()))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("거래가 등록됐어요"));
    }

    @Test
    @DisplayName("거래를 삭제한다")
    void deleteTransaction_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("거래가 삭제됐어요"));

        verify(transactionService).deleteTransaction(any(), anyLong());
    }

    @Test
    @DisplayName("금액이 0이하면 400 에러가 발생한다")
    void createTransaction_invalidAmount_returns400() throws Exception {
        // given
        TransactionRequest request = new TransactionRequest();
        setField(request, "type", TransactionType.EXPENSE);
        setField(request, "amount", new BigDecimal("-1000")); // 음수
        setField(request, "transactionDate", LocalDate.of(2024, 3, 15));
        setField(request, "categoryId", 1L);

        // when & then
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    private TransactionResponse createMockTransactionResponse() {
        User user = User.builder()
                .email("test@gmail.com")
                .name("테스트")
                .provider(Provider.GOOGLE)
                .providerId("google-123")
                .role(Role.USER)
                .build();

        Category category = Category.builder()
                .user(user)
                .name("식비")
                .type(CategoryType.EXPENSE)
                .isDefault(false)
                .build();

        Transaction transaction = Transaction.builder()
                .user(user)
                .category(category)
                .type(TransactionType.EXPENSE)
                .amount(new BigDecimal("50000"))
                .transactionDate(LocalDate.of(2024,3,15))
                .isAutoGenerated(false)
                .build();

        return TransactionResponse.from(transaction);
    }

    private void setField(Object obj, String fieldName, Object value) {
        try {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}