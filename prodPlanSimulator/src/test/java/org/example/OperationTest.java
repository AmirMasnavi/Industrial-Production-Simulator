package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OperationTest {

    private Operation operation;

    @BeforeEach
    public void setUp() {
        operation = new Operation(14, "drill bench leg");
    }

    @Test
    public void testOperationConstructorAndFields() {
        assertEquals(14, operation.getId());
        assertEquals("drill bench leg", operation.getName());
    }

    @Test
    public void testOperationToString() {
        String expected = "Operation{id=14, name='drill bench leg'}";
        assertEquals(expected, operation.toString());
    }

    @Test
    public void testSettersAndGetters() {
        operation.setId(16);
        operation.setName("drill bench seat");
        assertEquals(16, operation.getId());
        assertEquals("drill bench seat", operation.getName());
    }

    @Test
    public void testOperationEquality() {
        Operation anotherOperation = new Operation(14, "drill bench leg");
        assertEquals(operation, anotherOperation);

        anotherOperation.setName("fix nut M16");
        assertNotEquals(operation, anotherOperation);
    }

    @Test
    public void testOperationHashCode() {
        Operation anotherOperation = new Operation(14, "drill bench leg");
        assertEquals(operation.hashCode(), anotherOperation.hashCode());

        anotherOperation.setName("fix nut M16");
        assertNotEquals(operation.hashCode(), anotherOperation.hashCode());
    }
}
