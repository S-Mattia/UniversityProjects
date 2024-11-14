#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int min_3_el(int a, int b, int c);

int edit_distance_memo(const char *s1, const char *s2, int m, int n);

int edit_distance_dyn(const char *s1, const char *s2);