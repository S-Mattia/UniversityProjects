#include <stdio.h>
#include <stdlib.h>
#include "merge_sort.h"
#include "quick_sort.h"
#include "comparator.h"

void sort_records(FILE *infile, FILE *outfile, size_t field, size_t algo);