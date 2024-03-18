import java.util.Scanner;

public class X {
    // public static void main(String[] args) {
    //     Scanner scanner = new Scanner(System.in);
    //     String s = scanner.nextLine();
    //     while (!s.equals("FIM")) {
    //     String isVogal = vogais(s)? "SIM " : "NAO ";
    //     String isConsoante = consoante(s)? "SIM " : "NAO ";
    //     String isInteiro = inteiro(s)? "SIM " : "NAO ";
    //     String isReal = real(s)? "SIM" : "NAO";
    //     System.out.println(isVogal + isConsoante + isInteiro + isReal);
    //     s = scanner.nextLine();
    //     }
    //     scanner.close();
    // }

    public static boolean vogais(String valor) {
        return vogaisP(valor, 0);
    }

    private static boolean vogaisP(String valor, int posicao) {
        boolean isVogal = isVogal(valor.charAt(posicao));
        if (!isVogal) return false;
        else if (posicao == valor.length() - 1) return isVogal;
        return vogaisP(valor, posicao + 1);
    }

    private static boolean isVogal(char v) {
        String s = String.valueOf(v);
        return s.equalsIgnoreCase("a")
            || s.equalsIgnoreCase("e")
            || s.equalsIgnoreCase("i")
            || s.equalsIgnoreCase("o")
            || s.equalsIgnoreCase("u");
    }

    public static boolean consoante(String valor) {
        return consoanteP(valor, 0);
    }

    private static boolean consoanteP(String valor, int posicao) {
        char v = valor.charAt(posicao);
        boolean isVogal = isVogal(v);
        if (isVogal || isNumber(v)) return false;
        if (posicao == valor.length() - 1) return !isVogal;
        return consoanteP(valor, posicao + 1);
    }

    public static boolean inteiro(String valor) {
        return inteiroP(valor, 0);
    }

    private static boolean inteiroP(String valor, int posicao) {
        boolean isNumber = isNumber(valor.charAt(posicao));
    
        if (!isNumber) return false;
        else if (posicao == valor.length() - 1) return isNumber;
        return inteiroP(valor, posicao + 1);
    }

    private static boolean isNumber(char v) {
        return v >= '0' && v <= '9';
    }

    public static boolean real(String valor) {
        return realP(valor, 0, false);
    }

    private static boolean realP(String valor, int posicao, boolean foundSeparator) {
        char v = valor.charAt(posicao);
        boolean foundNewSeparator = v == '.' || v == ',';

        if ((foundNewSeparator && foundSeparator) || (!isNumber(v) && !foundNewSeparator)) return false;
        else if (posicao == valor.length() - 1) return isNumber(v) || foundNewSeparator;
        return realP(valor, posicao + 1, foundNewSeparator || foundSeparator);
    }
}
