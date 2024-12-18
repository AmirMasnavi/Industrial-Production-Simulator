#include <stdio.h>
#include "asm.h"

int main() {
	    int buffer[10];
	    int length = 10;             // Buffer length
    int tail = 3;                // Tail position
    int head = 7;                // Head position

    // Call assembly function
    int n_elements = get_n_element(buffer, length, &tail, &head);

    // Print the result
    printf("Number of elements in the buffer: %d\n", n_elements);

    return 0;

}
