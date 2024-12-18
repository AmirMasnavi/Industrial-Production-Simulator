#include <stdio.h>
#include "asm.h"

void menu() {
    printf("\n=== MENU ===\n");
    printf("1. US01 - Extract Data\n");
    printf("2. US02 - Get Number Binary\n");
    printf("3. US03 - Get Number\n");
    printf("4. US04 - Format Command\n");
    printf("5. US05 - Enqueue Value in Circular Buffer\n");
    printf("6. US06 - Dequeue Value from Circular Buffer\n");
    printf("7. US07 - Get Number of Elements in Buffer\n");
    printf("8. US08 - Move N Elements to Array\n");
    printf("9. US09 - Sort Array\n");
    printf("10. US10 - Calculate Median\n");
    printf("0. Exit\n");
    printf("\nSelect an option: ");
}

void print_buffer(int* buffer, int length, int head, int tail) {
    printf("\nBuffer: [");
    for (int i = 0; i < length; i++) {
        printf("%d", buffer[i]);
        if (i < length - 1) printf(", ");
    }
    printf("]\n");
    printf("Head: %d | Tail: %d\n", head, tail);
}

int main() {
    int option;

    do {
        menu();
        scanf("%d", &option);
        printf("\n");

        switch (option) {
            case 1: {
                char str[] = "TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80";
                char token1[] = "TEMP";
                char token2[] = "AAA";
                char unit[20];
                int value, res;

                printf("Input string: %s\n", str);
                printf("Token 1: %s\n", token1);
                printf("Token 2: %s\n", token2);

                res = extract_data(str, token1, unit, &value);
                printf("%d:%s,%d\n", res, unit, value);

                res = extract_data(str, token2, unit, &value);
                printf("%d:%s,%d\n", res, unit, value);

                break;
            }

            case 2: {
                int value = 26;
                char bits[5];

                printf("Input Value: %d\n\n", value);

                int res = get_number_binary(value, bits);
                printf("Result: %d\nBits: %d, %d, %d, %d, %d\n\n", res, bits[4], bits[3], bits[2], bits[1], bits[0]);
                break;
            }

            case 3: {
                char str1[] = "    89 ";
                char str2[] = " 8 - -9 ";

                int value, res;

                printf("Input String 1: '%s'\n", str1);
                res = get_number(str1, &value);
                printf("Result: %d | Value: %d\n\n", res, value);

                printf("Input String 2: '%s'\n", str2);
                res = get_number(str2, &value);
                printf("Result: %d | Value: %d\n\n", res, value);
                break;
            }

            case 4: {
                int value = 26;
                char str[] = " oN ";
                char str2[] = " aaa ";
                char cmd[20];

                printf("Input String 1: '%s', Value: %d\n", str, value);
                int res = format_command(str, value, cmd);
                printf("Result: %d | Command: %s\n\n", res, cmd);

                printf("Input String 2: '%s', Value: %d\n", str2, value);
                int res2 = format_command(str2, value, cmd);
                printf("Result: %d | Command: %s\n\n", res2, cmd);

                break;
            }

            case 5: {
                int buffer[4] = {0, 0, 0, 0};
                int length = 4;
                int head = 0;
                int tail = 3;
                int value = 5;

                printf("Initial Buffer: {0, 0, 0, 0}\nLength: %d | Head: %d | Tail: %d | Value: %d\n\n", length, head, tail, value);

                int is_full = enqueue_value(buffer, length, &tail, &head, value);
                print_buffer(buffer, length, head, tail);
                printf("Is buffer full after insertion? %s\n\n", is_full ? "Yes" : "No");

                break;
            }

            case 6: {
                int buffer[5] = {1, 2, 3, 4, 5};
                int length = 5;
                int tail = 3;
                int head = 1;
                int value;

                printf("Initial Buffer: {1, 2, 3, 4, 5}\nLength: %d | Head: %d | Tail: %d\n\n", length, head, tail);

                int result = dequeue_value(buffer, length, &tail, &head, &value);
                if (result == 1) {
                    printf("Dequeued Value: %d\nUpdated Head: %d | Updated Tail: %d\n\n", value, head, tail);
                } else {
                    printf("Buffer is empty, cannot dequeue.\n\n");
                }

                break;
            }

            case 7: {
                int buffer[10];
                int length = 10;
                int tail = 3;
                int head = 7;

                printf("Buffer Length: %d | Head: %d | Tail: %d\n\n", length, head, tail);

                int n_elements = get_n_element(buffer, length, &tail, &head);
                printf("Number of elements in the buffer: %d\n\n", n_elements);

                break;
            }

            case 8: {
                int buffer[4] = {1, 2, 3, 4};
                int length = 4;
                int tail = 3;
                int head = 2;
                int n = 3;
                int array[n];

                printf("Buffer: {1, 2, 3, 4}\nLength: %d | Head: %d | Tail: %d | n: %d\n\n", length, head, tail, n);

                int result = move_n_to_array(buffer, length, &tail, &head, n, array);
                if (result == 0) {
                    printf("Error: n cannot be 0 or less!\n\n");
                }

                if (result == 1) {
                    printf("Result: %d\nArray: ", result);
                    for (int i = 0; i < n; i++) {
                        printf("%d ", array[i]);
                    }
                    printf("\n\n");
                }

                break;
            }

            case 9: {
                int vec[] = {5, 3, 8, 1, 2};
                int length = 5;
                char order = 1;

                printf("Input Array: {5, 3, 8, 1, 2}\nLength: %d | Order: %d\n\n", length, order);

                int result = sort_array(vec, length, order);
                if (result == 1) {
                    printf("Sorted Array: ");
                    for (int i = 0; i < length; i++) {
                        printf("%d ", vec[i]);
                    }
                    printf("\n\n");
                } else {
                    printf("Failed to sort the array.\n\n");
                }

                break;
            }

            case 10: {
                int vec[] = {-1, -3, -1, -2};
                int length = 4;
                int me;

                printf("Input Array: {-1, -3, -1, -2}\nLength: %d\n\n", length);

                int result = median(vec, length, &me);
                if (result) {
                    printf("Sorted Array: ");
                    for (int i = 0; i < length; i++) {
                        printf("%d ", vec[i]);
                    }
                    printf("\nMedian: %d\n\n", me);
                } else {
                    printf("Error: Invalid length.\n\n");
                }

                break;
            }

            case 0:
                printf("Exiting...\n\n");
                break;

            default:
                printf("Invalid option! Please try again.\n\n");
        }
    } while (option != 0);

    return 0;
}
