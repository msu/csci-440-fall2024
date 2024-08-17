package edu.montana.csci.csci440.demo;

import static org.junit.jupiter.api.Assertions.*;

public class RPNCalculatorTest {

    public void testBasicRPNExpression() {
        RPNCalculator calc = new RPNCalculator();
        assertEquals(2, calc.evaluate("1 1 +"));
    }

    public void testBadExpressionThrowsIllegalArgumentException() {
        RPNCalculator calc = new RPNCalculator();
        assertThrows(IllegalArgumentException.class, () -> calc.evaluate("1 1 1 +"));
    }

}
