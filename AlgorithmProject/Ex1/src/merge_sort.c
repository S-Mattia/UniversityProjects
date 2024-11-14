#include "merge_sort.h"

/**
 * @author: Sandri Mattia, Sandri Gabriele ;
 * @date: 28 / 03 / 2024 ;
 * @details: Mergesort algorithm, used to sort generic element ;
 */

void merge_sort(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void *))
{

    if (nitems > 1)
    {
        void *mid = base + size * (nitems / 2);
        size_t baselength = (nitems / 2);
        size_t midlength = (nitems / 2 + nitems % 2);
        merge_sort(base, baselength, size, (*compar));
        merge_sort(mid, midlength, size, (*compar));
        merge(base, baselength, mid, midlength, size, (*compar));
    }
}

/** DESCRIPTION: Merge function, used to merge 2 array in the correct order ;
 */
void merge(void *base, size_t initems, void *mid, size_t fnitems, size_t size, int (*compar)(const void *, const void *))
{
    void *first_half = malloc(initems * size);
    void *second_half = malloc(fnitems * size);
    if (first_half == NULL || second_half == NULL)
    {
      fprintf(stderr, "main: unable to allocate memory for the merge_sort");
      exit(EXIT_FAILURE);
    }

    memcpy(first_half, base, initems * size);
    memcpy(second_half, mid, fnitems * size);

    void *first_cursor = first_half;
    void *second_cursor = second_half;
    size_t tot_lenght = initems + fnitems;

    while (tot_lenght > 0)
    {
        if (initems == 0)
        {
            memcpy(base, second_cursor, size);
            second_cursor += size;
            fnitems -= 1;
        }
        else if (fnitems == 0)
        {
            memcpy(base, first_cursor, size);
            first_cursor += size;
            initems -= 1;
        }
        else
        {
            int value = (*compar)(first_cursor, second_cursor);
            if (value < 0)
            {
                memcpy(base, first_cursor, size);
                first_cursor += size;
                initems -= 1;
            }
            else
            {
                memcpy(base, second_cursor, size);
                second_cursor += size;
                fnitems -= 1;
            }
        }
        base += size;
        tot_lenght -= 1;
    }
    free(first_half);
    free(second_half);
}