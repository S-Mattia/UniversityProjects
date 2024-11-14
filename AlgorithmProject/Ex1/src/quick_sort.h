#include <memory.h>
#include <stdio.h>
#include <stdlib.h>

void swap(void *firstitem, void *seconditem, size_t size);

int partition(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void *));

void quick_sort(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void *));