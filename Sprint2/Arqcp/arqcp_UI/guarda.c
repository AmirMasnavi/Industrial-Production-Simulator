case 4: {
                // Testando format_command
                char op[] = "ADD";
                int n = 5;
                char cmd[20];
                if (format_command(op, n, cmd) == 0) {
                    printf("Comando formatado: %s\n", cmd);
                } else {
                    printf("Erro ao formatar comando.\n");
                }
                break;
            }

            case 5: {
                // Testando enqueue_value
                int buffer[5] = {0}, tail = 0, head = 0, length = 5;
                if (enqueue_value(buffer, length, &tail, &head, 42) == 0) {
                    printf("Valor 42 enfileirado com sucesso. Tail: %d, Head: %d\n", tail, head);
                } else {
                    printf("Erro ao enfileirar valor.\n");
                }
                break;
            }

            case 6: {
                // Testando dequeue_value
                int buffer[5] = {42, 0, 0, 0, 0}, tail = 0, head = 1, length = 5, value;
                if (dequeue_value(buffer, length, &tail, &head, &value) == 0) {
                    printf("Valor retirado da fila: %d. Tail: %d, Head: %d\n", value, tail, head);
                } else {
                    printf("Erro ao desenfileirar valor.\n");
                }
                break;
            }

            case 7: {
                // Testando get_n_element
                int buffer[5] = {1, 2, 3, 4, 5}, tail = 0, head = 5, length = 5, n = 2;
                int result = get_n_element(buffer, length, &tail, &head);
                printf("O %dº elemento na fila é: %d\n", n, result);
                break;
            }

            case 8: {
                // Testando move_n_to_array
                int buffer[5] = {10, 20, 30, 40, 50}, array[3];
                int tail = 0, head = 5, length = 5, n = 3;
                if (move_n_to_array(buffer, length, &tail, &head, n, array) == 0) {
                    printf("Valores movidos para o array: ");
                    for (int i = 0; i < n; i++) {
                        printf("%d ", array[i]);
                    }
                    printf("\n");
                } else {
                    printf("Erro ao mover valores.\n");
                }
                break;
            }

            case 9: {
                // Testando sort_array
                int vec[] = {5, 2, 9, 1, 3};
                int length = 5;
                char order = 'a'; // 'a' para ascendente, 'd' para descendente
                if (sort_array(vec, length, order) == 0) {
                    printf("Array ordenado: ");
                    for (int i = 0; i < length; i++) {
                        printf("%d ", vec[i]);
                    }
                    printf("\n");
                } else {
                    printf("Erro ao ordenar array.\n");
                }
                break;
            }

            case 10: {
                // Testando median
                int vec[] = {5, 2, 9, 1, 3};
                int length = 5, med;
                if (median(vec, length, &med) == 0) {
                    printf("Mediana do array: %d\n", med);
                } else {
                    printf("Erro ao calcular mediana.\n");
                }
                break;
            }
