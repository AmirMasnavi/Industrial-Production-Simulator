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

    /**
     * Test case for inserting materials and displaying them in increasing order of quantity.
     */
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

    /**
     * Test case for inserting materials and displaying them in decreasing order of quantity.
     */
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

    /**
     * Test case for inserting duplicate materials with the same quantity.
     */
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

    /**
     * Test case for displaying materials in an empty tree.
     */
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
     * Test case for displaying the total materials and their quantities.
     */
    @Test
    void testDisplayTotalMaterials() {
        outputStream.reset();

        materialBST.displayTotalMaterialsTest();
        String expectedOutputEmpty = """
            Total Quantity of Materials Used: 0.0
            """;
        assertEquals(expectedOutputEmpty.strip(), outputStream.toString().strip());
        outputStream.reset();

        materialBST.insert(25.0, "Material Single");
        materialBST.displayTotalMaterialsTest();
        String expectedOutputSingleNode = """
            Quantity: 25.0, Materials: [Material Single]
            Total Materials in Node: 1
            Total Quantity of Materials Used: 25.0
            """;
        assertEquals(expectedOutputSingleNode.strip(), outputStream.toString().strip());
        outputStream.reset();

        materialBST.insert(15.0, "Material X");
        materialBST.insert(10.0, "Material Y");
        materialBST.insert(20.0, "Material Z");
        materialBST.insert(15.0, "Material W");
        materialBST.displayTotalMaterialsTest();
        String expectedOutputMultipleNodes = """
            Quantity: 10.0, Materials: [Material Y]
            Total Materials in Node: 1
            Quantity: 15.0, Materials: [Material X, Material W]
            Total Materials in Node: 2
            Quantity: 20.0, Materials: [Material Z]
            Total Materials in Node: 1
            Total Quantity of Materials Used: 60.0
            """;
        assertEquals(expectedOutputMultipleNodes.strip(), outputStream.toString().strip());
    }

    /**
     * Test case for updating the quantity of an existing material.
     */
    @Test
    void testUpdateMaterialQuantityExistingMaterial() {
        // Initial setup
        materialBST.insert(10.0, "Material A");
        materialBST.insert(20.0, "Material B");
        materialBST.insert(15.0, "Material C");

        // Update an existing material
        materialBST.updateMaterialQuantity("Material B", 25.0);

        // Verify the output
        materialBST.displayInOrder();
        String expectedOutput = """
            Materials in Increasing Order of Quantity:
            Quantity: 10.0, Materials: [Material A]
            Quantity: 15.0, Materials: [Material C]
            Quantity: 25.0, Materials: [Material B]
            """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
        outputStream.reset();
    }

    /**
     * Test case for attempting to update a material that doesn't exist.
     */
    @Test
    void testUpdateMaterialQuantityNonExistingMaterial() {
        // Initial setup
        materialBST.insert(10.0, "Material A");
        materialBST.insert(20.0, "Material B");

        // Try to update a non-existing material
        materialBST.updateMaterialQuantity("Material X", 30.0);

        // Verify that the structure remains unchanged
        materialBST.displayInOrder();
        String expectedOutput = """
            Materials in Increasing Order of Quantity:
            Quantity: 10.0, Materials: [Material A]
            Quantity: 20.0, Materials: [Material B]
            """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
        outputStream.reset();
    }

    /**
     * Test case for updating a material quantity when there are duplicate materials.
     */
    @Test
    void testUpdateMaterialQuantityWithDuplicateMaterials() {
        // Initial setup
        materialBST.insert(10.0, "Material A");
        materialBST.insert(10.0, "Material B"); // Same quantity as Material A

        // Update an existing material
        materialBST.updateMaterialQuantity("Material B", 15.0);

        // Verify the output
        materialBST.displayInOrder();
        String expectedOutput = """
            Materials in Increasing Order of Quantity:
            Quantity: 10.0, Materials: [Material A]
            Quantity: 15.0, Materials: [Material B]
            """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
        outputStream.reset();
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
