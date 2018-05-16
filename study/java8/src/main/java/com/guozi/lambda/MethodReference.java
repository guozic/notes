package com.guozi.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @author guozi
 *
 * 1.指向静态方法的方法引用
 * 2. 指向现有对象的实例方法的方法引用
 * @date 2018-05-15 15:06
 */
public class MethodReference {

    public static List<Integer> findNumbers(List<Integer> numbers, Predicate<Integer> filter) {
        List<Integer> numbersFound = numbers
                .stream()
                .filter(filter)
                .collect(toList());

        return numbersFound;
    }

    public static boolean multipleOf3(Integer number) {
        return (number % 3) == 0;
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 3, 6, 8, 9, 12, 14, 15);

        //引用这个类中的 静态 方法
        List<Integer> arr = findNumbers(numbers, MethodReference::multipleOf3);

        //结果
        arr.forEach(i -> System.out.println(i));

    }
}
