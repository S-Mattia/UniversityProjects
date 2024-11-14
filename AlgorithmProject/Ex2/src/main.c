#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <limits.h>
#include "edit_distance_dyn.h"
#include <time.h>

#define N_STRING 1000
#define N_DICTIONARY 800000
#define N_LIST_MIN_WORD 10

/**
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 20/04/2024;
 * @details Program that bring the word from a txt file, and give the correction for those not find in the dictionary;
 */
int corrector(FILE *text_file, FILE *dictionary_file);
static void to_lower_case(char *a);
static void load_word(FILE *infile, char **array, int max_size);
FILE *open_file(const char *file_path);
void freeArray(char **array);

int main(int argc, char *argv[])
{
    if (argc < 3)
    {
        printf("Usage: <text_file_name> <dictionary_file_name>\n");
        exit(EXIT_FAILURE);
    }
    FILE *text_file = open_file(argv[1]);
    FILE *dictionary = open_file(argv[2]);
    corrector(text_file, dictionary);
    fclose(text_file);
    fclose(dictionary);
    clock_t t1 = clock() / CLOCKS_PER_SEC;
    printf("\nIl programma ha impiegato: %ld s.\n", t1);
    return 0;
}

/** DESCRIPTION: Corrector: return a small list of words in dictionary_file that have minimum edit distance for every word of the text_file;*/
int corrector(FILE *text_file, FILE *dictionary_file)
{
    if (text_file == NULL || dictionary_file == NULL)
    {
        fprintf(stderr, "corrector: unable to open the file\n");
        exit(EXIT_FAILURE);
    }

    printf("\nCreating the environment...");
    char **text;
    char **dictionary;
    text = malloc(N_STRING * sizeof(char *));
    dictionary = malloc(N_DICTIONARY * sizeof(char *));
    if (text == NULL || dictionary == NULL)
    {
        fprintf(stderr, "corrector: unable to allocate memory\n");
        exit(EXIT_FAILURE);
    }
    printf("DONE\n");

    printf("\nLoading the text into memory... ");
    load_word(text_file, text, N_STRING);
    printf("DONE\n");
    printf("\nLoading the dictionary into memory... ");
    load_word(dictionary_file, dictionary, N_DICTIONARY);
    printf("DONE\n");

    printf("\nScanning the text looking for some errors...\n");
    int i = 0;
    while (text[i] != NULL)
    {
        int min_edit = INT_MAX;
        int min_word_count = 0;
        char *min_words[N_LIST_MIN_WORD];
        to_lower_case(text[i]);

        int j = 0;
        while (dictionary[j] != NULL)
        {
            int current_edit = edit_distance_dyn(text[i], dictionary[j]);
            if (current_edit < min_edit)
            {
                min_edit = current_edit;
                min_word_count = 1;
                min_words[0] = dictionary[j];
            }
            else if (current_edit == min_edit && min_word_count < N_LIST_MIN_WORD)
            {
                min_words[min_word_count++] = dictionary[j];
            }
            j += 1;
        }

        if (min_edit == 0)
        {
            printf("\nThe word \"%s\" is correct.", text[i]);
        }
        else
        {
            printf("\nThe word \"%s\" is incorrect. The optimal corrections are:\n", text[i]);
            for (int k = 0; k < min_word_count; k++)
            {
                printf("%s, ", min_words[k]);
            }
            printf(" with an edit-distance of: %i.", min_edit);
        }
        i += 1;
    }

    freeArray(text);
    freeArray(dictionary);

    return 0;
}

/** DESCRIPTION: load_word: Function to load a text file into an array of string into memory;*/
static void load_word(FILE *infile, char **array, int max_size)
{
    char *read_line;
    char buffer[1024];
    int i = 0;

    while (fgets(buffer, sizeof(buffer), infile) != NULL && i < max_size - 1)
    {
        read_line = malloc((strlen(buffer) + 1) * sizeof(char));
        if (read_line == NULL)
        {
            fprintf(stderr, "load_word: unable to allocate memory for the read line\n");
            exit(EXIT_FAILURE);
        }
        strcpy(read_line, buffer);

        char *StringToken = strtok(read_line, ",. !?:\n\t\r");
        while (StringToken != NULL && i < max_size - 1)
        {
            char *string_field = malloc((strlen(StringToken) + 1) * sizeof(char));
            if (string_field == NULL)
            {
                fprintf(stderr, "load_word: unable to allocate memory for the string\n");
                exit(EXIT_FAILURE);
            }
            strcpy(string_field, StringToken);
            array[i] = string_field;
            StringToken = strtok(NULL, ",. !?:\n\t\r");
            i += 1;
        }
        free(read_line);
    }
    array[i] = NULL; // Terminate the array with NULL
}

void freeArray(char **array)
{
    // Free memory allocated for array
    int i = 0;
    while (array[i] != NULL)
    {
        free(array[i]);
        i++;
    }
    free(array);
}

// Convert the input string to lower case;
static void to_lower_case(char *a)
{
    int k = strlen(a);
    for (int i = 0; i < k; i++)
    {
        a[i] = tolower(a[i]);
    }
}

// Open a file and return the pointer to it;
FILE *open_file(const char *file_path)
{
    FILE *fp = fopen(file_path, "r");

    if (fp == NULL)
    {
        fprintf(stderr, "open_file: unable to open the file %s\n", file_path);
        exit(EXIT_FAILURE);
    }
    return fp;
}