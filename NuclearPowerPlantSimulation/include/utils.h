#ifndef UTILS_H
#define UTILS_H

/*LIBRARIES*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <unistd.h>
#include <time.h>
#include <errno.h>
#include <stdarg.h>

/*IPC and signal libraires*/
#include <signal.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>

/*external libraries*/
#include "../library/ini.h"

/*MACRO*/

#define _GNU_SOURCE

/*constants used to reconize the semaphores*/
#define START_SEM 0
#define ATOMS_SEM 1
#define SPLITTING_SEM 2
#define WASTE_SEM 3
#define ACTIVATION_SEM 4
#define ABSORBING_SEM 5
#define ENERGY_SEM 6
#define MSGQ_SEM 7
#define LOG_SEM 8

/*constants used to reconize the type
of message that contain the pid of an atom process*/
#define MSG_TYPE_PID 1

/*MAIN STRUCTURES USED IN THE PROJECT*/

/*Declaration of the struct used as a message in the queue*/
typedef struct msg_buffer
{
    long type;
    char text[10];
} msg_buffer;

/*Declaration of struct shared_stats used to manage the shared memory*/
typedef struct shared_stats
{
    int master_pid; /*Pid of the master process*/

    int inhibitor_mode; /*Pid of the master process*/

    int total_atoms_inserted;       /*Total number of the inserted atoms*/
    int atoms_inserted_last_second; /*Atoms inserted in the last second*/

    int total_splitting;       /*Total number of splitting*/
    int splitting_last_second; /*Splitting in the last second*/

    int total_waste;       /*Total number of waste*/
    int waste_last_second; /*Waste of the last second*/

    int total_activations;       /*Total number of activation*/
    int activations_last_second; /*Activation in the last second*/

    float total_produced_energy;       /*Total energy produced*/
    float energy_produced_last_second; /*Enenrgy produced in the last second*/

    float total_consumed_energy;       /*Total energy consumed*/
    float consumed_energy_last_second; /*Energy consumed in the last second*/

    float total_absorbed_energy;       /*Total energy absorbed from the inhibitor*/
    float absorbed_energy_last_second; /*Energy absorbed in the last second from the inhibitor*/
} shared_stats;

/*struct that contains all the variables of configuration*/
typedef struct configuration
{

    int n_atoms_init;
    int n_new_atoms;
    int n_atom_max;
    int n_atom_min;
    int energy_demand;
    int energy_explode_threshold;
    int simulation_duration;

    int feeder_step;

    int activator_step;

    int in_mode;
    int inhibitor_step;
    float inhibitor_percent_absorbing;
    float inhibitor_probability_waste;

} configuration;

/*DECLARATION OF THE UTILS FUNCTION*/

int semaphore_operation(int sem_id, int sem_num, int n);

void log_clear();
void log_message(const char *message, pid_t pid, int sem_id);

int receive_msg(int msg_id);
int send_msg(int msgq_id);

int nanosleep_custom(struct timespec *req);

sigset_t block_signals(int num, ...);
sigset_t unblock_signals(int num, ...);
void reset_signals(sigset_t mask);

void set_handler(int signum, void (*func)(int), int flag);

int handler(void *user, const char *section, const char *name, const char *value);

#endif /* UTILS_H */