package com.natural.memento.junit.calculaotr;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CalculatorServiceTest {

    private CalculatorService service;

    @BeforeAll
    static void beforeAll() {
        System.out.println("[BeforeAll] 전체 테스트 시작 전 1회");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("[BeforeEach] 각 테스트 시작 전");
        service = new CalculatorService();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("[AfterAll] 전체 테스트 종료 후 1회");
    }

    @Test
    @DisplayName("add: 10 + 20 = 30")
    void add_test() {
        int result = service.add(10, 20);
        assertEquals(30, result);
    }

    @Test
    @DisplayName("divide: b가 0이면 IllegalArgumentException")
    void divide_throw_test() {
        IllegalArgumentException e = Assertions.assertThrows(
                IllegalArgumentException.class, () ->
                        service.divide(10, 0)
        );
        assertEquals("b는 0이면 안 됩니다.", e.getMessage());
    }

    @Test
    @DisplayName("assertAll: 여러 검증을 한 번에 모아서 확인")
    void assertAll_test() {
        int result = service.add(1, 2);

        assertAll(
                () -> assertEquals(3, result),
                () -> assertTrue(result > 0),
                () -> assertNotEquals(0, result)
        );
    }

    @Nested
    @DisplayName("Nested로 테스트 묶기")
    class Nested_tests {

        @Test
        @DisplayName("Nested 내부 테스트도 Discovery 대상")
        void nested_add_test() {
            assertEquals(5, service.add(2, 3));
        }
    }
}


