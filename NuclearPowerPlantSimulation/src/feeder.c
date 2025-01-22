#include "../include/feeder.h"

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
        perror("Missing agruments to execve call of the feeder");
        exit(EXIT_FAILURE);
    }

    /*setting the handler of the signal we want to use*/
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
    req.tv_nsec = config.feeder_step;

    /*attaching the shard mameory*/
    shm_pointer = (shared_stats *)(shmat(shm_id, NULL, 0));
    if (shm_pointer == (void *)-1)
    {
        perror("an error occurred attaching the shared memory");
        raise(SIGINT); // Clean exit on error
    }

    /*writing on the logfile*/
    log_message("Feeder Process was created...", getpid(), sem_id);

    /*declaring and setting the main variable to insert into the array of arguments of execve functions*/
    char shm_id_string[10], sem_id_string[10], msgq_id_string[10];
    sprintf(shm_id_string, "%i", shm_id);
    sprintf(sem_id_string, "%i", sem_id);
    sprintf(msgq_id_string, "%i", msgq_id);

    /*creating N_NEW_ATOMS every STEP_FEEDER nanosecond*/
    while (1)
    {

        for (int i = 0; i < config.n_new_atoms; i++)
        {
            pid_t atom_pid = fork();
            if (atom_pid == 0)
            {
                /*child process
                writing the insertion action into the logfile*/
                char message[50];
                sprintf(message, "Feeder Process inserted an atom with pid: %i", getpid());
                log_message(message, getpid(), sem_id);

                /*protecting critical section of code*/
                old_mask = block_signals(1, SIGTERM);

                /*initializing the rand function with the pid as a seed*/
                srand(getpid());

                /*calculating a random number between 0 and N_ATOM_MAX*/
                int atomic_number = (rand() % config.n_atom_max) + 1;

                /*creating the array of arguments to pass into the execve call*/
                char atomic_number_string[10];
                sprintf(atomic_number_string, "%i", atomic_number);
                char *atom_argv[] = {"./bin/atom", atomic_number_string, shm_id_string, sem_id_string, msgq_id_string, NULL};

                reset_signals(old_mask);

                /*execve call to create a new atom*/
                execve("./bin/atom", atom_argv, NULL);

                /*checking for some errors of the execve*/
                perror("execve atom in feeder");
                kill(shm_pointer->master_pid, SIGINT);
            }
            else if (atom_pid < 0)
            {
                /*meltdown due to fork error*/
                kill(shm_pointer->master_pid, SIGUSR1);
                exit(EXIT_FAILURE);
            }
        }

        /*protecting critical section of code*/
        old_mask = block_signals(1, SIGTERM);

        /*incrementing the number of atoms inserted in the last second, when the semaphore is free*/
        semaphore_operation(sem_id, ATOMS_SEM, -1);
        shm_pointer->atoms_inserted_last_second += config.n_new_atoms;
        semaphore_operation(sem_id, ATOMS_SEM, 1);

        reset_signals(old_mask);

        /*waiting FEEDER_STEP nanoseconds*/
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