package presentation;

import presentation.portuguese.PortuguesUILanguage;

public class UILanguageFactory {
    private UILanguage lang;

    public UILanguageFactory(String lang) {
        if (lang.equals("PT"))
            this.lang = new PortuguesUILanguage();
    }

    public GetAssetsUILanguage getAssetsUILanguage() {
        return this.lang;
    }

    public GetCFDsUILanguage getCFDsUILanguage() {
        return this.lang;
    }

    public SetCFDUILanguage getSetCFDUILanguage() {
        return this.lang;
    }

    public UILanguage getLang() {
        return this.lang;
    }
}
