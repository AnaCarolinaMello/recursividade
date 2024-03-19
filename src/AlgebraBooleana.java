import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AlgebraBooleana {
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

            if (v2 == 't') e.type = "not";
            else if (v2 == 'd') e.type = "and";
            else if (v2 == 'r') e.type = "or";

            e.parent = executor;
            executor.subValue.add(e);
        }
        else if (v == '1') executor.value.add(1); 
        else if (v == '0') executor.value.add(0);

        if (position == value.length() - 1) return executor;
        return getAlgebraBooleanaP(value, position + 1, e != null ? e : executor, e != null && e.parent != null ? e.parent : parent);
    }

    public static class Executor {
        public List<Integer> value = new ArrayList<Integer>();
        public String type;
        public List<Executor> subValue = new ArrayList<Executor>();
        public Executor parent;
        public int id;
        private static int count = 0;

        public Executor() {
            id = ++count;
        }

        public List<Integer> execute() {
            List<Integer> resultList = new ArrayList<Integer>();

            if (subValue.size() != 0) {
                for (Executor childValue : subValue) {
                    value.addAll(childValue.execute());
                }
            }

            if (value.size() == 0) return new ArrayList<>();
            else if (type == null) return value;

            boolean result = type.equals("and") ? true : false;

            for (int i = 0; i < value.size(); i++) {
                if (type.equals("and") && value.get(i) != 1) {
                    result = false;
                    break;
                } else if (type.equals("or") && value.get(i) == 1) {
                    result = true;
                    break;
                } else if (type.equals("not")) {
                    result = value.get(i) == 0;
                    break;
                }
            }
            resultList.add(result ? 1 : 0);
            return resultList;
        }

        public void setValue(Integer v) {
            value.add(v);
        }

        @Override
        public String toString() {
            return "Id: " + id + " Value:" + value.toString() + " Type:" + type + "\n\tSub:" + (subValue != null ? subValue.toString() : "");
        }
    }
}