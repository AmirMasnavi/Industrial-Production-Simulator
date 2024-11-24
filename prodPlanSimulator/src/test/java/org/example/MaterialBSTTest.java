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

    @Test
    void testUpdateMaterialQuantityExistingMaterial() {
        // Configuração inicial
        materialBST.insert(10.0, "Material A");
        materialBST.insert(20.0, "Material B");
        materialBST.insert(15.0, "Material C");

        // Atualizar um material existente
        materialBST.updateMaterialQuantity("Material B", 25.0);

        // Verificar a saída
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

    @Test
    void testUpdateMaterialQuantityNonExistingMaterial() {
        // Configuração inicial
        materialBST.insert(10.0, "Material A");
        materialBST.insert(20.0, "Material B");

        // Tentar atualizar um material que não existe
        materialBST.updateMaterialQuantity("Material X", 30.0);

        // Verificar se a estrutura permanece inalterada
        materialBST.displayInOrder();
        String expectedOutput = """
            Materials in Increasing Order of Quantity:
            Quantity: 10.0, Materials: [Material A]
            Quantity: 20.0, Materials: [Material B]
            """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
        outputStream.reset();
    }

    @Test
    void testUpdateMaterialQuantityWithDuplicateMaterials() {
        // Configuração inicial
        materialBST.insert(10.0, "Material A");
        materialBST.insert(10.0, "Material B"); // Mesmo valor de quantidade

        // Atualizar um material existente
        materialBST.updateMaterialQuantity("Material B", 15.0);

        // Verificar a saída
        materialBST.displayInOrder();
        String expectedOutput = """
            Materials in Increasing Order of Quantity:
            Quantity: 10.0, Materials: [Material A]
            Quantity: 15.0, Materials: [Material B]
            """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
        outputStream.reset();
    }

    @Test
    void testUpdateMaterialQuantityEmptyTree() {
        // Tentar atualizar em uma árvore vazia
        materialBST.updateMaterialQuantity("Material A", 50.0);

        // Verificar se não há saída inesperada
        materialBST.displayInOrder();
        String expectedOutput = """
            Materials in Increasing Order of Quantity:
            """;
        assertEquals(expectedOutput.strip(), outputStream.toString().strip());
        outputStream.reset();
    }




}
