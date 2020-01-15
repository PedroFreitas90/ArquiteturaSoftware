package presentation;

public interface KeyboardInputs {
    int getNif(UILanguage lang);
    String getNome(UILanguage lang);
    String getEmail(UILanguage lang);
    String getPassword(UILanguage lang);
    double getSaldo(UILanguage lang);
    String getString();
    int getInt();
    double getDouble();
    boolean getBoolean();
    int chooseOption(int defaultOption, int maxOption);
}
