#include "comparator.h"

/**
 * @author: Sandri Mattia, Sandri Gabriele ;
 * @date: 17 / 04 / 2024 ;
 * @details: Comparator used in sort_records.c ;
 */

struct Record
{
  int id;
  char *field1;
  int field2;
  float field3;
};

/// It takes as input two pointers to struct record and compare by the sting field
int compare_record_string_field(const void *r1_p, const void *r2_p)
{
  if (r1_p == NULL)
  {
    fprintf(stderr, "compare_string: the first parameter is a null pointer");
    exit(EXIT_FAILURE);
  }
  if (r2_p == NULL)
  {
    fprintf(stderr, "compare_string: the second parameter is a null pointer");
    exit(EXIT_FAILURE);
  }
  struct Record *rec1_p = *(struct Record **)r1_p;
  struct Record *rec2_p = *(struct Record **)r2_p;
  return strcmp(rec1_p->field1, rec2_p->field1);
}

/// It takes as input two pointers to struct record and compare by the int field
int compare_record_int_field(const void *r1_p, const void *r2_p)
{
  if (r1_p == NULL)
  {
    fprintf(stderr, "compare_record_int_field: the first parameter is a null pointer");
    exit(EXIT_FAILURE);
  }
  if (r2_p == NULL)
  {
    fprintf(stderr, "compare_record_int_field: the second parameter is a null pointer");
    exit(EXIT_FAILURE);
  }
  struct Record *rec1_p = *(struct Record **)r1_p;
  struct Record *rec2_p = *(struct Record **)r2_p;
  if (rec1_p->field2 < rec2_p->field2)
  {
    return (-1);
  }
  else if (rec1_p->field2 > rec2_p->field2)
  {
    return (1);
  }
  else
  {
    return (0);
  }
}

/// It takes as input two pointers to struct record and compare by the float field
int compare_record_float_field(const void *r1_p, const void *r2_p)
{
  if (r1_p == NULL)
  {
    fprintf(stderr, "compare_record_float_field: the first parameter is a null pointer");
    exit(EXIT_FAILURE);
  }
  if (r2_p == NULL)
  {
    fprintf(stderr, "compare_record_float_field: the second parameter is a null pointer");
    exit(EXIT_FAILURE);
  }
  struct Record *rec1_p = *(struct Record **)r1_p;
  struct Record *rec2_p = *(struct Record **)r2_p;
  if (rec1_p->field3 < rec2_p->field3)
  {
    return (-1);
  }
  else if (rec1_p->field3 > rec2_p->field3)
  {
    return (1);
  }
  else
  {
    return (0);
  }
}
