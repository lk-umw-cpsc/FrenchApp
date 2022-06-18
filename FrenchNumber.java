public class FrenchNumber {
    private int number;
    private String asDigits;
    private String asWords;

    public FrenchNumber(FrenchNumberPiece piece) {
        number = piece.getNumber();
        asDigits = Integer.toString(number);
        asWords = piece.getWords();
    }

    public void update(FrenchNumberPiece piece) {
        if (piece == null) {
            asDigits += " 000";
            return;
        }
        asWords += " " + piece.getWords();
        int n = piece.getNumber();

        number *= 1000;
        number += n;
        if (n > 0) {
            asDigits += String.format(" %03d", n);
        } else {
            asDigits += n;
        }
    }

    public String getDigitsString() {
        return asDigits;
    }

    public String getWordsString() {
        return asWords;
    }
}
