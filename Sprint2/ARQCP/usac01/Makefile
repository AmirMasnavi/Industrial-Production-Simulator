main: us01.o asm.o
	gcc us01.o asm.o -z noexecstack -o main
	
us02.o: us01.c 
	gcc -g -Wall -Wextra -fanalyzer -c us01.c -o us01.o
	
asm.o: asm.s
	gcc -g -Wall -Wextra -fanalyzer -c asm.s -o asm.o

run: main
	./main
	
clean:
	rm -f *.o main
