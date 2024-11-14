#include "../../Unity/unity.h"
#include "edit_distance.h"
#include "edit_distance_dyn.h"
#include <string.h>
#include <stdlib.h>

/**
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 10/04/2024;
 * @details unit-testing edit_distance and his dynamic version algorithm;
 */
void setUp(void)
{
}

void tearDown(void)
{
}

void test_empty_words()
{
    size_t len_a = 0;
    size_t len_b = 0;
    char a[] = "";
    char b[] = "";
    TEST_ASSERT_TRUE(edit_distance(a, b) == 0);
}

void test_one_word_one_empty()
{
    char s1[] = "casa";
    char s2[] = "";
    TEST_ASSERT_TRUE(edit_distance(s1, s2) == strlen(s1));
}

void test_one_empty_one_word()
{
    char s1[] = "";
    char s2[] = "cassa";
    TEST_ASSERT_TRUE(edit_distance(s1, s2) == strlen(s2));
}

void test_different_words()
{
    char s1[] = "casa";
    char s2[] = "cassa";

    char s3[] = "casa";
    char s4[] = "cara";

    char s5[] = "tassa";
    char s6[] = "passato";

    TEST_ASSERT_EQUAL_INT(edit_distance(s1, s2), 1);
    TEST_ASSERT_EQUAL_INT(edit_distance(s3, s4), 2);
    TEST_ASSERT_EQUAL_INT(edit_distance(s5, s6), 4);
}

void test_equal_words()
{
    char s1[] = "pioppo";
    char s2[] = "pioppo";

    char s3[] = "maria";
    char s4[] = "maria";

    char s5[] = "gioco";
    char s6[] = "gioco";

    TEST_ASSERT_EQUAL_INT(edit_distance(s1, s2), 0);
    TEST_ASSERT_EQUAL_INT(edit_distance(s3, s4), 0);
    TEST_ASSERT_EQUAL_INT(edit_distance(s5, s6), 0);
}

void test_empty_words_dyn()
{
    size_t len_a = 0;
    size_t len_b = 0;
    char a[] = "";
    char b[] = "";
    TEST_ASSERT_TRUE(edit_distance_dyn(a, b) == 0);
}

void test_one_word_one_empty_dyn()
{
    char s1[] = "casa";
    char s2[] = "";
    TEST_ASSERT_TRUE(edit_distance_dyn(s1, s2) == strlen(s1));
}

void test_one_empty_one_word_dyn()
{
    char s1[] = "";
    char s2[] = "cassa";
    TEST_ASSERT_TRUE(edit_distance_dyn(s1, s2) == strlen(s2));
}

void test_different_words_dyn()
{
    char s1[] = "casa";
    char s2[] = "cassa";

    char s3[] = "casa";
    char s4[] = "cara";

    char s5[] = "tassa";
    char s6[] = "passato";

    TEST_ASSERT_EQUAL_INT(edit_distance_dyn(s1, s2), 1);
    TEST_ASSERT_EQUAL_INT(edit_distance_dyn(s3, s4), 2);
    TEST_ASSERT_EQUAL_INT(edit_distance_dyn(s5, s6), 4);
}

void test_equal_words_dyn()
{
    char s1[] = "pioppo";
    char s2[] = "pioppo";

    char s3[] = "maria";
    char s4[] = "maria";

    char s5[] = "gioco";
    char s6[] = "gioco";

    TEST_ASSERT_EQUAL_INT(edit_distance_dyn(s1, s2), 0);
    TEST_ASSERT_EQUAL_INT(edit_distance_dyn(s3, s4), 0);
    TEST_ASSERT_EQUAL_INT(edit_distance_dyn(s5, s6), 0);
}

int main()
{
    UNITY_BEGIN();
    RUN_TEST(test_empty_words);
    RUN_TEST(test_one_word_one_empty);
    RUN_TEST(test_one_empty_one_word);
    RUN_TEST(test_different_words);
    RUN_TEST(test_equal_words);
    RUN_TEST(test_empty_words_dyn);
    RUN_TEST(test_one_word_one_empty_dyn);
    RUN_TEST(test_one_empty_one_word_dyn);
    RUN_TEST(test_different_words_dyn);
    RUN_TEST(test_equal_words_dyn);
    return UNITY_END();
}
