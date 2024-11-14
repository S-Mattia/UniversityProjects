#include "sort_records.h"
#include <time.h>

// It open the file cvs from read
FILE *open_file(const char *file_path)
{
  FILE *fp;
  printf("\nOpening file: %s... ", file_path);
  fp = fopen(file_path, "r");

  if (fp == NULL)
  {
    fprintf(stderr, "main: unable to open the file %s\n", file_path);
    exit(EXIT_FAILURE);
  }
  printf("-DONE!\n");
  return fp;
}

// It make/open an out file .txt
FILE *make_outfile(const char *file_path)
{
  FILE *fp;
  printf("\nOpening file: %s... ", file_path);
  fp = fopen(file_path, "w");

  if (fp == NULL)
  {
    fprintf(stderr, "main: unable to open the file %s\n", file_path);
    exit(EXIT_FAILURE);
  }
  printf("-DONE!\n");
  return fp;
}

// It should be invoked with one parameter specifying the filepath of the data file
int main(int argc, const char *argv[])
{
  if (argc < 5)
  {
    printf("Usage: <infile_name> <outfile_name> <field> <algo>");
    exit(EXIT_FAILURE);
  }
  FILE *infile_name = open_file(argv[1]);
  FILE *outfile_name = make_outfile(argv[2]);

  sort_records(infile_name, outfile_name, atoi(argv[3]), atoi(argv[4]));
  fclose(infile_name);
  fclose(outfile_name);

  clock_t t1 =clock()/CLOCKS_PER_SEC;
  printf("\nThe program has been compleated in: %ld s.\n",t1);

  return (EXIT_SUCCESS);
}
