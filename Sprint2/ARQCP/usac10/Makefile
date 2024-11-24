prog: asm.o main.o
	gcc asm.o main.o -o prog

asm.o: asm.s asm.h
	gcc -c asm.s -o asm.o

main.o: main.c asm.h
	gcc -c main.c -o main.o

clean:
	rm -f *.o prog
	
debug: prog
	gdb ./prog 

run: prog
	./prog
