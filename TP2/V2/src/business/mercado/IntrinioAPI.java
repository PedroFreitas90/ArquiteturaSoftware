package business.mercado;

import com.intrinio.api.*;
import com.intrinio.invoker.ApiClient;
import com.intrinio.invoker.ApiException;
import com.intrinio.invoker.Configuration;
import com.intrinio.invoker.auth.ApiKeyAuth;
import com.intrinio.models.*;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IntrinioAPI implements Mercado {

	private final static List<String> EXCHANGES = new ArrayList<>(Arrays.asList("XLIS", "XAMS", "XBRU"));

	private ApiKeyAuth autenticacao(){
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");
		return auth;
	}


	public List<String> getAcoes() {
		autenticacao();
		StockExchangeApi stockExchangeApi = new StockExchangeApi();
		List<String> result = new ArrayList<>();
		for(String identifier : EXCHANGES) {
			Integer pageSize = 100;
			try {
				ApiResponseStockExchangeSecurities r = stockExchangeApi.getStockExchangeSecurities(identifier, pageSize, null);
				r.getSecurities().forEach(s -> result.add(s.getId()));
			} catch (ApiException e) {
				System.err.println("Could not retrieve Acoes. Defaulting to empty List!");
			}
		}

		return result;
	}

	public String getNomeAcao(String identifier) {
		autenticacao();
		SecurityApi securityApi = new SecurityApi();
		String result = "";
		try {
			Security s = securityApi.getSecurityById(identifier);
			result = s.getName();
		} catch (ApiException e) {
			System.err.println("Could not get name of " + identifier + ". Defaulting to empty name!");
		}

		return result;
	}

	public String getEmpresaAcao(String identifier) {
		autenticacao();
		String result = "";
		SecurityApi securityApi = new SecurityApi();
		try {
			Security s = securityApi.getSecurityById(identifier);
			result = s.getName();
		} catch (ApiException e) {
			System.err.println("Could not get the name of the company of " + identifier + ". Defaulting to empty name!");
		}

		return result;
	}

	public double getCotacaoAcao(String identifier) {
		autenticacao();
		SecurityApi securityApi = new SecurityApi();

		LocalDate startDate = LocalDate.now().minusDays(4);
		LocalDate endDate = LocalDate.now();
		String frequency = "daily";
		Integer numberResultsToReturn = 100;
		try {
			ApiResponseSecurityStockPrices result = securityApi.getSecurityStockPrices(identifier, startDate, endDate, frequency, numberResultsToReturn, null);
			BigDecimal close = BigDecimal.ZERO;
			if (result.getStockPrices().size() > 1)
				close = result.getStockPrices().get(0).getClose();
			return close.doubleValue();
		} catch (ApiException e) {
			System.err.println("Could not get quote for " + identifier + ". Defaulting to 0");
		}
		return 0;
	}

	@Override
	public double getCotacaoCommodity(String identifier) {
		autenticacao();
		IndexApi indexApi = new IndexApi();
		String tag = "level";
		try {
			BigDecimal result = indexApi.getSicIndexDataPointNumber(identifier, tag);
			if (result.doubleValue() == 0)
				return ThreadLocalRandom.current().nextDouble(10, 250);
			return result.doubleValue();
		} catch (ApiException e) {
			System.err.println("Could not get quote for " + identifier + ". Defaulting to 0");
		}
		return 0;
	}

	public List<String> getCommodities() {
		autenticacao();
		IndexApi indexApi = new IndexApi();
		List<String> result = new ArrayList<>();
		Integer numberResultsToReturn = 100;
		try {
			ApiResponseSICIndices r = indexApi.getAllSicIndices(numberResultsToReturn, null);
			r.getIndices().forEach(i -> result.add(i.getId()));
		} catch (ApiException e) {
			System.err.println("Could not get Commodities. Defaulting to empty List");
		}

		return result;
	}

	public String getNomeCommodity(String identifier) {
		autenticacao();
		IndexApi indexApi = new IndexApi();
		try {
			SICIndex result = indexApi.getSicIndexById(identifier);
			return result.getName();
		} catch (ApiException e) {
			System.err.println("Could not get name for " + identifier + ". Defaulting to empty name");
		}
		return "";
	}

	public String getPaisCommodity(String identifier) {
		autenticacao();
		IndexApi indexApi = new IndexApi();
		try {
			SICIndex result = indexApi.getSicIndexById(identifier);
			return result.getCountry();
		} catch (ApiException e) {
			System.err.println("Could not get pais for " + identifier + ". Defaulting to empty name");
		}
		return "";
	}

	@Override
	public double getCotacaoIndice(String identifier) {
		autenticacao();
		IndexApi indexApi = new IndexApi();
		String tag = "level"; // String | An Intrinio data tag ID or code-name
		try {
			BigDecimal result = indexApi.getStockMarketIndexDataPointNumber(identifier, tag);
			return result.doubleValue();
		} catch (ApiException e) {
			System.err.println("Could not get quote for " + identifier + ". Defaulting to 0");
		}
		return 0;
	}

	public List<String> getIndices() {
		autenticacao();
		IndexApi indexApi = new IndexApi();
		List<String> result = new ArrayList<>();
		Integer numberResultsToReturn = 100;
		try {
			ApiResponseStockMarketIndices r = indexApi.getAllStockMarketIndices(numberResultsToReturn, null);
			r.getIndices().forEach(i -> result.add(i.getId()));
		} catch (ApiException e) {
			System.err.println("Could not get indices. Defaulting to empty list");
		}
		return result;
	}

	public String getNomeIndice(String identifier) {
		autenticacao();
		IndexApi indexApi = new IndexApi();
		String result = "";
		try {
			StockMarketIndex r = indexApi.getStockMarketIndexById(identifier);
			return r.getName();
		} catch (ApiException e) {
			System.err.println("Could not get name for " + identifier + ". Defaulting to empty name");
		}
		return result;
	}

	@Override
	public double getCotacaoMoeda(String identifier) {
		autenticacao();
		ForexApi forexApi = new ForexApi();
		String timeframe = "D1";
		String timezone = "UTC";
		LocalDate startDate = LocalDate.now().minusDays(7);
		LocalDate endDate = LocalDate.now();
		Integer numberResultsToReturn = 100;
		double value;
		try {
			ApiResponseForexPrices result = forexApi.getForexPrices(identifier, timeframe, timezone, startDate,null, endDate,null, numberResultsToReturn, null);
			value= getCotacaoMoeda(result);
			return value;
		} catch (ApiException e) {
			System.err.println("Could not get quote for " + identifier + ". Defaulting to 0");
		}
		return 0;
	}

	public List<String> getMoedas() {
		List<String> result = new ArrayList<>();
		ForexApi forexApi = new ForexApi();
		try {
			ApiResponseForexPairs r = forexApi.getForexPairs();
			r.getPairs().forEach(c -> result.add(c.getCode()));
		} catch (ApiException e) {
			System.err.println("Could not get moedas. Defaulting to empty list");
		}
		return result;
	}

	public String getMoedaBase(String identifier) {
		return identifier.substring(0,3);
	}

	public String getMoedaQuota(String identifier) {
		return identifier.substring(2,6);
	}


	private double getCotacaoMoeda(ApiResponseForexPrices result){
		double value;
		if (result.getPrices().size() > 0)
			value = result.getPrices().get(0).getOpenBid().doubleValue();
		else {
			value = ThreadLocalRandom.current().nextDouble(0.5, 3);
		}
		return value;
	}
}