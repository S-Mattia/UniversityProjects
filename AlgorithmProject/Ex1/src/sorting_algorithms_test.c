#include <string.h>
#include "../../Unity/unity.h"
#include "merge_sort.h"
#include "quick_sort.h"

/**
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 08/04/2024;
 * @details unit-testing for quick_sort and merge_sort algorithm;
 */

int comparint(const void *first, const void *second);
int comparstring(const void *first, const void *second);

void setUp(void)
{
}

void tearDown(void)
{
}

void test_sort_null_array_merge()
{
    void *a = NULL;
    merge_sort(a, 0, sizeof(a), comparint);
    TEST_ASSERT_NULL(a);
}

void test_sort_empty_array_merge()
{
    size_t len_a = 0;
    size_t len_b = 0;
    int a[len_a];
    int b[len_b];
    merge_sort(a, len_a, sizeof(a[0]), comparint);
    TEST_ASSERT_TRUE(*a == *b);
}

//Sorting Tests, using different type elements;
void test_sort_one_int_el_array_merge()
{
    size_t len_a = 1;
    int *a = malloc(sizeof(int));
    int *b = malloc(sizeof(int));
    if (a == NULL || b == NULL)
    {
      fprintf(stderr, "main: unable to allocate memory for the first parameter in test_sort_one_int_el_array_merge");
      exit(EXIT_FAILURE);
    }
    *a = 1;
    *b = 1;
    merge_sort(a, len_a, sizeof(a[0]), comparint);
    TEST_ASSERT_EQUAL_INT(*a, *b);
    free(a);
    free(b);
}

void test_sort_one_string_el_array_merge()
{
    size_t len_a = 1;
    char *a = "albero";
    char *b ="albero";
    merge_sort(a, len_a, sizeof(a[0]), comparstring);
    TEST_ASSERT_EQUAL_STRING(a, b);
}

void test_sort_three_int_el_array_merge()
{
    size_t len_a = 3;
    int a[3] = {2, 3, 1};
    int b[3] = {1, 2, 3};
    merge_sort(a, len_a, sizeof(a[0]), comparint);
    for (int i = 0; i < len_a; i++)
    {
        TEST_ASSERT_EQUAL_INT(a[i], b[i]);
    }
}


void test_sort_three_string_el_array_merge()
{
    size_t len_a = 5;
    char a[5][10] = {"ciao", "albero", "gioco", "birillo", "alberot"};
    char b[5][10] = {"albero", "alberot", "birillo", "ciao", "gioco"};
    merge_sort(a, len_a, sizeof(a[0]), comparstring);
    for (int i = 0; i < len_a; i++)
    {
        for (int j = 0; j < sizeof(a[0]); j++)
        {
            TEST_ASSERT_EQUAL_CHAR(a[i][j], b[i][j]);
        }
    }
}

void test_sort_null_array_quick()
{
    void *a = NULL;
    quick_sort(a,0,sizeof(a),comparint);
    TEST_ASSERT_NULL(a);
}

void test_sort_empty_array_quick()
{
   
    size_t len_a = 0;
    size_t len_b = 0;
    int a[len_a];
    int b[len_b];
    quick_sort(a,len_a,sizeof(a[0]),comparint);
    TEST_ASSERT_TRUE(*a==*b);

}

void test_sort_one_int_el_array_quick()
{
    size_t len_a = 1;
    int *a = malloc(sizeof(int));
    int *b = malloc(sizeof(int));
    if (a == NULL || b == NULL)
    {
      fprintf(stderr, "main: unable to allocate memory for the first parameter in test_sort_one_int_el_array_quick");
      exit(EXIT_FAILURE);
    }
    *a = 1;
    *b = 1;
    quick_sort(a,len_a,sizeof(a[0]),comparint);
    TEST_ASSERT_EQUAL_INT(*a,*b);
    free(a);
    free(b);
}

void test_sort_one_string_el_array_quick()
{
    size_t len_a = 1;
    char *a = "albero";
    char *b ="albero";
    quick_sort(a, len_a, sizeof(a[0]), comparstring);
    TEST_ASSERT_EQUAL_STRING(a, b);
}

void test_sort_three_int_el_array_quick(){

    int a[3] = {2,3,1};
    size_t len_a=3;
    int b[3] = {1,2,3};
    quick_sort(a,len_a,sizeof(a[0]),comparint);
    for(int i = 0; i < len_a;i++){
        TEST_ASSERT_EQUAL_INT(a[i],b[i]);
    }
}

void test_sort_three_string_el_array_quick(){
    char a[5][10] = {"ciao","albero","gioco","birillo","astronave"};
    size_t len_a = 5;
    char b[5][10] = {"albero","astronave","birillo","ciao","gioco"};
    quick_sort(a,len_a,sizeof(a[0]),comparstring);
    for(int i = 0; i < len_a;i++){
        for(int j = 0; j < sizeof(a[0]);j++){
            TEST_ASSERT_EQUAL_CHAR(a[i][j],b[i][j]);
        }
    }
}


int main()
{
    UNITY_BEGIN();
    RUN_TEST(test_sort_empty_array_merge);
    RUN_TEST(test_sort_null_array_merge);
    RUN_TEST(test_sort_one_int_el_array_merge);
    RUN_TEST(test_sort_one_string_el_array_merge);
    RUN_TEST(test_sort_three_int_el_array_merge);
    RUN_TEST(test_sort_three_string_el_array_merge);
    RUN_TEST(test_sort_empty_array_quick);
    RUN_TEST(test_sort_null_array_quick);
    RUN_TEST(test_sort_one_int_el_array_quick);
    RUN_TEST(test_sort_one_string_el_array_quick);
    RUN_TEST(test_sort_three_int_el_array_quick);
    RUN_TEST(test_sort_three_string_el_array_quick);
    return UNITY_END();
}


// Comparator Def, for int, char and string type ;

int comparint(const void *first, const void *second)
{
    int *firstint = (int *)first;
    int *secondint = (int *)second;
    if (*firstint > *secondint)
    {
        return 1;
    }
    else if (*firstint < *secondint)
    {
        return -1;
    }
    else
    {
        return 0;
    }
}



int comparstring(const void *first, const void *second)
{
    char *firststring = (char *)first;
    char *secondstring = (char *)second;
    return strcmp(firststring, secondstring);
}
