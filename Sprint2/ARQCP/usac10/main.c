#include <stdio.h>
#include "asm.h"

int main() {
    int vec[] = {-1,-3,-1, -2}; // Array inicial
    int length = 4;
    int me;

    // Chama a função assembly	
    int result = median(vec, length, &me);

    if (result) {
        printf("Array ordenado: ");
        for (int i = 0; i < length; i++) {
            printf("%d ", vec[i]);
        }
        printf("\n");
        printf("Mediana: %d\n", me);
    } else {
        printf("Erro: comprimento inválido.\n");
    }

    return 0;
}
