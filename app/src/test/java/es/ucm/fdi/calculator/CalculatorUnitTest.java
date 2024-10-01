package es.ucm.fdi.calculator;
import org.junit.Test;

import static org.junit.Assert.*;
import es.ucm.fdi.calculator.Calculator;

public class CalculatorUnitTest {
    @Test
    public void addition_isCorrect() {
        Calculator c = new Calculator();
        assertEquals(4, c.suma(2,2),3);
        assertEquals(3.2,c.suma(1.1,2.1),3);
        assertEquals(-5.4,c.suma(10,-15.4),3);
    }

}
