import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

class Fraction {
    public int up, bot;

    public Fraction(int up, int bot) throws Exception {
        if (bot == 0) {
            System.out.println("Ошибка. Деление на 0.");
            System.exit(1);
        } else {
            this.up= up;
            this.bot= bot;
        }
    }

    public static String sum(Fraction frac1, Fraction frac2) {
        return (frac1.up * frac2.bot + frac2.up * frac1.bot) + "/" + (frac1.bot * frac2.bot);
    }
    public static String ded(Fraction frac1, Fraction frac2) {
        return (frac1.up+"/"+ frac1.bot+" + -"+ frac2.up+ "/"+ frac2.bot);
    }
    public static String mult(Fraction frac1, Fraction frac2) {
        return (frac1.up * frac2.up) + "/" + (frac1.bot * frac2.bot);
    }
    public static String div(Fraction frac1, Fraction frac2) {
        return frac1.up+"/"+frac1.bot+ " * "+ frac2.bot+"/"+ frac2.up;
    }


}

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Введите выражение в одну строку. Знаки нужно обособлять пробелами. Деление записывать через знак : (двоеточие). ");
        Scanner sc = new Scanner(System.in);
        StringBuilder text = new StringBuilder(String.valueOf(sc.nextLine()));
        if(text.toString().equals("quit")){
            System.out.println("Завершение работы");
            System.exit(1);
        }

        Matcher symbols = Pattern.compile(" - | \\+ | \\* | : ").matcher(text);
        while (symbols.find()){
            text = new StringBuilder("("+ text+ ")");
        }
        ArrayList<Integer> OpenBrackets = new ArrayList<>();
        ArrayList<Integer> CloseBrackets = new ArrayList<>();
        Matcher open = Pattern.compile("\\(").matcher(text);
        Matcher close = Pattern.compile("\\)").matcher(text);

        int cntt = 0;
        while (open.find()) {
            OpenBrackets.add(open.start());
            cntt++;
        }
        int cntc=0;
        while (close.find()){
            CloseBrackets.add(close.start());
            cntc++;
        }
        if(cntt!= cntc){
            System.out.println("Ошибка. Некорректное выражение.");
            System.exit(1);
        }

        for (int i = 0; i < cntt; i++) {
            open.reset(text); close.reset(text);
            String[] splitText = text.toString().split("");
            int cnt=0; OpenBrackets.clear(); CloseBrackets.clear();
            while (open.find() && close.find()) {
                OpenBrackets.add(open.start());
                CloseBrackets.add(close.start());
                cnt++;
            }

            int closebr = CloseBrackets.get(0);
            for (int k = 0; k < cnt; k++) {
                int openbr = OpenBrackets.get(k);
                StringBuilder inBrackets = new StringBuilder();

                for (int j = openbr + 1; j < closebr; j++) {
                    inBrackets.append(splitText[j]);
                }

                Matcher lastOpen = Pattern.compile("\\(").matcher(inBrackets.toString());
                Matcher div = Pattern.compile("[0-9]+/[0-9]+ : [0-9]+/[0-9]+|-[0-9]+/[0-9]+ : [0-9]+/[0-9]+|[0-9]+/[0-9]+ : -[0-9]+/[0-9]+|-[0-9]+/[0-9]+ : -[0-9]+/[0-9]+").matcher(inBrackets.toString());
                Matcher mult = Pattern.compile("[0-9]+/[0-9]+ \\* [0-9]+/[0-9]|-[0-9]+/[0-9]+ \\* [0-9]+/[0-9]+|[0-9]+/[0-9]+ \\* -[0-9]+/[0-9]+|-[0-9]+/[0-9]+ \\* -[0-9]+/[0-9]+").matcher(inBrackets.toString());
                Matcher sum = Pattern.compile("[0-9]+/[0-9]+ \\+ [0-9]+/[0-9]|-[0-9]+/[0-9]+ \\+ [0-9]+/[0-9]+|[0-9]+/[0-9]+ \\+ -[0-9]+/[0-9]+|-[0-9]+/[0-9]+ \\+ -[0-9]+/[0-9]+").matcher(inBrackets.toString());
                Matcher ded = Pattern.compile("[0-9]+/[0-9]+ - [0-9]+/[0-9]|-[0-9]+/[0-9]+ - [0-9]+/[0-9]+|[0-9]+/[0-9]+ - -[0-9]+/[0-9]+|-[0-9]+/[0-9]+ - -[0-9]+/[0-9]+").matcher(inBrackets.toString());

                while (ded.find()) {
                    int start = ded.start();
                    int end = ded.end();
                    String[] dedFrs = inBrackets.substring(start, end).split(" - ");

                    String[] dedFrc1 = dedFrs[0].split("/");
                    String[] dedFrc2 = dedFrs[1].split("/");

                    Fraction frac1 = new Fraction(Integer.parseInt(dedFrc1[0]), Integer.parseInt(dedFrc1[1]));
                    Fraction frac2 = new Fraction(Integer.parseInt(dedFrc2[0]), Integer.parseInt(dedFrc2[1]));
                    inBrackets.replace(start, end, Fraction.ded(frac1, frac2));
                    mult.reset(inBrackets.toString()); div.reset(inBrackets.toString()); ded.reset(inBrackets.toString()); sum.reset(inBrackets.toString());}

                    while (div.find()) {
                        int start = div.start();
                        int end = div.end();
                        String[] divFrs = inBrackets.substring(start, end).split(" : ");

                        String[] divFrc1 = divFrs[0].split("/");
                        String[] divFrc2 = divFrs[1].split("/");

                        Fraction frac1 = new Fraction(Integer.parseInt(divFrc1[0]), Integer.parseInt(divFrc1[1]));
                        Fraction frac2 = new Fraction(Integer.parseInt(divFrc2[0]), Integer.parseInt(divFrc2[1]));
                        inBrackets.replace(start, end, Fraction.div(frac1, frac2));
                        mult.reset(inBrackets.toString()); div.reset(inBrackets.toString()); ded.reset(inBrackets.toString()); sum.reset(inBrackets.toString());
                    }



                if (!(lastOpen.find() || inBrackets.length() == 0)) {

                    while (mult.find()) {
                        int start = mult.start();
                        int end = mult.end();
                        String[] multFrs = inBrackets.substring(start, end).split(" \\* ");

                        String[] mFrc1 = multFrs[0].split("/");
                        String[] mFrc2 = multFrs[1].split("/");

                        Fraction frac1 = new Fraction(Integer.parseInt(mFrc1[0]), Integer.parseInt(mFrc1[1]));
                        Fraction frac2 = new Fraction(Integer.parseInt(mFrc2[0]), Integer.parseInt(mFrc2[1]));

                        inBrackets.replace(start, end, Fraction.mult(frac1, frac2));
                        mult.reset(inBrackets.toString()); div.reset(inBrackets.toString()); ded.reset(inBrackets.toString()); sum.reset(inBrackets.toString());

                    }


                    while (sum.find()) {
                        int start = sum.start();
                        int end = sum.end();
                        String[] sumFrs = inBrackets.substring(start, end).split(" \\+ ");

                        String[] sumFrc1 = sumFrs[0].split("/");
                        String[] sumFrc2 = sumFrs[1].split("/");

                        Fraction frac1 = new Fraction(Integer.parseInt(sumFrc1[0]), Integer.parseInt(sumFrc1[1]));
                        Fraction frac2 = new Fraction(Integer.parseInt(sumFrc2[0]), Integer.parseInt(sumFrc2[1]));

                        inBrackets.replace(start, end, Fraction.sum(frac1, frac2));
                        mult.reset(inBrackets.toString()); div.reset(inBrackets.toString()); ded.reset(inBrackets.toString()); sum.reset(inBrackets.toString());
                    }

                    text.replace(openbr, closebr+1, inBrackets.toString());
            }
        }}
            System.out.println(text);
    }}