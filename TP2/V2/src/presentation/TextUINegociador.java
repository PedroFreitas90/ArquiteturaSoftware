package presentation;

import business.ativos.Ativo;
import business.CFD;
import business.ativos.AtivoConsts;
import business.exceptions.CFDNaoExisteException;
import business.exceptions.NegociadorNaoExisteException;
import business.exceptions.NegociadorNaoPossuiSaldoSuficienteException;
import business.facade.FacadeNegociador;

import java.util.*;
import java.util.stream.Collectors;

public class TextUINegociador implements UINegociador {
    private final static String MENU_SEPARATOR = "-";

    private FacadeNegociador facade;
    private Integer nif;
    private UILanguageFactory factory;
    private KeyboardInputs inputs;

    public TextUINegociador(FacadeNegociador f, String lang) {
        this.facade = f;
        this.nif = null;
        this.factory = new UILanguageFactory(lang);
        this.inputs = new Inputs();
    }

    public void start() {
        showMenuInicial();
    }

    private static String listOptions(List<String> options) {
        int maxSize = 0;
        StringBuilder opsString = new StringBuilder();
        for(int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            opsString.append(i).append(") ").append(option).append("\n");
            maxSize = Math.max(option.length(), maxSize);
        }
        int n = String.valueOf(options.size()).length();
        String separator = MENU_SEPARATOR.repeat(maxSize + 2 + n) + "\n";
        return separator + opsString + separator;
    }

    private void showMenuInicial() {
        List<String> options = this.factory.getLang().getInitialMenuOptions();
        System.out.print(listOptions(options));
        int choice = inputs.chooseOption(2, options.size() - 1);
        switch (choice) {
            case 0: showMenuLogin();
                    break;
            case 1: showMenuRegistar();
                    break;
            default: showAdeus();
                    break;
        }
    }

    private void showMenuLogin() {
        UILanguage lang = this.factory.getLang();
        int nif = inputs.getNif(lang);
        String pass = inputs.getPassword(lang);
        boolean loggedIn = this.facade.verificarCredenciais(nif, pass);
        if (loggedIn) {
            this.nif = nif;
            showPaginaInicial();
        }
        else {
            System.out.println(lang.getNonExistentCredentials());
            showMenuRegistar();
        }
    }

    private void showPaginaInicial() {
        UILanguage lang = this.factory.getLang();
        List<String> options = this.factory.getLang().getInitialPageOptions();
        System.out.println(lang.getInicialPage());
        System.out.print(listOptions(options));
        int choice = inputs.chooseOption(4, options.size() - 1);
        switch (choice) {
            case 0: showEstabelecerCFD();
                    break;
            case 1: showEncerrarCFD();
                    break;
            case 2: showConsultarCFDs();
                    break;
            case 3: showConsultarAtivos();
                    break;
            case 4: showAdicionarSaldo();
                    break;
            case 5: showSubscreverAtivo();
                break;
            case 6: showAtivosSubscritos();
                break;
            default: this.nif = null;
                    showMenuInicial();
                    break;
        }
    }

    private List<String> ativosAsString(List<Ativo> ativos){
        return ativos.stream().map(Ativo::toString).collect(Collectors.toList());
    }

    private void showSubscreverAtivo(){
        UILanguage lang = this.factory.getLang();
        System.out.println(lang.getChooseAtivoToFollow());
        int typeOfAtivo = chooseTypeOfAtivo();
        List<Ativo> ativos = this.facade.getAtivos(AtivoConsts.ALL_ATIVOS[typeOfAtivo]);
        List<String> ativosAsString = ativosAsString(ativos);
        System.out.println(lang.getInsertAssetToInvest());
        System.out.print(listOptions(ativosAsString));

        int escolha = inputs.chooseOption(ativosAsString.size(), ativosAsString.size() - 1);
        this.facade.seguirAtivo(this.nif,ativos.get(escolha).getId());
        showPaginaInicial();
    }

    private void showAtivosSubscritos(){
        UILanguage lang = this.factory.getLang();
        List<Ativo> ativos = this.facade.getAtivosSubscritos(this.nif);

        if (ativos != null){
            System.out.println(lang.getSubscribedAtivos());
            String delimiter = "*".repeat(40) + "\n";
            ativos.forEach((ativo) -> { System.out.println(delimiter + ativo.toString() + "\n" + delimiter);});
        }
        else
            System.out.println(lang.getNoSubscriber());
        showPaginaInicial();
    }

    private void showAdicionarSaldo() {
        UILanguage lang = this.factory.getLang();
        System.out.println(lang.getInsertValue());
        double saldo = inputs.getDouble();
        double saldoFinal;
        try {
            saldoFinal = this.facade.atualizarSaldo(this.nif, saldo);
        }
        catch(NegociadorNaoExisteException e) {
            saldoFinal=0;
        }
        System.out.println(lang.getSaldo() + saldoFinal);
        showPaginaInicial();
    }

    private void showAllAtivos(){
        GetAssetsUILanguage lang = this.factory.getAssetsUILanguage();
        System.out.println(lang.getInsertTypeofAsset());
        List<String> options = lang.getTypesOfAssets();
        System.out.println(listOptions(options));
        int tipo = inputs.chooseOption(options.size(), options.size() - 1);
        List<Ativo> ativos = this.facade.getAtivos(AtivoConsts.ALL_ATIVOS[tipo]);
        ativos.forEach(a -> System.out.println(a.toString())); // update to customize according to lang
    }

    private void showConsultarAtivos() {
        showAllAtivos();
        showPaginaInicial();
    }

    private void showCFDsAbertos(){
        GetCFDsUILanguage lang = this.factory.getCFDsUILanguage();
        List<CFD> cfds;
        try {
            cfds = this.facade.getCFDs(this.nif);
        } catch (NegociadorNaoExisteException e) {
            cfds = new ArrayList<>();
        }
        Map<CFD, Double> valorAtual = new HashMap<>();
        cfds.forEach(c -> valorAtual.put(c,this.facade.getValorAtualCFD(c.getId())));

        for(Map.Entry<CFD, Double> en : valorAtual.entrySet()) {
            CFD cfd = en.getKey();
            List<String> format = new ArrayList<>(Arrays.asList("ID | " + cfd.getId(),
                    lang.getTimestamp() + " | " + cfd.getData().toString(),
                    lang.getAsset() + " | " + cfd.getIdAtivo(),
                    lang.getUnitsOfAsset() + " | " + cfd.getUnidadesDeAtivo(),
                    lang.getInvestedValue() + " | " + cfd.getValorInvestido() + "€",
                    lang.getInvestedValueInCaseOfRefund() + " | " + en.getValue() + "€",
                    lang.getTipoCFD() + " | " + cfd.getTipo()));
            String delimiter = "*".repeat(format.get(format.size()-1).length());
            System.out.println(delimiter);
            format.forEach(System.out::println);
            System.out.println(delimiter);
        }
    }

    private void showConsultarCFDs() {
        showCFDsAbertos();
        showPaginaInicial();
    }

    private void showEncerrarCFD() {
        UILanguage lang = this.factory.getLang();
        System.out.println(lang.getInsertCFDtoEnd());
        this.showCFDsAbertos();
        int id = inputs.getInt();
        try {
            double valorCFD = this.facade.fecharCFD(id);
            double valorFinal = this.facade.getSaldo(this.nif);
            String delimiter = "*".repeat(60);
            System.out.println(delimiter);
            System.out.println(lang.getCFDClosed() + valorCFD + lang.getEuro());
            System.out.println(lang.getSaldo() + valorFinal + lang.getEuro());
            System.out.println(delimiter);
        }
        catch (CFDNaoExisteException e){
            System.out.println(e.toString());
        }
        showPaginaInicial();
    }

    private int chooseTypeOfAtivo(){
        List<String> options = this.factory.getAssetsUILanguage().getTypesOfAssets();
        System.out.print(listOptions(options)); // print types of assets
        int typeOfAtivo = inputs.chooseOption(options.size(), options.size() - 1);
        return typeOfAtivo;
    }
    private int chooseAtivo(List<Ativo> ativos,SetCFDUILanguage lang, int typeOfAtivo){
        List<String> ativosAsString = ativosAsString(ativos);
        System.out.println(lang.getInsertAssetToInvest());
        System.out.print(listOptions(ativosAsString));

        int ativo = inputs.chooseOption(ativos.size(), ativos.size() - 1);
        return ativo;
    }

    private String choose_tipoAtivo(SetCFDUILanguage lang){
        List<String> tiposCFD = lang.getTypesOfCFD();
        System.out.println(lang.getChooseTypeOfCFD());
        System.out.println(listOptions(tiposCFD));
        int tipoCFD = inputs.chooseOption(0, 1);
        String tipo = tiposCFD.get(tipoCFD);
        return tipo;
    }

    private double getUnidades(SetCFDUILanguage lang){
        double saldo = this.facade.getSaldo(this.nif);
        System.out.println(lang.getInsertUnitsToInvest() + "\n" + lang.getYouHave() + saldo + lang.getToInvest());
        double unidades = inputs.getDouble();
        return unidades;
    }


    private void showEstabelecerCFD() {
        SetCFDUILanguage lang = this.factory.getSetCFDUILanguage();
        int typeOfAtivo = chooseTypeOfAtivo();
        List<Ativo> ativos = this.facade.getAtivos(AtivoConsts.ALL_ATIVOS[typeOfAtivo]);
        String tipo = choose_tipoAtivo(lang);
        int ativo = chooseAtivo(ativos,lang,typeOfAtivo);
        double unidades = getUnidades(lang);
        double investimento = unidades * ativos.get(ativo).getValorPorUnidade();
        Double stopLossValue = definirLimite("Stop Loss", investimento);
        Double takeProfitValue = definirLimite("Take Profit", investimento);
        try {
            CFD cfd = this.facade.registarCFD(ativos.get(ativo).getId(), this.nif, unidades, stopLossValue, takeProfitValue, tipo);
            System.out.println(lang.getCFDEstablishedWithSuccess());
            System.out.println(cfd.toString());
        } catch (NegociadorNaoExisteException e) {
            e.printStackTrace();
        } catch (NegociadorNaoPossuiSaldoSuficienteException e) {
            System.out.println(e.toString());
        }
        showPaginaInicial();
    }

    private Double definirLimite(String nomeLimite, double investimentoBase) {
        SetCFDUILanguage lang = this.factory.getSetCFDUILanguage();
        System.out.println(lang.getWishToDefine() + nomeLimite + lang.getQuestionYesOrNo() + "\n" + lang.getYouWillInvest() + investimentoBase + "€");
        boolean queroLimite = inputs.getBoolean();
        Double result = null;
        if (queroLimite) {
            System.out.println(lang.getInsertValue());
            result = inputs.getDouble();
            if (nomeLimite.equals("Take Profit") && result < investimentoBase ||
                    nomeLimite.equals("Stop Loss") && result > investimentoBase)
                System.out.println(lang.getInvalidLimit());
        }
        return result;
    }

    private void nextMenu(UILanguage lang,boolean inserted,int nif){
        if (inserted) {
            System.out.println(lang.getNegociatorInserted());
            this.nif= nif;
            showPaginaInicial();
        }
        else{
            System.out.println(lang.getNegociatorNotInserted());
            showMenuRegistar();
        }
    }
    private void showMenuRegistar() {
        UILanguage lang = this.factory.getLang();
        int nif = inputs.getNif(lang);
        String nome = inputs.getNome(lang);
        String email = inputs.getEmail(lang);
        String password = inputs.getPassword(lang);
        double saldo = inputs.getSaldo(lang);
        boolean inserted = this.facade.registarNegociador(nif,nome,email,password,saldo);
        nextMenu(lang,inserted,nif);
    }

    private void showAdeus() {
        System.out.println(this.factory.getLang().getGoodbyeMessage());
    }

}