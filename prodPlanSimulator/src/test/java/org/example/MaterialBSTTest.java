package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MaterialBST, which tests various functionalities of the MaterialBST class.
 */
class MaterialBSTTest {

    private MaterialBST materialBST;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * Sets up the test environment by initializing the MaterialBST instance
     * and redirecting System.out to capture output.
     */
    @BeforeEach
    void setUp() {
        materialBST = new MaterialBST();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out for testing output
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



    /**
     * Test case for attempting to update a material in an empty tree.
     */
    @Test
    void testUpdateMaterialQuantityEmptyTree() {
        // Attempt to update in an empty tree
        materialBST.updateMaterialQuantity("Material A", 50.0);

        // Verify that no unexpected output is produced
        materialBST.displayInOrder();
        String expectedOutput = """
            Materials in Increasing Order of Quantity:
            """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
        outputStream.reset();
    }
}
