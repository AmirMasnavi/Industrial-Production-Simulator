#include <stdio.h>
#include "asm.h"

int main() {
    int buffer[5] = {1, 2, 3, 4, 5}; // Example circular buffer
    int length = 5;                 // Buffer length
    int tail = 3;                   // Tail starts at index 3
    int head = 1;                   // Head starts at index 1
    int value;                      // Variable to store dequeued value

    // Dequeue an element
    int result = dequeue_value(buffer, length, &tail, &head, &value);

    if (result == 1) {
        printf("Dequeued value: %d\n", value);
        printf("Updated head: %d, Updated tail: %d\n", head, tail);
    } else {
        printf("Buffer is empty, cannot dequeue.\n");
    }

    return 0;
}
