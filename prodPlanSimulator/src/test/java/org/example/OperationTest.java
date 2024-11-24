package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Operation class, testing its constructor, methods, and behavior.
 */
public class OperationTest {

    private Operation operation;

    /**
     * Sets up the test environment by initializing an Operation instance
     * before each test method is run.
     */
    @BeforeEach
    public void setUp() {
        operation = new Operation(14, "drill bench leg");
    }

    /**
     * Test case for verifying the constructor and fields of the Operation class.
     * It checks if the id and name are set correctly.
     */
    @Test
    public void testOperationConstructorAndFields() {
        assertEquals(14, operation.getId());
        assertEquals("drill bench leg", operation.getName());
    }

    /**
     * Test case for the toString method of the Operation class.
     * It verifies that the string representation matches the expected format.
     */
    @Test
    public void testOperationToString() {
        String expected = "Operation{id=14, name='drill bench leg'}";
        assertEquals(expected, operation.toString());
    }

    /**
     * Test case for setters and getters of the Operation class.
     * It ensures that the setter methods work as expected and the values are updated correctly.
     */
    @Test
    public void testSettersAndGetters() {
        operation.setId(16);
        operation.setName("drill bench seat");
        assertEquals(16, operation.getId());
        assertEquals("drill bench seat", operation.getName());
    }

    /**
     * Test case for the equality method of the Operation class.
     * It checks if two Operation objects with the same id and name are considered equal.
     * Also checks if different names make them unequal.
     */
    @Test
    public void testOperationEquality() {
        Operation anotherOperation = new Operation(14, "drill bench leg");
        assertEquals(operation, anotherOperation);

        anotherOperation.setName("fix nut M16");
        assertNotEquals(operation, anotherOperation);
    }

    /**
     * Test case for the hashCode method of the Operation class.
     * It checks if two equal Operation objects return the same hash code,
     * and ensures that different objects with different names have different hash codes.
     */
    @Test
    public void testOperationHashCode() {
        Operation anotherOperation = new Operation(14, "drill bench leg");
        assertEquals(operation.hashCode(), anotherOperation.hashCode());

        anotherOperation.setName("fix nut M16");
        assertNotEquals(operation.hashCode(), anotherOperation.hashCode());
    }
}
