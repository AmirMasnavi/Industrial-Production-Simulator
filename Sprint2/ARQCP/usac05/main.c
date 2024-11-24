#include <stdio.h>
#include "asm.h"

// Função para imprimir o estado do buffer
void print_buffer(int* buffer, int length, int head, int tail) {
    printf("Buffer: [");
    for (int i = 0; i < length; i++) {
        printf("%d", buffer[i]);
        if (i < length - 1) printf(", ");
    }
    printf("] | Head: %d, Tail: %d\n", head, tail);
}

int main() {
    // Configuração inicial
    int buffer[4] = {0, 0, 0, 0}; // Buffer circular de tamanho 3
    int length = 4;            // Tamanho do buffer
    int head = 0;              // Posição inicial de leitura (head)
    int tail = 3;              // Posição inicial de escrita (tail)
    int value = 5;             // Valor a ser inserido
    int is_full;

    // Inserir valor no buffer
    printf("Inserting %d...\n", value);
    is_full = enqueue_value(buffer, length, &tail, &head, value);

    // Imprimir estado do buffer
    print_buffer(buffer, length, head, tail);

    // Verificar se o buffer está cheio
    printf("Is buffer full after insertion? %s\n", is_full ? "Yes" : "No");
    printf("----\n");

    return 0;
}
