package edu.montana.csci.csci440.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BagEquivalence {

    public static void main(String[] args) {
        List<String> a = Arrays.asList("A", "B", "C", "A");
        List<String> b = Arrays.asList("A", "C", "A", "B", "A");
        List<String> c = Arrays.asList("A", "C", "A", "B");

        if (bagEquivalent(a, b)) {
            System.out.println("a & b are bag equivalent!");
        } else {
            System.out.println("a & b are not bag equivalent");
        }

        if (bagEquivalent(a, c)) {
            System.out.println("a & c are bag equivalent!");
        } else {
            System.out.println("a & c are not bag equivalent");
        }
    }

    private static boolean bagEquivalent(List<String> a, List<String> b) {
        if (a.size() != b.size()) {
            return false;
        }
        Map<String, Integer> mapA = getMapCount(a);
        Map<String, Integer> mapB = getMapCount(b);
        return mapA.equals(mapB);
    }

    private static Map<String, Integer> getMapCount(List<String> a) {
        Map<String, Integer> count = new HashMap<>();
        for (String str : a) {
            Integer integer = count.computeIfAbsent(str, s -> 0);
            integer++;
            count.put(str, integer);
        }
        return count;
    }

}
