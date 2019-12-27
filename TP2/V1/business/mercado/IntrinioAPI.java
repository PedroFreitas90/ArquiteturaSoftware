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

	public List<String> getAcoes() {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

		StockExchangeApi stockExchangeApi = new StockExchangeApi();

		List<String> result = new ArrayList<>();

		for(String identifier : EXCHANGES) {
			Integer pageSize = 100; // Integer | The number of results to return
			String nextPage = null; // String | Gets the next page of data from a previous API call

			try {
				ApiResponseStockExchangeSecurities r = stockExchangeApi.getStockExchangeSecurities(identifier, pageSize, nextPage);
				r.getSecurities().forEach(s -> result.add(s.getId()));
			} catch (ApiException e) {
				System.err.println("Could not retrieve Acoes. Defaulting to empty List!");
			}
		}

		return result;
	}

	public String getNomeAcao(String identifier) {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

		SecurityApi securityApi = new SecurityApi();

		// String | A Security identifier (Ticker, FIGI, ISIN, CUSIP, Intrinio ID)
		LocalDate startDate = LocalDate.now().minusDays(4); // LocalDate | Return prices on or after the date
		LocalDate endDate = LocalDate.now(); // LocalDate | Return prices on or before the date
		String frequency = "daily"; // String | Return stock prices in the given frequency
		Integer pageSize = 100; // Integer | The number of results to return
		String nextPage = null; // String | Gets the next page of data from a previous API call

		try {
			ApiResponseSecurityStockPrices result = securityApi.getSecurityStockPrices(identifier, startDate, endDate, frequency, pageSize, nextPage);
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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

		IndexApi indexApi = new IndexApi();

		// String identifier = "$SIC.1"; // String | An Index Identifier (symbol, Intrinio ID)
		String tag = "level"; // String | An Intrinio data tag ID or code-name

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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

		IndexApi indexApi = new IndexApi();
		List<String> result = new ArrayList<>();

		Integer pageSize = 100; // Integer | The number of results to return
		String nextPage = null; // String | Gets the next page of data from a previous API call

		try {
			ApiResponseSICIndices r = indexApi.getAllSicIndices(pageSize, nextPage);
			r.getIndices().forEach(i -> result.add(i.getId()));
		} catch (ApiException e) {
			System.err.println("Could not get Commodities. Defaulting to empty List");
		}

		return result;
	}

	public String getNomeCommodity(String identifier) {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

		IndexApi indexApi = new IndexApi();
		List<String> result = new ArrayList<>();

		Integer pageSize = 100; // Integer | The number of results to return
		String nextPage = null; // String | Gets the next page of data from a previous API call

		try {
			ApiResponseStockMarketIndices r = indexApi.getAllStockMarketIndices(pageSize, nextPage);
			r.getIndices().forEach(i -> result.add(i.getId()));
		} catch (ApiException e) {
			System.err.println("Could not get indices. Defaulting to empty list");
		}
		return result;
	}

	public String getNomeIndice(String identifier) {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

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
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

		ForexApi forexApi = new ForexApi();

		//String pair = "EURUSD"; // String | The Forex Currency Pair code
		String timeframe = "D1"; // String | The time interval for the quotes
		String timezone = "UTC"; // String | Returns trading times in this timezone
		LocalDate startDate = LocalDate.now().minusDays(7); // LocalDate | Return Forex Prices on or after this date
		String startTime = null; // String | Return Forex Prices at or after this time (24-hour)
		LocalDate endDate = LocalDate.now(); // LocalDate | Return Forex Prices on or before this date
		String endTime = null; // String | Return Forex Prices at or before this time (24-hour)
		Integer pageSize = 100; // Integer | The number of results to return
		String nextPage = null; // String | Gets the next page of data from a previous API call

		try {
			ApiResponseForexPrices result = forexApi.getForexPrices(identifier, timeframe, timezone, startDate, startTime, endDate, endTime, pageSize, nextPage);
			double value;
			if (result.getPrices().size() > 0)
				value = result.getPrices().get(0).getOpenBid().doubleValue();
			else {
				// if sandbox does not allow for getting this value, generate random between 0.5 and 3
				value = ThreadLocalRandom.current().nextDouble(0.5, 3);
			}
			return value;
		} catch (ApiException e) {
			System.err.println("Could not get quote for " + identifier + ". Defaulting to 0");
		}
		return 0;
	}

	public List<String> getMoedas() {
		List<String> result = new ArrayList<>();

		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth auth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		auth.setApiKey("OjcxNjhjYzQ5NzAwMThlNTlmZTI5YWI4M2NlZGFhMmI1");

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
}