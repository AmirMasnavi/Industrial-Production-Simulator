#include <stdio.h>
#include "asm.h"

int main() {
    char str[] = "TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80";
    char token1[] = "TEMP";
    char token2[] = "AAA";
    char unit[20];
    int value, res;

    // Primeiro token
    res = extract_data(str, token1, unit, &value);
    printf("%d:%s,%d\n", res, unit, value); // Resultado esperado: 1:celsius,20

    // Segundo token
    res = extract_data(str, token2, unit, &value);
    printf("%d:%s,%d\n", res, unit, value); // Resultado esperado: 0:,0

    return 0;
}

