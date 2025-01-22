#include "../include/utils.h"

/*function that manage the operations on the semaphores*/
int semaphore_operation(int sem_id, int sem_num, int n)
{
    struct sembuf operation;
    operation.sem_num = sem_num; /*the semaphore's number*/
    operation.sem_op = n;        /*number that we want to increase o decrease from the semaphore*/
    operation.sem_flg = 0;       /*Flag number*/

    if (semop(sem_id, &operation, 1) == -1)
    {
        perror("semop error:");
        return -1; /*error*/
    }

    return 0; /*success*/
}

/*funtion to clear the log file*/
void log_clear()
{
    /*open the log file in write mode*/
    FILE *logfile = fopen("./log/logfile.txt", "w+");
    if (logfile == NULL)
    {
        perror("fopen logfile.txt error");
        exit(EXIT_FAILURE);
    }
    /*close the logfile*/
    fclose(logfile);
}

/*function to write on the log file*/
void log_message(const char *message, pid_t pid, int sem_id)
{
    /*open the logfile*/
    FILE *logfile = fopen("./log/logfile.txt", "a+");
    if (logfile == NULL)
    {
        perror("fopen logfile.txt error");
        exit(EXIT_FAILURE);
    }

    /*requring the LOG_sem*/
    semaphore_operation(sem_id, LOG_SEM, -1);

    /*taking the current time*/
    time_t now = time(NULL);
    char *time_str = ctime(&now);
    time_str[strlen(time_str) - 1] = '\0'; /*Removing the newline character*/

    /*writing the id of the writer process followed by the message*/
    fprintf(logfile, "[%s - ID %i]: %s\n", time_str, pid, message);

    /*release the LOG_SEM*/
    semaphore_operation(sem_id, LOG_SEM, +1);

    /*close the logfile*/
    fclose(logfile);
}

/*function to receive a PID from a message queue identified by msgq_id*/
int receive_msg(int msgq_id)
{
    msg_buffer message;

    /*Receive a message from the message queue.*/
    /*(sizeof(message) - sizeof(long)) specifies the size of the content to be received, excluding mtype.*/
    /*MSG_TYPE_PID is a macro defining the number that rapresent the type od message containgin a pid.*/
    /*The last 0 flag indicates no special options.*/
    if (msgrcv(msgq_id, &message, (sizeof(message) - sizeof(long)), MSG_TYPE_PID, 0) == -1)
    {
        /*If msgrcv returns -1, an error occurred (e.g., no messages in the queue).*/
        return -1;
    }

    /*Convert the received string back to an integer PID and return it.*/
    return atoi(message.text);
}

/*function to send the pid as message to the message queue identified by -msgq_id*/
int send_msg(int msgq_id)
{

    /*Send a message from the message queue.*/
    /*MSG_TYPE_PID is a macro defining the number that rapresent the type od message containgin a pid.*/
    /*The last 0 flag indicates no special options.*/
    msg_buffer message;
    message.type = MSG_TYPE_PID;
    sprintf(message.text, "%d", getpid());
    return msgsnd(msgq_id, &message, sizeof(message) - sizeof(long), 0);
}

/*function used to wait some nanoseconds*/
int nanosleep_custom(struct timespec *req)
{
    struct timespec rem = {0}; /*initializing rem to 0*/

    while (nanosleep(req, &rem) == -1)
    {
        if (errno == EINTR)
        {
            /*in case of sleep interrupted by a signal, continue to wait for the remaining time*/
            *req = rem; /*update req as rem*/
        }
        else
        {
            perror("nanosleep");
            return -1; /*error*/
        }
    }
    return 0; /*success*/
}

/*function to block a signal, (This function was taken from an example project posted in the 2022/2023 academic year )*/
sigset_t block_signals(int num, ...)
{
    sigset_t mask, old_mask;
    va_list args;
    int i;

    /*initializing the empty set*/
    sigemptyset(&mask);

    /*reading the arguments of the function after num*/
    va_start(args, num);

    for (i = 0; i < num; i++)
    {
        /*adding all the signal into the set */
        sigaddset(&mask, va_arg(args, int)); /*va_args used to get a single arg*/
    }
    /*deallocate the memory used for args */
    va_end(args);

    /*blocking all the signals into the sigset sturct*/
    sigprocmask(SIG_BLOCK, &mask, &old_mask);

    /*returning the old mask*/
    return old_mask;
}

/*function to unblock a signal, (This function was taken from an example project posted in the 2022/2023 academic year )*/
sigset_t unblock_signals(int num, ...)
{
    sigset_t mask, old_mask;
    va_list args;
    int i;

    /*initializing the empty set*/
    sigemptyset(&mask);

    /*reading the arguments of the function after num*/
    va_start(args, num);

    for (i = 0; i < num; i++)
    {
        /*adding all the signal into the set */
        sigaddset(&mask, va_arg(args, int)); /*va_args used to get a single arg*/
    }
    /*deallocate the memory used for args */
    va_end(args);

    /*blocking all the signals into the sigset sturct*/
    sigprocmask(SIG_UNBLOCK, &mask, &old_mask);

    /*returning the old mask*/
    return old_mask;
}

/*reset the old_mask of the process (This function was taken from an example project posted in the 2022/2023 academic year )*/
void reset_signals(sigset_t mask)
{
    sigprocmask(SIG_SETMASK, &mask, NULL);
}

/*function used to set the handler of a signal*/
void set_handler(int signum, void (*func)(int), int flag)
{
    struct sigaction sa;

    /*intializing the sication struct and the mask to zero*/
    bzero(&sa, sizeof(sa));
    sigset_t mask;
    sigemptyset(&mask);

    /*setting the handler and the flag*/
    sa.sa_handler = func;
    sa.sa_mask = mask;
    sa.sa_flags = flag;

    /*changing the handler for signum signal*/
    sigaction(signum, &sa, NULL);
}

/*callback function to process all the key=value of the file (ini.h library) */
int handler(void *user, const char *section, const char *name, const char *value)
{
    configuration *config = (configuration *)user;

/*macro that check the section and the name*/
#define MATCH(s, n) strcmp(section, s) == 0 && strcmp(name, n) == 0

    if (MATCH("master", "n_atoms_init"))
    {
        config->n_atoms_init = atoi(value);
    }
    else if (MATCH("master", "energy_demand"))
    {
        config->energy_demand = atoi(value);
    }
    else if (MATCH("master", "energy_explode_threshold"))
    {
        config->energy_explode_threshold = atoi(value);
    }
    else if (MATCH("master", "simulation_duration"))
    {
        config->simulation_duration = atoi(value);
    }
    else if (MATCH("atom", "n_atom_max"))
    {
        config->n_atom_max = atoi(value);
    }
    else if (MATCH("atom", "n_atom_min"))
    {
        config->n_atom_min = atoi(value);
    }
    else if (MATCH("feeder", "feeder_step"))
    {
        config->feeder_step = atoi(value);
    }
    else if (MATCH("feeder", "n_new_atoms"))
    {
        config->n_new_atoms = atoi(value);
    }
    else if (MATCH("activator", "activator_step"))
    {
        config->activator_step = atoi(value);
    }
    else if (MATCH("inhibitor", "inhibitor_step"))
    {
        config->inhibitor_step = atoi(value);
    }
    else if (MATCH("inhibitor", "inhibitor_mode"))
    {
        config->in_mode = atoi(value);
    }
    else if (MATCH("inhibitor", "inhibitor_percent_absorbing"))
    {
        config->inhibitor_percent_absorbing = atof(value);
    }
    else if (MATCH("inhibitor", "inhibitor_probability_waste"))
    {
        config->inhibitor_probability_waste = atof(value);
    }
    else
    {
        return 0; /* unknown section/name, error */
    }
    return 1;
}
