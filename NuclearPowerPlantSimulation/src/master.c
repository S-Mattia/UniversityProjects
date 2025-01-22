#include "../include/master.h"
#include <sys/wait.h>

/*IPC id variables*/
int shm_id = -1;
int sem_id = -1;
int msgq_id = -1;

/*shared memory's struct*/
shared_stats *shm_pointer;

/*configuration's struct*/
configuration config;

/*number of times that the process recived the SIALRM signal*/
int calls = 0;

/*declaring a struct where is going to be saved the old sigset mask of the process*/
sigset_t old_mask;

int main()
{
    /*block all the signal to protect the setup phase and blocking SIGTSTP permanently*/
    old_mask = block_signals(2, SIGTSTP, SIGINT);

    /*setting the handler for the main signal we want to use*/
    set_handler(SIGINT, sig_handler, 0);
    set_handler(SIGUSR1, sig_handler, 0);
    set_handler(SIGALRM, sig_handler, 0);
    set_handler(SIGTERM, sig_handler, 0);

    allocate_ipc();

    log_clear();

    printf("Master Process started...\n");

    /*writing the correct value of master_pid into the shared memory*/
    shm_pointer->master_pid = getpid();

    /*fetching the configuration's values and setting config*/
    if (ini_parse("./config/config.ini", handler, &config) < 0)
    {
        perror("Can't load 'config.ini'\n");
        exit(EXIT_FAILURE);
    }

    /*Creating String variable of the id to use as arguments*/
    char shm_id_string[10], sem_id_string[10], msgq_id_string[10];
    sprintf(shm_id_string, "%i", shm_id);
    sprintf(sem_id_string, "%i", sem_id);
    sprintf(msgq_id_string, "%i", msgq_id);

    /*Creating all the child process (Activator,feeder,inhibitor, N_ATOM_INIT atoms)
    Creating the array of arguments to pass into the execve calls for activator process*/
    char *activator_argv[] = {"./bin/activator", shm_id_string, sem_id_string, msgq_id_string, NULL};
    /*fork to create the child process*/
    pid_t activator_pid = fork();
    if (activator_pid == 0)
    {
        /*child process
        waiting the start of the simulation*/
        semaphore_operation(sem_id, START_SEM, -1);

        /*execve call to create the activator process*/
        execve("./bin/activator", activator_argv, NULL);

        /*checking for some errors of the execve*/
        perror("execve activator in master");
        raise(SIGINT);
    }
    else if (activator_pid < 0)
    {
        /*Meltdown due to fork error*/
        raise(SIGUSR1);
    }

    /*Creating the array of arguments to pass into the execve calls for feeder process*/
    char *feeder_argv[] = {"./bin/feeder", shm_id_string, sem_id_string, msgq_id_string, NULL};

    pid_t feeder_pid = fork();
    if (feeder_pid == 0)
    {
        /*child process*/
        semaphore_operation(sem_id, START_SEM, -1);

        /*execve call to create the feeder process*/
        execve("./bin/feeder", feeder_argv, NULL);

        perror("execve feeder in master");
        raise(SIGINT);
    }
    else if (feeder_pid < 0)
    {
        raise(SIGUSR1);
    }

    /*Creating the array of arguments to pass into the execve calls for inhibitor process*/
    char *inhibitor_argv[] = {"./bin/inhibitor", shm_id_string, sem_id_string, msgq_id_string, NULL};

    pid_t inhibitor_pid = fork();
    if (inhibitor_pid == 0)
    {
        /*child process*/
        semaphore_operation(sem_id, START_SEM, -1); /*waiting the start*/

        /*execve call to create the inhibitor process*/
        execve("./bin/inhibitor", inhibitor_argv, NULL);

        perror("execve inhibitor in master");
        raise(SIGINT);
    }
    else if (inhibitor_pid < 0)
    {
        raise(SIGUSR1);
    }

    /*Creating N_ATOM_INIT atoms to insert into the simuation*/
    log_message("Creating N_ATOM_INIT atoms...", getpid(), sem_id);
    for (int i = 0; i < config.n_atoms_init; i++)
    {
        pid_t atom_pid = fork();
        if (atom_pid == 0)
        {
            /*child process
            waiting the start*/
            semaphore_operation(sem_id, START_SEM, -1);

            /*initializing the rand function with the pid as a seed*/
            srand(getpid());

            /*calculating a random number between 0 and N_ATOM_MAX*/
            int atomic_number = (rand() % config.n_atom_max) + 1;

            /*creating the array of arguments to pass into the execve call*/
            char atomic_number_string[10];
            sprintf(atomic_number_string, "%i", atomic_number);
            char *atom_argv[] = {"./bin/atom", atomic_number_string, shm_id_string, sem_id_string, msgq_id_string, NULL};

            /*execve call to create a new atom*/
            execve("./bin/atom", atom_argv, NULL);

            perror("execve atom in master");
            raise(SIGINT); /*TERMINATION SIMULATION*/
        }
        else if (atom_pid < 0)
        {
            raise(SIGUSR1);
        }
    }

    /*Incrementing the number of atoms inserted in the last second, when the semaphore is free*/
    semaphore_operation(sem_id, ATOMS_SEM, -1);
    shm_pointer->atoms_inserted_last_second += config.n_atoms_init;
    semaphore_operation(sem_id, ATOMS_SEM, 1);

    /*initialzing the status of the inhibitor to the value that tha user write into the config file*/
    shm_pointer->inhibitor_mode = config.in_mode;

    log_message("Simulation Start...", getpid(), sem_id);
    printf("Simulation Start...\n");

    /*giving the start to all the child processes*/
    semctl(sem_id, START_SEM, SETVAL, config.n_atoms_init + 3);

    /*unblocking ctrl + c command to stop the simulation*/
    unblock_signals(1, SIGINT);

    /*sending an alarm in a second*/
    alarm(1);
    while (1)
    {
        /*waiting for a signal*/
        pause();
    }
}

/*signal handler of the process*/
void sig_handler(int signum)
{

    switch (signum)
    {
    case SIGALRM: /*update and printing stat - simulation termination after SIM_DURATION calls*/
        if (calls < config.simulation_duration)
        {
            /*simulation still not terminated*/
            calls += 1;
            print_stats();
            check_update_stats();
            alarm(1); /*alarm every second*/
        }
        else
        {
            /*simulation terminated due to time's up*/
            check_update_stats();
            print_stats();
            simulation_terminator("TIME'S UP");
        }
        break;

    case SIGUSR1: /*simulation terminated due to time's up*/
        simulation_terminator("MELTDOWN");
        break;
    case SIGINT: /*simulation terminated due to the user or an error of him*/
        simulation_terminator("USER");
        break;
    case SIGTERM:
        while (wait(NULL) != -1); /*wait for all the childs to terminate*/
        deallocate_ipc();
        exit(1);
        break;
    }
}

/*function that read the stats from the shared memory and print it into stdout*/
void print_stats()
{
    /*protect the printing command*/
    old_mask = block_signals(2, SIGINT, SIGUSR1);
    printf("\n----------------------- STATUS SIMULATION %2i ----------------------\n", calls);
    printf("| %-40s | %-20s |\n", "Description", "Value");
    printf("-------------------------------------------------------------------\n");
    printf("| %-40s | %20i |\n", "Atoms inserted in the last second", shm_pointer->atoms_inserted_last_second);
    printf("| %-40s | %20i |\n", "Atoms inserted in total", shm_pointer->total_atoms_inserted);
    printf("| %-40s | %20i |\n", "Activation in the last second", shm_pointer->activations_last_second);
    printf("| %-40s | %20i |\n", "Activation in total", shm_pointer->total_activations);
    printf("| %-40s | %20i |\n", "Splitting in the last second", shm_pointer->splitting_last_second);
    printf("| %-40s | %20i |\n", "Splitting in total", shm_pointer->total_splitting);
    printf("| %-40s | %20.1f |\n", "Energy Produced in the last second", shm_pointer->energy_produced_last_second);
    printf("| %-40s | %20.1f |\n", "Energy Produced in total", shm_pointer->total_produced_energy);
    printf("| %-40s | %20i |\n", "Waste in the last second", shm_pointer->waste_last_second);
    printf("| %-40s | %20i |\n", "Waste in total", shm_pointer->total_waste);
    printf("| %-40s | %20.1f |\n", "Energy consumed in the last second", shm_pointer->consumed_energy_last_second);
    printf("| %-40s | %20.1f |\n", "Energy consumed in total", shm_pointer->total_consumed_energy);
    printf("| %-40s | %20.1f |\n", "Energy absorbed in the last second", shm_pointer->absorbed_energy_last_second);
    printf("| %-40s | %20.1f |\n", "Energy absorbed in total", shm_pointer->total_absorbed_energy);
    printf("| %-40s | %20i |\n", "Inhibitor mode", shm_pointer->inhibitor_mode);
    printf("-------------------------------------------------------------------\n");
    printf("| %-40s | %20i |\n", "CURRENT ATOM:", ((shm_pointer->total_atoms_inserted + shm_pointer->total_splitting) - shm_pointer->total_waste));
    printf("-------------------------------------------------------------------\n");
    /*unblock the signals that we blocked previously*/
    reset_signals(old_mask);
}

/*function that update the shared memory and reset the relative variable, blocking one semaphore at a time, and check if there's a blackout or explode situation*/
void check_update_stats()
{
    /*protecting the update phase from unexpected signal*/
    old_mask = block_signals(2, SIGINT, SIGUSR1);

    semaphore_operation(sem_id, ATOMS_SEM, -1);
    shm_pointer->total_atoms_inserted += shm_pointer->atoms_inserted_last_second;
    shm_pointer->atoms_inserted_last_second = 0;
    semaphore_operation(sem_id, ATOMS_SEM, 1);

    semaphore_operation(sem_id, WASTE_SEM, -1);
    shm_pointer->total_waste += shm_pointer->waste_last_second;
    shm_pointer->waste_last_second = 0;
    semaphore_operation(sem_id, WASTE_SEM, 1);

    semaphore_operation(sem_id, ACTIVATION_SEM, -1);
    shm_pointer->total_activations += shm_pointer->activations_last_second;
    shm_pointer->activations_last_second = 0;
    semaphore_operation(sem_id, ACTIVATION_SEM, 1);

    semaphore_operation(sem_id, SPLITTING_SEM, -1);
    shm_pointer->total_splitting += shm_pointer->splitting_last_second;
    shm_pointer->splitting_last_second = 0;
    semaphore_operation(sem_id, SPLITTING_SEM, 1);

    semaphore_operation(sem_id, ENERGY_SEM, -1);
    shm_pointer->total_produced_energy += shm_pointer->energy_produced_last_second;
    shm_pointer->energy_produced_last_second = 0;
    semaphore_operation(sem_id, ENERGY_SEM, 1);

    shm_pointer->consumed_energy_last_second = config.energy_demand;
    shm_pointer->total_consumed_energy += shm_pointer->consumed_energy_last_second;

    semaphore_operation(sem_id, ABSORBING_SEM, -1);
    shm_pointer->total_absorbed_energy += shm_pointer->absorbed_energy_last_second;
    shm_pointer->absorbed_energy_last_second = 0;
    semaphore_operation(sem_id, ABSORBING_SEM, 1);
    /*unblocking*/
    reset_signals(old_mask);

    int energy = shm_pointer->total_produced_energy - shm_pointer->total_consumed_energy;
    if (energy > config.energy_explode_threshold)
    {
        simulation_terminator("EXPLODE");
    }
    if (energy < 0)
    {
        simulation_terminator("BLACKOUT");
    }
}

/*function to initialize the value of all the semaphore and check for error*/
void sem_setting()
{
    if (semctl(sem_id, START_SEM, SETVAL, 0) == -1)
    {
        perror("an error occurred in semctl for START_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, ATOMS_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for ATOMS_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, SPLITTING_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for SPLITTING_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, WASTE_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for WASTE_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, ACTIVATION_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for ACTIVATION_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, ABSORBING_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for ABSORBING_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, ENERGY_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for ENERGY_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, MSGQ_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for MSGQ_SEM");
        exit(EXIT_FAILURE);
    }
    if (semctl(sem_id, LOG_SEM, SETVAL, 1) == -1)
    {
        perror("an error occurred in semctl for LOG_SEM");
        exit(EXIT_FAILURE);
    }
}

/*funtion that close all the ipc elements and terminate all the process*/
void simulation_terminator(char *Cause)
{
    /*protecting the termination phase from unexpected signal*/
    block_signals(3, SIGUSR1, SIGALRM, SIGINT);
    log_message("Closing simulation...", getpid(), sem_id);
    printf("Closing simulation...\n");
    printf("Simulation terminated due to %s\n", Cause); /*print a message termination*/
    killpg(getpgid(getpid()), SIGTERM);                 /*Terminate all the process in the group of process*/
    pause();
}

/*function tha allocate all the ipc that the program is going to use*/
void allocate_ipc()
{

    /*Generating a unique key for each element (Using Ftok function) and checking for error*/
    key_t shm_key, sem_key, msgq_key;
    shm_key = ftok("./src/master.c", 'A');
    if (shm_key == -1)
    {
        perror("an error occurred in ftok for shm_key");
        exit(EXIT_FAILURE);
    }

    sem_key = ftok("./src/master.c", 'B');
    if (sem_key == -1)
    {
        perror("an error occurred in ftok for sem_key");
        exit(EXIT_FAILURE);
    }

    msgq_key = ftok("./src/master.c", 'C');
    if (msgq_key == -1)
    {
        perror("an error occurred in ftok for msgq_key");
        exit(EXIT_FAILURE);
    }

    /*Creating the Shared Memory and getting his id*/
    shm_id = shmget(shm_key, 64, IPC_CREAT | 0666);
    /*checking for some error while creating the shared memory*/
    if (shm_id == -1)
    {
        perror("an error occurred creating the shared memory");
        raise(SIGINT); /*termination due to an error of the USER*/
    }

    /*Creating the Semaphore and getting his id*/
    sem_id = semget(sem_key, 9, IPC_CREAT | 0666);
    /*checking for some error while creating the semaphore*/
    if (sem_id == -1)
    {
        perror("an error occurred creating a semaphore");
        raise(SIGINT);
    }
    /*initialzing the semaphores*/
    sem_setting();

    /*Creating the Message Queue and getting his id*/
    msgq_id = msgget(msgq_key, IPC_CREAT | 0666);
    /*checking for some error while creating the semaphore*/
    if (msgq_id == -1)
    {
        perror("an error occurred creating the message queue");
        raise(SIGINT);
    }

    /*attaching the shared memory*/
    shm_pointer = (shared_stats *)(shmat(shm_id, NULL, 0));
    if (shm_pointer == (void *)-1)
    {
        perror("an error occurred attaching the shared memory");
        raise(SIGINT); // Clean exit on error
    }

    /*writing onto the logfile*/
    log_message("IPC created correctly...", getpid(), sem_id);
}

/*deallocate all the ipc elements created previously*/
void deallocate_ipc()
{
    if (sem_id != -1)
    {
        semctl(sem_id, 0, IPC_RMID); /*remove the semaphore*/
    }

    if (shm_pointer != (void *)-1)
    {
        shmdt(shm_pointer); /*deattach the shared memory*/
    }

    if (shm_id != -1)
    {
        shmctl(shm_id, IPC_RMID, NULL); /*remove the shared memory*/
    }

    if (msgq_id != -1)
    {
        msgctl(msgq_id, IPC_RMID, NULL); /*remove the message queue*/
    }
}