#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

void merge(void *base, size_t initems, void *mid, size_t fnitems, size_t size, int (*compar)(const void *, const void *));

void merge_sort(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void *));