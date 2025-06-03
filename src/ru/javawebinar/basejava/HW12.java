package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HW12 {

    public static void main(String[] args) {
        int[] values1 = {9, 8};
        int[] values2 = {1, 2, 3, 3, 2, 3};
        int[] values3 = {1, 3, 9, 8};

        System.out.println("\n ---- minValues ---- \n");
        System.out.println(minValues(values1));
        System.out.println(minValues(values2));
        System.out.println(minValues(values3));

        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(6, 7, 8, 9));

        System.out.println("\n ---- oddOrEven ---- \n");
        System.out.println(oddOrEven(new ArrayList<>(list1)));
        System.out.println(oddOrEven(new ArrayList<>(list2)));


        System.out.println("\n ---- oddOrEvenStream ---- \n");
        System.out.println(oddOrEvenStream(list1));
        System.out.println(oddOrEvenStream(list2));
    }

    public static int minValues(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (s, digit) -> s * 10 + digit);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        List<Integer> even = new ArrayList<>();
        List<Integer> odd = new ArrayList<>();
        int s = 0;
        for (Integer i : integers) {
            s += i;
            if (i % 2 == 0)
                even.add(i);
            else
                odd.add(i);
        }
        integers.removeAll(s % 2 == 0 ? even : odd);
        return integers;
    }

    public static List<Integer> oddOrEvenStream(List<Integer> integers) {
        int s = integers.stream().reduce(0, Integer::sum);
        return integers.stream().filter(i -> (s % 2 == 0) == (i % 2 != 0)).toList();
    }

}
