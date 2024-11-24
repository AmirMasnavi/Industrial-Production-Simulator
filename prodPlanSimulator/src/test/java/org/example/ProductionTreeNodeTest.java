package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ProductionTreeNodeTest {

    private Item item;
    private Operation operation;
    private ProductionTreeNode itemNode;
    private ProductionTreeNode operationNode;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        item = new Item(1001, "bench leg w/hole");
        operation = new Operation(14, "drill bench leg");

        // Initialize nodes
        itemNode = new ProductionTreeNode(item);
        operationNode = new ProductionTreeNode(operation);
    }

    @Test
    public void testProductionTreeNodeConstructorAndFields() {
        assertNotNull(itemNode);
        assertNotNull(operationNode);

        // Test the item node
        assertEquals(item, itemNode.getItem());
        assertNull(itemNode.getOperation());

        // Test the operation node
        assertEquals(operation, operationNode.getOperation());
        assertNull(operationNode.getItem());
    }

    @Test
    public void testAddChild() {
        ProductionTreeNode childNode = new ProductionTreeNode(new Item(1002, "bench leg w/bolt"));
        itemNode.addChild(childNode);

        List<ProductionTreeNode> children = itemNode.getChildren();
        assertEquals(1, children.size());
        assertEquals(childNode, children.get(0));
    }

    @Test
    public void testSettersAndGetters() {
        itemNode.setItem(new Item(1010, "updated bench leg"));
        assertEquals(1010, itemNode.getItem().getId());
        assertEquals("updated bench leg", itemNode.getItem().getName());

        operationNode.setOperation(new Operation(20, "varnish bench"));
        assertEquals(20, operationNode.getOperation().getId());
        assertEquals("varnish bench", operationNode.getOperation().getName());
    }

    @Test
    public void testProductionTreeNodeToString() {
        String expectedItemNode = "ProductionTreeNode{item=Item{id=1001, name='bench leg w/hole'}}";
        assertEquals(expectedItemNode, itemNode.toString());

        String expectedOperationNode = "ProductionTreeNode{operation=Operation{id=14, name='drill bench leg'}}";
        assertEquals(expectedOperationNode, operationNode.toString());
    }

    @Test
    public void testUpdateMaterialQuantityRootNodeOnly() {
        // Configurar a quantidade inicial
        itemNode.setQuantity(10.0);

        // Atualizar a quantidade
        itemNode.updateMaterialQuantity(20.0);

        // Verificar se a quantidade foi atualizada corretamente
        assertEquals(20.0, itemNode.getQuantity());
    }

    @Test
    public void testUpdateMaterialQuantityWithChildren() {
        // Configurar a estrutura da árvore
        itemNode.setQuantity(10.0);
        ProductionTreeNode child1 = new ProductionTreeNode(new Item(1002, "bench top"));
        child1.setQuantity(5.0);

        ProductionTreeNode child2 = new ProductionTreeNode(new Item(1003, "bench screws"));
        child2.setQuantity(2.0);

        itemNode.addChild(child1);
        itemNode.addChild(child2);

        // Atualizar a quantidade no nó raiz
        itemNode.updateMaterialQuantity(20.0); // Novo valor do nó raiz

        // Verificar se a quantidade foi atualizada corretamente no nó raiz
        assertEquals(20.0, itemNode.getQuantity());

        // Verificar se a quantidade foi atualizada proporcionalmente nos filhos
        assertEquals(10.0, child1.getQuantity()); // 5.0 * 2.0 (fator de escala)
        assertEquals(4.0, child2.getQuantity());  // 2.0 * 2.0 (fator de escala)
    }

    @Test
    public void testUpdateMaterialQuantityWithNestedChildren() {
        // Configurar a estrutura da árvore
        itemNode.setQuantity(10.0);
        ProductionTreeNode child1 = new ProductionTreeNode(new Item(1002, "bench top"));
        child1.setQuantity(5.0);

        ProductionTreeNode child2 = new ProductionTreeNode(new Item(1003, "bench screws"));
        child2.setQuantity(2.0);

        ProductionTreeNode grandChild = new ProductionTreeNode(new Item(1004, "screw nut"));
        grandChild.setQuantity(1.0);

        child2.addChild(grandChild);
        itemNode.addChild(child1);
        itemNode.addChild(child2);

        // Atualizar a quantidade no nó raiz
        itemNode.updateMaterialQuantity(20.0); // Novo valor do nó raiz

        // Verificar se a quantidade foi atualizada corretamente no nó raiz
        assertEquals(20.0, itemNode.getQuantity());

        // Verificar se a quantidade foi atualizada proporcionalmente nos filhos
        assertEquals(10.0, child1.getQuantity());    // 5.0 * 2.0 (fator de escala)
        assertEquals(4.0, child2.getQuantity());     // 2.0 * 2.0 (fator de escala)
        assertEquals(2.0, grandChild.getQuantity()); // 1.0 * 2.0 (fator de escala)
    }

    @Test
    public void testUpdateMaterialQuantityNoChildren() {
        // Configurar a quantidade inicial
        itemNode.setQuantity(15.0);

        // Atualizar a quantidade
        itemNode.updateMaterialQuantity(30.0);

        // Verificar se a quantidade foi atualizada corretamente
        assertEquals(30.0, itemNode.getQuantity());
        assertTrue(itemNode.getChildren().isEmpty()); // Sem filhos
    }

    @Test
    public void testUpdateMaterialQuantityZeroQuantity() {
        // Configurar a estrutura da árvore
        itemNode.setQuantity(10.0);
        ProductionTreeNode child = new ProductionTreeNode(new Item(1002, "bench top"));
        child.setQuantity(5.0);
        itemNode.addChild(child);

        // Atualizar a quantidade para zero
        itemNode.updateMaterialQuantity(0.0);

        // Verificar se a quantidade foi atualizada para zero no nó raiz
        assertEquals(0.0, itemNode.getQuantity());

        // Verificar se as quantidades dos filhos também foram zeradas
        assertEquals(0.0, child.getQuantity());
    }



}
