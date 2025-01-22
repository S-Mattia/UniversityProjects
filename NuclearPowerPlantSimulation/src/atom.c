#include "../include/atom.h"

/*IPC id variables*/
int shm_id, sem_id, msgq_id;

/*shared memory's struct*/
shared_stats *shm_pointer;

/*configuration's struct*/
configuration config;

/*struct of a set of signal used to save the old mask when we change it*/
sigset_t old_mask;

/*int variable that indicate the atomic number of an atom*/
int atomic_number;

/*flag that indicate if the splitting command is delivered from the inhibitor or the activator*/
int splitting_method;

/*declaring the main variable to insert into the array of arguments*/
int child_atomic_number;
char atomic_number_string[10], child_atomic_number_string[10];
char shm_id_string[10], sem_id_string[10], msgq_id_string[10];

int main(int argc, char *argv[])
{
    /*Checking if we have all the required arguments*/
    if (argc < 5)
    {
        perror("Missing agruments to execve call of the atom");
        exit(EXIT_FAILURE);
    }

    /*setting the handler for the main signal we want to use*/
    set_handler(SIGUSR1, sig_handler, 0);
    set_handler(SIGUSR2, sig_handler, 0);
    set_handler(SIGTERM, sig_handler, 0);

    /*fetching the atomic number of the atom and the id of the ipc structures*/
    atomic_number = atoi(argv[1]);
    sem_id = atoi(argv[3]);
    shm_id = atoi(argv[2]);
    msgq_id = atoi(argv[4]);

    /*function to fetch the configuration's values*/
    if (ini_parse("./config/config.ini", handler, &config) < 0)
    {
        perror("Can't load 'config.ini'\n");
        exit(EXIT_FAILURE);
    }

    /*attaching the shard mameory and checking for some errors*/
    shm_pointer = (shared_stats *)(shmat(shm_id, NULL, 0));
    if (shm_pointer == (void *)-1)
    {
        perror("an error occurred attaching the shared memory");
        raise(SIGINT); // Clean exit on error
    }

    /*writing onto the logfile*/
    log_message("Atom Process was created", getpid(), sem_id);

    /*check the atomic number, if it's less than N_ATOM_MIN turn into a waste*/
    if (atomic_number <= config.n_atom_min)
    {
        /*incrementing the number of wastes in the last second, when the semaphore it's free than exit*/
        semaphore_operation(sem_id, WASTE_SEM, -1);
        shm_pointer->waste_last_second += 1;
        semaphore_operation(sem_id, WASTE_SEM, 1);
        exit(1);
    }
    else
    {
        /*sending the pid to  the message queue*/
        send_msg(msgq_id);
    }

    /*Splitting with different method */
    while (1)
    {
        pause();
        switch (splitting_method)
        {
        case 0: /*Activator splitting SIGURS1*/
            atom_splitting(0);
            break;
        case 1: /*Inhibitor splitting SIGURS2*/
            /*combinate the time and the pid to reach a unique seed to initialize the rand function then calculate the random number*/
            old_mask = block_signals(1, SIGTERM); /*protecting critical section of code*/

            srand(time(NULL) ^ getpid());
            float random_number = (((float)(rand() % 100)) / 100.0);
            
            reset_signals(old_mask); /*end of the critical section*/

            /*check if the random number is bigger of INHIBITOR_PROBABILITY_WASTE*/
            if (random_number >= config.inhibitor_probability_waste)
            {
                /*spliting and absorbing INHIBITOR_PERCENT_ABSORBING*/
                atom_splitting(config.inhibitor_percent_absorbing);
            }
            else
            {
                old_mask = block_signals(1, SIGTERM); /*protecting critical section of code*/

                /*incrementing the number of waste in the last second, when the smephore is free*/
                semaphore_operation(sem_id, WASTE_SEM, -1);
                shm_pointer->waste_last_second += 1;
                semaphore_operation(sem_id, WASTE_SEM, 1);

                /*writing the action into the logfile*/
                log_message("Atom Process was turned into a waste by the Inhibitor", getpid(), sem_id);

                reset_signals(old_mask); /*end of the critical section*/

                exit(1);
            }
            break;

        }
    }
}

/*signal handler of the process*/
void sig_handler(int signum)
{
    switch (signum)
    {
    case SIGUSR1: /*signal from the activator process*/
        splitting_method = 0;
        break;
    case SIGUSR2: /*signal from the inhibitor process*/
        splitting_method = 1;
        break;
    case SIGTERM:
        shmdt(shm_pointer); /*deattach the shared memory*/
        exit(1);
        break;
    }
}

/*
function to execute the splitting action;
-percent_absorbing: the value that could be absobed
from the inhibitor during the splitting
*/
void atom_splitting(float percent_absorbing)
{
    old_mask = block_signals(1,SIGTERM);  /*protecting critical section of code*/

    srand(getpid());

    /*calculating the child atomic number that is a number between 1 and the atom's atomic number*/
    child_atomic_number = (rand() % (atomic_number - 1)) + 1;
    atomic_number -= child_atomic_number;

    /*initializing the main variable to insert into the array of arguments*/
    sprintf(atomic_number_string, "%i", atomic_number);
    sprintf(child_atomic_number_string, "%i", child_atomic_number);
    sprintf(shm_id_string, "%i", shm_id);
    sprintf(sem_id_string, "%i", sem_id);
    sprintf(msgq_id_string, "%i", msgq_id);

    reset_signals(old_mask); /*end of the critical section*/

    /*creating the arrays of arguments to pass into the execve calls*/
    char *child_atom_argv[] = {"./bin/atom", child_atomic_number_string, shm_id_string, sem_id_string, msgq_id_string, NULL};
    char *atom_argv[] = {"./bin/atom", atomic_number_string, shm_id_string, sem_id_string, msgq_id_string, NULL};

    /*splitting by the fork*/
    pid_t pid = fork();
    if (pid == 0)
    {
        /*child process
        execve call to create the new atom genereted by the splitting*/
        execve("./bin/atom", child_atom_argv, NULL);

        /*checking for some errors of the execve*/
        perror("execve atom in atom");
        kill(shm_pointer->master_pid, SIGINT);
    }
    else if (pid > 0)
    {
        old_mask = block_signals(1,SIGTERM);  /*protecting critical section of code*/
        /*atom process
        incrementing the number of splitting in the last second, when the semaphore is free*/
        semaphore_operation(sem_id, SPLITTING_SEM, -1);
        shm_pointer->splitting_last_second += 1;
        semaphore_operation(sem_id, SPLITTING_SEM, 1);

        /*calculating the value of the released energy*/
        float energy_created = energy(child_atomic_number, atomic_number);

        /*calculating the value of the absorbed energy (0 if percent_absorbing is 0)*/
        float energy_absorbed = energy_created * percent_absorbing;
        energy_created -= energy_absorbed;

        /*incrementing the energy produced in the last second, when the sempahore is free*/
        semaphore_operation(sem_id, ENERGY_SEM, -1);
        shm_pointer->energy_produced_last_second += energy_created;
        semaphore_operation(sem_id, ENERGY_SEM, 1);

        /*incrementing the energy absorbed in the last second, when the sempahore is free*/
        semaphore_operation(sem_id, ABSORBING_SEM, -1);
        shm_pointer->absorbed_energy_last_second += energy_absorbed;
        semaphore_operation(sem_id, ABSORBING_SEM, 1);

        reset_signals(old_mask); /*end of the critical section*/

        if (percent_absorbing == 0)
        {
            log_message("Atom Process splitted", getpid(), sem_id);
        }
        else
        {
            log_message("Atom Process splitted with an absorption of energy by the Inhibitor", getpid(), sem_id);
        }

        /*execve call to recreate the atom that splitted*/
        execve("./bin/atom", atom_argv, NULL);

        /*checking for some errors of the execve*/
        perror("execve atom in atom");
        kill(shm_pointer->master_pid, SIGINT);
    }
    else
    {
        /*Meltdown due to fork error*/
        kill(shm_pointer->master_pid, SIGUSR1);
        exit(EXIT_FAILURE);
    }
}

/*function to calculate the energy relased from the splitting process*/
int energy(int a, int b)
{
    int max = 0;
    if (a < b)
    {
        max = b;
    }
    else
    {
        max = a;
    }
    return ((a * b) - max);
}