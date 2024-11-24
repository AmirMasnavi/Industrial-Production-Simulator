package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BooDataResultTest {

    private BooDataResult booDataResult;

    @BeforeEach
    void setUp() {
        // Configuração inicial do objeto BooDataResult
        Map<Integer, Map<Integer, Double>> booData = new HashMap<>();
        Map<Integer, Double> subOperation1 = new HashMap<>();
        subOperation1.put(101, 5.0);
        subOperation1.put(102, 10.0);

        Map<Integer, Double> subOperation2 = new HashMap<>();
        subOperation2.put(201, 20.0);

        booData.put(1, subOperation1);
        booData.put(2, subOperation2);

        Map<Integer, Double> itemQuantities = new HashMap<>();
        itemQuantities.put(101, 5.0);
        itemQuantities.put(102, 10.0);
        itemQuantities.put(201, 20.0);

        booDataResult = new BooDataResult(booData, itemQuantities);
    }

    @Test
    void testUpdateExistingItemQuantityInItemQuantities() {
        // Atualizar um item existente no mapa itemQuantities
        booDataResult.updateItemQuantity(101, 7.5);

        // Verificar a atualização no itemQuantities
        assertEquals(7.5, booDataResult.getItemQuantities().get(101));
    }

    @Test
    void testUpdateExistingItemQuantityInBooDataAsChild() {
        // Atualizar um item existente que é uma sub-operação em booData
        booDataResult.updateItemQuantity(102, 15.0);

        // Verificar a atualização em booData
        assertEquals(15.0, booDataResult.getBooData().get(1).get(102));
    }

    @Test
    void testUpdateExistingItemQuantityInBooDataAsParent() {
        // Atualizar um item existente que é uma operação principal em booData
        booDataResult.updateItemQuantity(2, 25.0);

        // Verificar a atualização em booData
        assertEquals(25.0, booDataResult.getBooData().get(2).get(201));
    }

    @Test
    void testUpdateNonExistingItem() {
        // Tentar atualizar um item inexistente
        booDataResult.updateItemQuantity(999, 50.0);

        // Verificar que nada foi alterado
        assertEquals(null, booDataResult.getItemQuantities().get(999));
        assertEquals(5.0, booDataResult.getItemQuantities().get(101)); // Valor original permanece
    }

    @Test
    void testUpdatePropagatesCorrectlyAcrossDataStructures() {
        // Atualizar um item e verificar que as alterações propagam
        booDataResult.updateItemQuantity(201, 30.0);

        // Verificar no itemQuantities
        assertEquals(30.0, booDataResult.getItemQuantities().get(201));

        // Verificar no booData
        assertEquals(30.0, booDataResult.getBooData().get(2).get(201));
    }
}
