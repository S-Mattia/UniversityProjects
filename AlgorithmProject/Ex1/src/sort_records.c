#include "sort_records.h"

/**
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 17/04/2024;
 * @details Program that bring record from a file, sort it, and rewrite the sorted record in an onther file;
 */

struct Record
{
  int id;
  char *field1;
  int field2;
  float field3;
};

#define N_RECORDS 20000000

static void load_array(FILE *infile, struct Record **array_record);
static void upload_array(FILE *outfile, struct Record **array_record);

/** DESCRIPTION: Sort Records, used to sort a file of records and print in an onther file in the correct order;
 */
void sort_records(FILE *infile, FILE *outfile, size_t field, size_t algo)
{

  struct Record **array_record;
  array_record = malloc(N_RECORDS * sizeof(struct Record));
  if (array_record == NULL)
  {
    fprintf(stderr, "Unable to allocate memory for array_record\n");
    exit(EXIT_FAILURE);
  }

  load_array(infile, array_record);

  printf("\nSorting records...");

  if (algo == 1)
  {
    if (field == 1)
    {
      merge_sort(array_record, N_RECORDS, sizeof(struct Record *), compare_record_string_field);
    }
    else if (field == 2)
    {
      merge_sort(array_record, N_RECORDS, sizeof(struct Record *), compare_record_int_field);
    }
    else
    {
      merge_sort(array_record, N_RECORDS, sizeof(struct Record *), compare_record_float_field);
    }
  }
  else
  {
    if (field == 1)
    {
      quick_sort(array_record, N_RECORDS, sizeof(struct Record *), compare_record_string_field);
    }
    else if (field == 2)
    {
      quick_sort(array_record, N_RECORDS, sizeof(struct Record *), compare_record_int_field);
    }
    else
    {
      quick_sort(array_record, N_RECORDS, sizeof(struct Record *), compare_record_float_field);
    }
  }
  printf("-DONE!\n");

  upload_array(outfile, array_record);
  free(array_record);
}

/** DESCRIPTION: load_array, used to load records from a file into the memory;
 */
static void load_array(FILE *infile, struct Record **array_record)
{
  char *read_line_p;
  char buffer[1024];
  int buf_size = 1024;
  int i = 0;

  printf("\nLoading data form the infile into memory... ");
  while ((fgets(buffer, buf_size, infile) != NULL) && (N_RECORDS - 1) >= i)
  {
    read_line_p = malloc((strlen(buffer) + 1) * sizeof(char));
    if (read_line_p == NULL)
    {
      fprintf(stderr, "main: unable to allocate memory for the read line");
      exit(EXIT_FAILURE);
    }

    strcpy(read_line_p, buffer);
    char *integer_id_in_read_line_p = strtok(read_line_p, ",");
    char *string_field_in_read_line_p = strtok(NULL, ",");
    char *integer_field_in_read_line_p = strtok(NULL, ",");
    char *float_field_in_read_line_p = strtok(NULL, ",");
    char *string_field = malloc((strlen(string_field_in_read_line_p) + 1) * sizeof(char));
    if (string_field == NULL)
    {
      fprintf(stderr, "main: unable to allocate memory for the string field of the read record");
      exit(EXIT_FAILURE);
    }
    int integer_id = atoi(integer_id_in_read_line_p);
    strcpy(string_field, string_field_in_read_line_p);
    int integer_field = atoi(integer_field_in_read_line_p);
    float float_field = atof(float_field_in_read_line_p);
    struct Record *record_p = malloc(sizeof(struct Record));
    if (record_p == NULL)
    {
      fprintf(stderr, "main: unable to allocate memory for the read record");
      exit(EXIT_FAILURE);
    }

    record_p->id = integer_id;
    record_p->field1 = string_field;
    record_p->field2 = integer_field;
    record_p->field3 = float_field;

    array_record[i] = record_p;
    free(read_line_p);
    i += 1;
  }
  printf("-DONE!\n");
}

/** DESCRIPTION: Upload_array, used to print the sorted array into a file;
 */
static void upload_array(FILE *outfile, struct Record **array_record)
{
  printf("\nUploading data from the sorted array into the output file... ");
  if (outfile == NULL)
  {
    fprintf(stderr, "main: unable to open the output file\n");
    exit(EXIT_FAILURE);
  }

  int i = 0;
  struct Record *ptr = array_record[i];
  while (ptr != NULL)
  {
    fprintf(outfile, "%i,%s,%i,%f\n", ptr->id, ptr->field1, ptr->field2, ptr->field3);
    i += 1;
    free(ptr->field1);
    free(ptr);
    ptr = array_record[i];
  }
  printf("-DONE!\n");
}
