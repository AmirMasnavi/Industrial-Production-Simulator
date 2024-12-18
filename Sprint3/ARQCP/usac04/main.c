#include <stdio.h>
#include "asm.h"

int main(void){
	int value = 26;
	char str [] = " oN ";
	char cmd [20];
	
	int res = format_command ( str , value , cmd );
	printf ( "%d: %s \n", res , cmd ); // 1: ON ,1 ,1 ,0 ,1 ,0
	
	char str2 [] = " aaa ";
	
	int res2 = format_command ( str2 , value , cmd );
	printf ( "%d: %s \n", res2 , cmd ); // 0:
	
	return 0;
}

