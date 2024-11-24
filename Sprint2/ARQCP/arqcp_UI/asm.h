#ifndef ASM_H
#define ASM_H

extern int extract_data(char* str, char* token, char* unit, int* value);
extern int get_number_binary(int n, char* bits);
extern int get_number(char* str, int* n);
extern int format_command(char* op, int n, char* cmd);
extern int enqueue_value(int* buffer, int length, int* tail, int* head, int value);
extern int dequeue_value(int* buffer, int length, int* tail, int* head, int* value);
extern int get_n_element(int* buffer, int length, int* tail, int* head);
extern int move_n_to_array(int* buffer, int length, int* tail, int* head, int n, int* array);
extern int sort_array(int* vec, int length, char order);
extern int median(int* vec, int length, int* me);

#endif
