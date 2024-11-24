#include <stdio.h>
#include "asm.h"

int main() {
    int vec[] = {5, 3, 8, 1, 2};
    int length = 5;
    char order = 1;  // 1 for ascending, 0 for descending
    

    int result = sort_array(vec, length, order);

    if (result == 1) {
        printf("Array sorted successfully:\n");
        for (int i = 0; i < length; i++) {
            printf("%d ", vec[i]);
 }
        printf("\n");
    } else {
        printf("Failed to sort the array.\n");
    }

    return 0;
}
