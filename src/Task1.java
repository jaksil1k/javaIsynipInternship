import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task1 {

    static class Paragraph {
        private String words;
        private int amountOfWords;

        public Paragraph(String words, int amountOfWords){
            this.words = words;
            this.amountOfWords = amountOfWords;
        }

        public void setWords (String words){
            this.words = words;
        }

        public int getAmountOfWords() {
            return amountOfWords;
        }

        public String getWords() {
            return words;
        }

        public void setAmountOfWords(int amountOfWords) {
            this.amountOfWords = amountOfWords;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Paragraph> paragraphs = new ArrayList<>();
        StringBuilder text = new StringBuilder();
        int cnt = 0;
        while (sc.hasNext()) {
            String inputData = sc.nextLine();
            for (int i = 0; i < inputData.length(); ++i) {
                char curChar = inputData.charAt(i);
                    if (curChar == ' ') {
                        cnt++;
                    }
                    text.append(curChar);
            }
            paragraphs.add(new Paragraph(text.toString(),++cnt));
        }
        for (Paragraph paragraph : paragraphs) {
            System.out.println(paragraph.getWords() + '\n' + paragraph.getAmountOfWords());
        }
    }

}
