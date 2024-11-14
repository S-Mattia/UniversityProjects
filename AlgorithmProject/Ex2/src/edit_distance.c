#include "edit_distance.h"

/**
 * @author Sandri Mattia, Sandri Gabriele ;
 * @date 10 / 04 / 2024
 * @details algorithm for calculate the minimum edit_distance beetwen two string ;
 */

/** DESCRIPTION: Edit_distance function, recoursive function to calculate number of operations to transform a word to an other;
 */
int edit_distance(const char *s1, const char *s2)
{
    // s1 | s2 == empty
    if (s1 == NULL || s2 == NULL)
    {
        fprintf(stderr, "The words must not be NULL");
        exit(EXIT_FAILURE);
    }
    else if (*s1 == 0 & *s2 == 0)
    {
        return 0;
    }
    else if (*s1 == 0)
    {
        return 1 + edit_distance(s1, s2 + 1);
    }
    else if (*s2 == 0)
    {
        return 1 + edit_distance(s1 + 1, s2);
    }

    int equal = __INT_MAX__;
    if (*s1 == *s2)
    {
        equal = 0 + edit_distance(s1 + 1, s2 + 1);
    }
    int canc = 1 + edit_distance(s1, s2 + 1);
    int insert = 1 + edit_distance(s1 + 1, s2);
    return min_3_el(equal, insert, canc);
}
