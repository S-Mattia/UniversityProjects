#ifndef MASTER_H
#define MASTER_H

#include "../include/utils.h"

void simulation_terminator(char *Cause);
void sem_setting();
void check_update_stats();
void print_stats();
void sig_handler(int signum);
void allocate_ipc();
void deallocate_ipc();

#endif /* MASTER_H */
