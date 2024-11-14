#include "edit_distance_dyn.h"
#define MAX_LEN 1000

/**
 * @author Sandri Mattia, Sandri Gabriele ;
 * @date 19 / 04 / 2024
 * @details algorithm for calculate the minimum edit_distance beetwen two string, using dynamic programmation;
 */

int min_3_el(int a, int b, int c);

int edit_distance_memo(const char *s1, const char *s2, int m, int n);

int memo[MAX_LEN][MAX_LEN];

/** DESCRIPTION: Edit_distance_dyn, dynamic function to calculate number of operations to transform a word to an other;
 */
int edit_distance_dyn(const char *s1, const char *s2)
{
    if (s1 == NULL || s2 == NULL)
    {
        fprintf(stderr, "The words must not be  NULL");
        exit(EXIT_FAILURE);
    }
    int m = strlen(s1);
    int n = strlen(s2);

    for (int i = 0; i <= m; i++)
    {
        for (int j = 0; j <= n; j++)
        {
            memo[i][j] = -1;
        }
    }
    return edit_distance_memo(s1, s2, m, n);
}

/** DESCRIPTION: Edit_distance_memo function, recoursive function to calculate and store partial result in a matrix called memo*/
int edit_distance_memo(const char *s1, const char *s2, int m, int n)
{
    if (m == 0)
        return n;
    if (n == 0)
        return m;
    if (memo[m][n] != -1)
        return memo[m][n];
    int equal = __INT_MAX__;
    if (s1[m - 1] == s2[n - 1])
    {
        equal = edit_distance_memo(s1, s2, m - 1, n - 1); // equal
    }

    return memo[m][n] = min_3_el(
               1 + edit_distance_memo(s1, s2, m, n - 1), // Insert
               1 + edit_distance_memo(s1, s2, m - 1, n), // Delete
               equal);
}

int min_3_el(int a, int b, int c)
{
    int min = a;
    if (min > b)
    {
        min = b;
    }
    if (min > c)
    {
        min = c;
    }
    return min;
}
