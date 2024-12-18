.section .text
    .global sort_array

sort_array:
    cmpq $0, %rsi          # Check if array size (n) is 0 or negative
    jle .fail               # If size <= 0, jump to fail

    movq $0, %rcx          # Initialize outer loop index i to 0

.outer_loop:
    cmpq %rsi, %rcx        # Compare i with array size (n)
    je .end_sort           # If i >= n, jump to end of sorting

    movq %rcx, %r8         # Set j = i for the inner loop

.inner_loop:
    incq %r8               # Increment j
    cmpq %rsi, %r8         # Compare j with array size (n)
    je .next_iteration     # If j >= n, jump to next outer loop iteration

    movl (%rdi, %rcx, 4), %r9d  # Load array[i] into %r9d
    movl (%rdi, %r8, 4), %r10d # Load array[j] into %r10d

    cmpb $1, %dl           # Check if sorting order is ascending (dl == 1)
    je .ascending_order    # If ascending order, jump to ascending comparison
    jmp .descending_order  # Else, jump to descending comparison

.next_iteration:
    incq %rcx              # Increment outer loop index i
    jmp .outer_loop         # Jump back to outer loop start

.ascending_order:
    cmpl %r10d, %r9d       # Compare array[j] with array[i]
    jle .skip_swap         # If array[j] <= array[i], skip swap
    jmp .swap              # Else, perform swap

.descending_order:
    cmpl %r10d, %r9d       # Compare array[j] with array[i]
    jge .skip_swap         # If array[j] >= array[i], skip swap

.swap:
    movl %r10d, (%rdi, %rcx, 4)  # Swap array[i] with array[j]
    movl %r9d, (%rdi, %r8, 4)   # Swap array[j] with array[i]

.skip_swap:
    jmp .inner_loop         # Continue with next inner loop iteration

.end_sort:
    movl $1, %eax          # Return 1 to indicate successful sort
    ret

.fail:
    movl $0, %eax          # Return 0 to indicate failure (invalid input)
    ret

.section .note.GNU-stack
