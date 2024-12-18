#include <stdio.h>
#include "asm.h"

int main() {
    int buffer[4] = {1,2,3,4};
    int length = 4;
    int tail = 3;
    int head = 2;
    int n = 3;
    int array[n];

    int result = move_n_to_array(buffer, length, &tail, &head, n, array); 
    
    
    if(result == 0){
		printf("Oh no! n cant be 0 or less!\n");
		}
		
	if(result == 1){
		printf("Result: %d\n", result);
		printf("Array: ");
		for(int i = 0; i< sizeof(array)/sizeof(array[0]); i++){
			printf("%d ", array[i]);
			}
			printf("\n");
    
    
    //printf("New tail: %d\n", tail);
	}
    return 0;
}
