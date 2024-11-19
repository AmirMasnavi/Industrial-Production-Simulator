package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MaterialBSTTest {

    private MaterialBST materialBST;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        materialBST = new MaterialBST();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out for testing output
    }

    @Test
    void testInsertAndDisplayInOrder() {
        materialBST.insert(10.0, "Material A");
        materialBST.insert(5.0, "Material B");
        materialBST.insert(20.0, "Material C");
        materialBST.insert(10.0, "Material D"); // Same quantity as Material A

        materialBST.displayInOrder();

        String expectedOutput = """
                Materials in Increasing Order of Quantity:
                Quantity: 5.0, Materials: [Material B]
                Quantity: 10.0, Materials: [Material A, Material D]
                Quantity: 20.0, Materials: [Material C]
                """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
    }

    @Test
    void testInsertAndDisplayInReverseOrder() {
        materialBST.insert(15.0, "Material X");
        materialBST.insert(10.0, "Material Y");
        materialBST.insert(20.0, "Material Z");
        materialBST.insert(15.0, "Material W"); // Same quantity as Material X

        materialBST.displayInReverseOrder();

        String expectedOutput = """
                Materials in Decreasing Order of Quantity:
                Quantity: 20.0, Materials: [Material Z]
                Quantity: 15.0, Materials: [Material X, Material W]
                Quantity: 10.0, Materials: [Material Y]
                """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
    }

    @Test
    void testInsertDuplicates() {
        materialBST.insert(10.0, "Material A");
        materialBST.insert(10.0, "Material B"); // Same quantity as Material A
        materialBST.insert(10.0, "Material C"); // Same quantity as Material A

        materialBST.displayInOrder();

        String expectedOutput = """
                Materials in Increasing Order of Quantity:
                Quantity: 10.0, Materials: [Material A, Material B, Material C]
                """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
    }

    @Test
    void testEmptyTreeDisplay() {
        materialBST.displayInOrder();

        String expectedOutput = """
                Materials in Increasing Order of Quantity:
                """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());

        outputStream.reset();

        materialBST.displayInReverseOrder();

        String expectedOutputReverse = """
                Materials in Decreasing Order of Quantity:
                """;
        assertEquals(expectedOutputReverse.strip(), outputStream.toString().strip());
    }
}
