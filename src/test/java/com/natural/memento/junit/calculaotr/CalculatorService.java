package com.natural.memento.junit.calculaotr;

public class CalculatorService {

    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("b는 0이면 안 됩니다.");
        }
        return a / b;
    }
}
