package client;

public class EscapeSequences {
    private static final String ESCAPE = "\u001b[";

    public static final String LOGO = "\uD83D\uDC36";
    public static final String GREEN = ESCAPE + "32m";
    public static final String BLUE = ESCAPE + "34m";
    public static final String RED = ESCAPE + "31m";
    public static final String RESET = ESCAPE + "0m";
    public static final String BG_LIGHT = ESCAPE + "47m";
    public static final String BG_DARK = ESCAPE + "44m";

}