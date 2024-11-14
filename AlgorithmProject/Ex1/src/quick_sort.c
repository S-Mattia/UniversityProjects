#include "quick_sort.h"

/**
 * @author Sandri Mattia, Sandri Gabriee;
 * @date 02/04/2024;
 * @details Quicksort algorithm for generic elements;
 */

/** DESCRIPTION: Splits the array into two parts excluding the central pivot
 */
void quick_sort(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void *))
{
    if (nitems > 1)
    {
        int corrent_pivot_index = partition(base, nitems, size, (*compar));

        void *left_pivot = base;
        size_t n_left_item = corrent_pivot_index;

        void *right_pivot = base + ((corrent_pivot_index + 1) * size);
        size_t n_right_item = nitems - (corrent_pivot_index + 1);

        quick_sort(left_pivot, n_left_item, size, (*compar));
        quick_sort(right_pivot, n_right_item, size, (*compar));
    }
}

/** DESCRIPTION: takes the first element as a pivot, divides the array,
    placing the minor elements on the left and the major elements on the right.
    then uses the pivot as a border between the two groups, returning his index
*/
int partition(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void *))
{
    void *pivot = base;
    int i = 0;
    int j = 1;

    while (j < nitems)
    {
        if ((*compar)(pivot, (base + (j * size))) >= 0)
        {
            i += 1;
            if (i != j)
            {
                swap((base + (j * size)), (base + (i * size)), size);
            }
        }
        j += 1;
    }
    pivot = base + (i * size);
    swap(base, pivot, size);
    return i;
}

void swap(void *firstitem, void *seconditem, size_t size)
{
    void *temp = malloc(size);
    if (temp == NULL)
    {
      fprintf(stderr, "main: unable to allocate memory for temp in quick_sort");
      exit(EXIT_FAILURE);
    }
    memcpy(temp, firstitem, size);
    memcpy(firstitem, seconditem, size);
    memcpy(seconditem, temp, size);
    free(temp);
}
