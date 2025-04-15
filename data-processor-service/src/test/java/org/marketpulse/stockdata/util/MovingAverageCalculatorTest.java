package org.marketpulse.stockdata.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovingAverageCalculatorTest {

    private MovingAverageCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new MovingAverageCalculator();
        ReflectionTestUtils.setField(calculator, "defaultPeriod", 5);
    }

    @Test
    void testCalculateMovingAverageWithDefaultPeriod() {
        // Given
        Deque<Double> prices = new LinkedList<>();
        prices.add(10.0);
        prices.add(20.0);
        prices.add(30.0);
        prices.add(40.0);
        prices.add(50.0);
        
        // When
        double average = calculator.calculateMovingAverage(prices);
        
        // Then
        assertEquals(30.0, average, 0.001);
    }

    @Test
    void testCalculateMovingAverageWithCustomPeriod() {
        // Given
        Deque<Double> prices = new LinkedList<>();
        prices.add(10.0);
        prices.add(20.0);
        prices.add(30.0);
        prices.add(40.0);
        prices.add(50.0);
        
        // When
        double average = calculator.calculateMovingAverage(prices, 3);
        
        // Then
        assertEquals(20.0, average, 0.001);
    }

    @Test
    void testCalculateMovingAverageWithEmptyQueue() {
        // Given
        Deque<Double> prices = new LinkedList<>();
        
        // When
        double average = calculator.calculateMovingAverage(prices);
        
        // Then
        assertEquals(0.0, average, 0.001);
    }

    @Test
    void testCalculateMovingAverageWithNullQueue() {
        // When
        double average = calculator.calculateMovingAverage(null);
        
        // Then
        assertEquals(0.0, average, 0.001);
    }
}

