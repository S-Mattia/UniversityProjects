#include "../include/activator.h"

/*IPC id variables*/
int shm_id, sem_id, msgq_id;

/*shared memory's struct*/
shared_stats *shm_pointer;

/*configuration's struct*/
configuration config;

/*struct of a set of signal used to save the old mask when we change it*/
sigset_t old_mask;

/*struct to specify the interval of time to wait (nanosleep)*/
struct timespec req;

int main(int argc, char *argv[])
{
    /*Checking if we have all the required arguments*/
    if (argc < 4)
    {

        perror("Missing agruments to execve call of the activator");
        exit(EXIT_FAILURE);
    }

    /*setting the handler of the singnal*/
    set_handler(SIGTERM, sig_handler, 0);

    /*fetching the id of the ipc structures*/
    shm_id = atoi(argv[1]);
    sem_id = atoi(argv[2]);
    msgq_id = atoi(argv[3]);

    /*fetching the configuration's values and setting config*/
    if (ini_parse("./config/config.ini", handler, &config) < 0)
    {
        perror("Can't load 'config.ini'\n");
        exit(EXIT_FAILURE);
    }

    /*setting the number of nanosecond with the value taken from the configuration struct*/
    req.tv_nsec = config.activator_step;

    /*attaching the shard mameory and checking for errors*/
    shm_pointer = (shared_stats *)(shmat(shm_id, NULL, 0));
    if (shm_pointer == (void *)-1)
    {
        perror("an error occurred attaching the shared memory");
        raise(SIGINT); // Clean exit on error
    }

    /*writing onto the logfile*/
    log_message("Activator Process was created...", getpid(), sem_id);

    while (1)
    {
        old_mask = block_signals(1, SIGTERM); /*protecting critical section of code*/

        /*seeking the pid of an atom from the msg queue, when the semaphore it's free*/
        semaphore_operation(sem_id, MSGQ_SEM, -1);
        pid_t atom_pid;
        do
        {
            atom_pid = receive_msg(msgq_id);
        } while (atom_pid == -1);
        semaphore_operation(sem_id, MSGQ_SEM, 1);

        reset_signals(old_mask); /*end of the critical section*/

        /*wrinting the action onto the log file*/
        char message[50];
        sprintf(message, "Activator Process activated an atom with pid: %i", atom_pid);
        log_message(message, getpid(), sem_id);

        /*sending splitting command to the atom found*/
        kill(atom_pid, SIGUSR1);

        old_mask = block_signals(1, SIGTERM); /*protecting critical section of code*/

        /*incrementing the number of activation in the last second, when the semaphore is free*/
        semaphore_operation(sem_id, ACTIVATION_SEM, -1);
        shm_pointer->activations_last_second += 1;
        semaphore_operation(sem_id, ACTIVATION_SEM, 1);

        reset_signals(old_mask); /*end of the critical section*/

        /*waiting ACTIVATOR_STEP seconds*/
        nanosleep_custom(&req);
    }
}

/*signal handler of the process*/
void sig_handler(int signum)
{
    switch (signum)
    {
    case SIGTERM:
        shmdt(shm_pointer); /*deattach the shared memory*/
        exit(1);
        break;
    }
}
