.section .text
.global move_n_to_array

move_n_to_array:
	
	#move_n_to_array(buffer, length, &tail, &head, n, array)
	
    movl (%rdx), %r10d # move o tail para o %r10
    movl (%rcx), %r11d # move o head para o %r11
    movq %r8, %r12 # move o n para o %r12
    movq %r9, %r13 # move o array para o %r13
    movl %esi, %r8d # move o length para %r8d
   
    cmpq $0, %r12 # verifica se o n é negativo ou nulo
    jle .fail
   
    
    movq $0, %r9 # contador
    

.move_elements:
	cmpq %r12, %r9 # verifica se o contador é menor que o n
	jge  .done
	
	cmpl %r11d, %r10d #Verifica quando é que o tail alcança o head para terminar
    je .done
	
	cmpl %r10d, %r8d # verifica se a tail alcançou o final do buffer
	je .reset_tail
    
    
    movl (%rdi, %r10, 4), %eax
    movl %eax, (%r13)
    addq $4, %r13
    addl $1, %r10d
    
    incq %r9
    
    jmp .move_elements
    
    
.reset_tail:
movl $0, %r10d
jmp .move_elements

.done:
    movl $1, %eax
    ret

.fail:
    movl $0, %eax
    ret
    
.section .note.GNU-stack

