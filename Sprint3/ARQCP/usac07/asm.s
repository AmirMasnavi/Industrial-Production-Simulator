.section .text

.global get_n_element

get_n_element:
    # Arguments:
    #   %rdi: Pointer to buffer (not used)
    #   %rsi: Length of the buffer
    #   %rdx: Pointer to tail
    #   %rcx: Pointer to head

    # Load values of head and tail
    movl (%rcx), %r8d     # Load *head into %r8d
    movl (%rdx), %r9d     # Load *tail into %r9d
    movl %esi, %r10d      # Load length into %r10d

    # Compare head and tail
    cmpl %r9d, %r8d
    jge head_greater_or_equal

    # Case: tail > head (wrap-around)
    addl %r10d, %r8d      # Add length to head
    subl %r9d, %r8d       # Subtract tail
    movl %r8d, %eax       # Store result in %eax
    ret

head_greater_or_equal:
    # Case: head >= tail
    subl %r9d, %r8d       # head - tail
    movl %r8d, %eax       # Store result in %eax
    ret

