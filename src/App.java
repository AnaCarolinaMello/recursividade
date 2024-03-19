import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        while (!s.equals("0")) {
            Executor e = getAlgebraBooleana(s);
            System.out.println(e.execute().get(0));
            s = scanner.nextLine();
        }
        scanner.close();
    }
    // public static void main(String[] args) throws Exception {
    //     // System.out.println(cesar("Ada"));
    //     // System.out.println(palindromo("aia") ? "SIM" : "NAO");
    //     // System.out.println(aleatorio("rasar"));
    //     // int[] a = {7, 5, 0, 7};
    //     // System.out.println(findIndex(a, 0));
    //     // System.out.println(calCoseno());
    //         Executor e = getAlgebraBooleana("3 0 1 0 or(and(A , B , C) , and(A , not(B) , C) , and(not(A) , not(B) , C) , and(not(A) , not(B) , not(C)))");
    //         System.out.println(e.toString());
    //         System.out.println(e.execute().get(0));
    // }

    public static int findIndex(int[] vet, int n) {
        return findIndexP(vet, n, vet.length - 1);
    }

    private static int findIndexP(int[] vet, int n, int position) {
        if (position < 0) return -1;
        else if (vet[position] == n) return position;
        return findIndexP(vet, n, position - 1);
    }

    public static double calCoseno() {
        return  1 + calCosenoP(2, -1);
    }

    private static double calCosenoP(int n, int sign) {
        if (n == 12) return (1 / fatorial(n)) * sign;
        return (1 / fatorial(n)) * sign + calCosenoP(n + 2, sign * -1);
    }

    private static double fatorial(int n) {
        if (n == 1) return n;
        return n * fatorial(n - 1);
    }

    public static String cesar(String valor) { 
        return cesarP(valor, 0); 
    } 

    private static String cesarP(String valor, int position) {
        char c = valor.charAt(position);
        int n = ((int) c) + 3;
        char v = (char)n;

        if (position == valor.length() - 1) return String.valueOf(v);
        return String.valueOf(v) + cesarP(valor, position + 1);
    } 

    public static boolean palindromo(String valor) {
        return palindromoP(valor, 0);
    }

    private static boolean palindromoP(String valor, int position) {
        boolean v = valor.charAt(valor.length() - 1 - position) == valor.charAt(position); 

        if (valor.length() / 2 == position) return v;
        return v == palindromoP(valor, position + 1); 

    }

    public static String aleatorio(String valor) {
        Random generator = new Random();
        generator.setSeed(4);
        // char letter = (char)('a' + Math.abs(generator.nextInt() % 26));
        // char letter2 = (char)(int)('a' + Math.abs(generator.nextInt() % 26));
        
        int a = (int)'a';
        int z = (int)'z';
        char letter = (char)(generator.nextInt(z - a) + a);
        char letter2 = (char)(generator.nextInt(z - a) + a);
        System.out.println(letter + " " + letter2);
        return aleatorioP(valor, 0, letter, letter2);
    }

    private static String aleatorioP(String valor, int position, char a, char b) {
        char v = valor.charAt(position);
        if (v == a) v = b;

        if (position == valor.length() - 1) return String.valueOf(v);
        return String.valueOf(v) + aleatorioP(valor, position + 1, a, b);
    }

    public static Executor getAlgebraBooleana(String valor) {
        String[] valorArray = valor.split(" ");
        int nOperacoes = Integer.parseInt(valorArray[0]);
        valor = valor.replaceAll(valorArray[0], "");
        int[] numbers = new int[nOperacoes];
        for (int i = 0; i < nOperacoes; i++) {
            numbers[i] = Integer.parseInt(valorArray[i + 1]);
            valor = valor.replaceAll(String.valueOf(numbers[i]), "");
        }

        valor = replaceValues(valor, 0, numbers);
        String newValue = valor;

        for (int i : numbers) {
            newValue = newValue.replaceAll(String.valueOf(i), "");
        }
        List<String> expressions = Arrays.asList(
            newValue
                .replaceAll("\\(", " ")
                .replaceAll("\\)", " ")
                .replaceAll(",", " ")
                .split(" ")
        );
        expressions = expressions.stream()
            .filter(value -> !value.isEmpty())
            .collect(Collectors.toList());

        valor = valor.replaceAll(" ", "").replaceFirst(expressions.get(0),"");
        Executor e = new Executor();
        e.type = expressions.get(0);
        getAlgebraBooleanaP(valor, 0, e, e);
        return e;
    }

    private static String replaceValues(String valor, int position, int[] numbers) {
        char v = valor.charAt(position);

        if (v == 'A') v = Character.forDigit(numbers[0], 10);
        else if (v == 'B') v = Character.forDigit(numbers[1], 10);
        else if (v == 'C') v = Character.forDigit(numbers[2], 10);

        if (position == valor.length() - 1) return String.valueOf(v);
        return v + replaceValues(valor, position + 1, numbers);
    }

    private static Executor getAlgebraBooleanaP(String value, int position, Executor executor, Executor parent) {
        char v = value.charAt(position);

        Executor e = null;
        if (v == ')') e = parent;
        else if (v == '(' && position != 0) {
            e = new Executor();
            char v2 = value.charAt(position - 1);
            char v3;

            if (v2 == 't') e.type = "not";
            else if (v2 == 'd') e.type = "and";
            else if (v2 == 'r') e.type = "or";

            if (v2 == 't' || v2 == 'd') v3 = value.charAt(position - 4);
            else v3 = value.charAt(position - 3);

            e.parent = executor;
            executor.subValue.add(e);
        }
        else if (v == '1') executor.value.add(1); 
        else if (v == '0') executor.value.add(0);

        if (position == value.length() - 1) return executor;
        return getAlgebraBooleanaP(value, position + 1, e != null ? e : executor, e != null && e.parent != null ? e.parent : parent);
    }

    public static void algebra(String word) {
        String newWord = word.replaceAll(" ", "");
        String quant = newWord.charAt(0) + "";
        newWord = replaceLetters(newWord, Integer.parseInt(quant));

        System.out.println(test(newWord));
    }

    public static char test(String word) {
        String expression;
        String result = "";
        while (word.length() >= 4) {
            expression = word.substring(word.lastIndexOf('(') - 2);
            if (expression.charAt(1) == 'd' || expression.charAt(1) == 't') {
                expression = word.substring(word.lastIndexOf('(') - 3);
            }
            if (expression.length() < 1)
                expression = word;
            if (expression.charAt(2) == 'd') {
                if (!expression.contains(")")) {
                    result = and(expression + result);
                } else {
                    result += and(expression);
                }
                word = word.substring(0, word.lastIndexOf(expression));
            } else if (expression.charAt(0) == 'o') {
                if (!expression.contains(")")) {
                    result = or(expression + result);
                } else {
                    result += or(expression);
                }
                word = word.substring(0, word.lastIndexOf(expression));
            } else if (expression.charAt(2) == 't') {
                if (!expression.contains(")")) {
                    result = not(expression + result);
                } else {
                    result += not(expression);
                }
                word = word.substring(0, word.lastIndexOf(expression));
            }
            if (!word.contains(")")) {
                word += result;
            }
        }

        return word.charAt(0);
    }

    public static String and(String word) {
        return !word.contains("0") ? "1" : "0";
    }

    public static String or(String word) {
        return word.contains("1") ? "1" : "0";
    }

    public static String not(String word) {
        return word.contains("0") ? "1" : "0";
    }

    public static String replaceLetters(String word, int quant) {
        String newWord = "";
        for (int i = 0; i < quant; i++) {
            char charToReplace = (char) (65 + i);
            if (newWord == "") {
                newWord = word.replaceAll(charToReplace + "", word.charAt(i + 1) + "");
            } else {
                newWord = newWord.replaceAll(charToReplace + "", newWord.charAt(i + 1) + "");
            }
        }
        return newWord.substring(quant + 1);
    }
}
