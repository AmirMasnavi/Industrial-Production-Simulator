#include <stdio.h>
#include "asm.h"

int main() {
	
    int value;      
    int res;        


    char str1[] = "    89 ";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
    res = get_number(str1, &value);
    printf ( "%d: %d \n" ,res , value );
 

	char str2 [] = " 8 - -9 ";
	res = get_number ( str2 , &value ) ;
	printf ( "%d: %d \n" ,res , value );

    return 0;
}
