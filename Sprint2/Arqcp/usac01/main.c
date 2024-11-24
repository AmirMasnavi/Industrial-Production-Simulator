#include <stdio.h>
#include "asm.h"

// DeclaraÃ§Ã£o da funÃ§Ã£o `extract_data` em Assembly
extern int extract_data(char* str, char* token, char* unit, int* value);

int main() {
    // String de entrada conforme o formato especificado no exercÃ­cio
    char str[] = "TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80";

    // VariÃ¡veis para testar o token TEMP
    char token1[] = "TEMP";
    char unit1[20]; // Buffer para armazenar a unidade
    int value1;     // Inteiro para armazenar o valor

    // Chamar a funÃ§Ã£o `extract_data` para TEMP
    int res1 = extract_data(str, token1, unit1, &value1);

    // Mostrar o resultado para TEMP
    printf("Resultado (TEMP): %d\n", res1);
    if (res1 == 1) {
        printf("Unidade: %s, Valor: %d\n", unit1, value1);
    } else {
        printf("Erro: Unidade vazia, Valor = %d\n", value1);
    }

    // VariÃ¡veis para testar o token HUM
    char token2[] = "HUM";
    char unit2[20]; // Buffer para armazenar a unidade
    int value2;     // Inteiro para armazenar o valor

    // Chamar a funÃ§Ã£o `extract_data` para HUM
    int res2 = extract_data(str, token2, unit2, &value2);

    // Mostrar o resultado para HUM
    printf("Resultado (HUM): %d\n", res2);
    if (res2 == 1) {
        printf("Unidade: %s, Valor: %d\n", unit2, value2);
    } else {
        printf("Erro: Unidade vazia, Valor = %d\n", value2);
    }

    // Testar com um token invÃ¡lido
    char token3[] = "INVALID";
    char unit3[20]; // Buffer para armazenar a unidade
    int value3;     // Inteiro para armazenar o valor

    // Chamar a funÃ§Ã£o `extract_data` para um token invÃ¡lido
    int res3 = extract_data(str, token3, unit3, &value3);

    // Mostrar o resultado para o token invÃ¡lido
    printf("Resultado (INVALID): %d\n", res3);
    if (res3 == 1) {
        printf("Unidade: %s, Valor: %d\n", unit3, value3);
    } else {
        printf("Erro: Unidade vazia, Valor = %d\n", value3);
    }

    return 0;
}
