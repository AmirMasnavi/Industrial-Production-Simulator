.section .text
    .global median

median:

	cmpq $0, %rsi
	je empty

    movq $0, %rcx
    movq $0, %r10

outer_loop:

    incq %rcx
    cmpq %rsi, %rcx
    je found_median
    decq %rcx
    movq %rcx, %r10

inner_loop:

    incq %r10
    cmpq %rsi, %r10
    je next_iteration

    movl (%rdi, %rcx, 4), %r8d
    movl (%rdi, %r10, 4), %r9d

    cmpl %r8d, %r9d
    jge inner_loop

    movl %r9d, (%rdi, %rcx, 4)
    movl %r8d, (%rdi, %r10, 4)
    
    jmp inner_loop

next_iteration:

    incq %rcx
    jmp outer_loop

found_median:

	pushq %rdx
	movq (%rdi), %r10
	
    movq $0, %r8          
    movq $0, %r9          
    movq $0, %r11     
    movq $0, %rax 
    movq $0, %rcx   
                    
    movq %rsi, %rax       
    movq $0, %rdx         
    movq $2, %rcx         
    cqo                   
    idivq %rcx            

    cmpl $0, %edx         
    je even_case

odd_case:

	movq %rax, %r11  
    movq (%rdi, %r11, 4), %r8
    
    popq %rdx
    movq %r8, (%rdx)
     
    jmp end_median

even_case:

    movq %rax, %r11       
    movq (%rdi, %r11, 4), %r8   
    decq %r11
    movq (%rdi, %r11, 4), %r9  
    
    addq %r8, %r9         
    movq $2, %rcx         
    movq %r9, %rax
    movq $0, %rdx         
    cqo                   
    idivq %rcx  
             
	movq %rax, %r12
	
    popq %rdx
    movq %r12, (%rdx)
    
end_median:

	movq %r10, (%rdi)
    movl $1, %eax       
    ret

empty:
    movl $0, %eax         
    ret

.section .note.GNU-stack 
