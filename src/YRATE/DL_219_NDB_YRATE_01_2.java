package YRATE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.math.BigDecimal;
import com.cake.net._useage.Parameter;
import com.cake.net.http.html.htmlparser.Extractor_Property;
import com.cake.net.http.html.htmlparser.Extractor_Result;
import com.cake.net.http.html.htmlparser.HtmlParser;
import com.tej.error.ErrorTitle;
import com.tej.frame.DownloadFrame;
import com.tej.frame.Table;

public class DL_219_NDB_YRATE_01_2 extends DownloadFrame {
	public Parameter parameter;

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public DL_219_NDB_YRATE_01_2(Parameter parameter) {
		this.parameter = parameter;
	}
	
	@Override
	public String getData(String url) throws Exception {
		//驅動chrome的EXE路徑
//		System.setProperty("webdriver.chrome.driver","A:\\\\selenium\\Webdriver\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", parameter.getMenu().get("setProperty"));
		WebDriver driver =null;
			try {			
				ChromeOptions chromeOpt = new ChromeOptions();
				//偽裝正常瀏覽器
				chromeOpt.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				chromeOpt.setExperimentalOption("useAutomationExtension", false);
				chromeOpt.addArguments("User-Agent=Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.9 Mobile Safari/537.36");
				chromeOpt.addArguments("--disable-blink-features");
				chromeOpt.addArguments("--disable-blink-features=AutomationControlled");
				chromeOpt.addArguments("start-maximized");
				chromeOpt.addArguments("enable-automation");
				chromeOpt.addArguments("--no-sandbox");
				chromeOpt.addArguments("--disable-infobars");
				chromeOpt.addArguments("--disable-dev-shm-usage");
				chromeOpt.addArguments("--disable-browser-side-navigation"); 
				chromeOpt.addArguments("--disable-gpu"); 					
				chromeOpt.addArguments("--disable-blink-features=AutomationControlled");
				//關閉自動跳出視窗
				chromeOpt.addArguments("--headless");
				//chrome相關設定
				DesiredCapabilities dc = DesiredCapabilities.chrome();
				dc.setJavascriptEnabled(true);
				//換IP
				Proxy proxy = new Proxy();
				proxy.setProxyType(ProxyType.MANUAL);
				proxy.setFtpProxy("10.10.20.52:8080");
				proxy.setHttpProxy("10.10.20.52:8080");
				proxy.setSslProxy("10.10.20.52:8080");
				dc.setCapability(CapabilityType.PROXY, proxy);
				//將chromeOpt的設定存進去
				dc.setCapability(ChromeOptions.CAPABILITY, chromeOpt);
				driver = new ChromeDriver(dc);
				//設定視窗大小
				driver.manage().window().setSize(new Dimension(1500, 1500));
				//timeout設定
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);         
				driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
				driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
				//偽裝正常瀏覽器
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("Object.defineProperties(navigator, {webdriver:{get:()=>undefined}});");			//連線網頁
				driver.get(url);
				//點選方式參考Seleumin的API
				
				//取得source
				String source = driver.getPageSource();
				return source;
			} catch (Exception e) {
				throw e;
			} finally{
				//一定要關閉!!
				if(driver!=null)driver.quit();
			}
				
	}
	

//	public String getData(String url, String currency, String start, String end) throws Exception {
//
//		try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
//			CloseableHttpResponse response;
//
//			// GET拜訪網頁拿參數
//			HttpGet httpget = new HttpGet(url);
//			httpget.setHeader("Accept",
//					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//			HttpResponse httpresponse = httpClient.execute(httpget);
//			Scanner sc = new Scanner(httpresponse.getEntity().getContent());
//			String allData = null;
//			while (sc.hasNext()) {
//				allData = allData + sc.nextLine() + "\n\r";
//			}
//			sc.close();
//			Document doc = Jsoup.parse(allData);
//			// 取得參數
//
//			System.out.println("d="+doc);
//			
//			String viewstate = doc.getElementById("__VIEWSTATE").attr("value");
//			String eventvalidation = doc.getElementById("__EVENTVALIDATION").attr("value");
//			String requestdigest = doc.getElementById("__REQUESTDIGEST").attr("value");
//
//			// 下載資料
//			HttpPost post = new HttpPost(url);
//			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
//
//			List<NameValuePair> form = new ArrayList<>();
//			form.add(new BasicNameValuePair("__REQUESTDIGEST", requestdigest));
//			form.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
//			form.add(new BasicNameValuePair("__EVENTVALIDATION", eventvalidation));
//
//			// 幣別參數
//			form.add(new BasicNameValuePair("ctl00$PlaceHolderEmptyMain$PlaceHolderMain$durationid$ddlCurrencyList",
//					currency));
//			// start yymmnn
//			form.add(new BasicNameValuePair("inq1_year", start.substring(0, 4)));
//			form.add(new BasicNameValuePair("inq1_month", start.substring(4, 6)));
//			form.add(new BasicNameValuePair("inq1_day", start.substring(6, 8)));
////			end yymmnn
//			form.add(new BasicNameValuePair("inq2_year", end.substring(0, 4)));
//			form.add(new BasicNameValuePair("inq2_month", end.substring(4, 6)));
//			form.add(new BasicNameValuePair("inq2_day", end.substring(6, 8)));
//			form.add(new BasicNameValuePair("ctl00$PlaceHolderEmptyMain$PlaceHolderMain$durationid$btnSearch", "確定查詢"));
//
//			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
//			post.setEntity(entity);
//
//			// 送出請求
//			response = httpClient.execute(post);
//			HttpEntity responseEntity = response.getEntity();
//			String source = EntityUtils.toString(responseEntity);
//
//			return source;
//		} catch (Exception ex) {
//			logger.error("網頁連線有誤", ex);
//		}
//
//		return null;
//	}

	// todo 清單只抓一半?
	// 先抓清單
	public ArrayList<String> getlist(String url) throws Exception {
		try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
//			CloseableHttpResponse response;
			// GET拜訪網頁拿參數
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			HttpResponse httpresponse = httpClient.execute(httpget);
			Scanner sc = new Scanner(httpresponse.getEntity().getContent());
			String source = null;
			while (sc.hasNext()) {
				source = source + sc.nextLine() + "\n\r";
			}
			sc.close();

			Document doc = Jsoup.parse(source);
			
			
			Elements currency = doc.select("option");
			ArrayList<String> cur_list = new ArrayList<>();

			for (Element element : currency) {
				cur_list.add(element.attr("value"));
//				System.out.println("doc="+cur_list);
			}

			return cur_list;

		}
	}

	@Override
	public Table[] parseListMap(String source, String currency) {

		// 表格陣列
		List<Table> tableList = new ArrayList<Table>();

		// 宣告表格資料暫存陣列
		List<Txwebch> listMap = new ArrayList<Txwebch>();

		// 此處應只有網頁核心資料已截取過區塊
		// 外部檔案讀取可直接由此處讀入並寫入暫存

		String title = parameter.getMenu().get("title_2");
		String end_1 = parameter.getMenu().get("end_1");
		String Bcode = parameter.getMenu().get("Bcode");
		String Bname = parameter.getMenu().get("Bname");
		String Type_buy = parameter.getMenu().get("Type_buy");
		String Type_sell = parameter.getMenu().get("Type_sell");
		String zdate = null;
		int count = 0;
		try {

			// 網頁標籤轉換資料分隔格式
			Extractor_Property extract = new Extractor_Property();
			extract.setEncoding(parameter.getEncoding());
			HtmlParser parser = new HtmlParser(source.toString(), extract);
			Extractor_Result extractResult = parser.getExtractResult();

			String lines[] = extractResult.getResult();
			boolean ishead = false;
			for (String line : lines) {
				
				System.out.println("l="+line);

				
				if (line.startsWith(title)) {
					ishead = true;
					continue;
				}

				if (line.replace("#", "").replace(":", "").equals(""))
					continue;

				if (line.contains(end_1))
					break;

				if (ishead) {
					count++; // 計算行數 資料散在兩行
					String column[] = (line + " ").split("#");
					// 資料回圈中每筆資料處理都要try..catch避免剖析段中斷
					try {
						Txwebch bean = new Txwebch();
						bean.setBcode(Bcode);
						bean.setCurrency(currency);
						bean.setBname(Bname);
						bean.setSpot(new BigDecimal(column[5]));
						// 部分資料為空值
						if (!column[6].replace(" ", "").equals("")) {
							bean.setCash(new BigDecimal(column[6].replace(" ", "")));
						}

						if (column[4].contains("買")) {
							bean.setType(Type_buy);
						} else if (column[4].contains("賣")) {
							bean.setType(Type_sell);
						}

						// 資料只有第一行有時間
						if (count % 2 == 1) {
							zdate = column[1];
							bean.setZdate(checkDate(zdate));
						} else if (count % 2 == 0) {
							bean.setZdate(checkDate(zdate));
						}

						// 分析資料寫入暫存部分

						listMap.add(bean);
					} catch (Exception e) {
						logger.error(ErrorTitle.CONTENT_TITLE.getTitle(line), e);
					}
				}
			}
			int check = 0;
			if (source.contains(parameter.getMenu().get("title_3"))) {
				logger.info("此區間無資料！");
				check = 1;
			}

			if (!ishead && check != 1) {
				logger.error(ErrorTitle.CONTENT_TITLE.getTitle("表頭異動,無法匯入資料"));
			}

		} catch (Exception e) {
			// 網頁轉換 或 其餘例外錯誤
			logger.error(ErrorTitle.ANALYSIS_TITLE.getTitle(), e);
		} finally {
			tableList.add(new Table(listMap.toArray(new Txwebch[0]), "表格註解"));
		}

		// 此function例外錯誤由抽象類別(DownloadFrame)統一回傳

		return tableList.toArray(new Table[0]);
	}

	/**
	 * 檢查日期格式
	 * 
	 * @param d
	 * @return
	 * @throws ParseException
	 */
	private java.sql.Date checkDate(String d) throws ParseException {
		d = d.replace("-", "");
		if (d.trim().equals(""))
			return null;
		else
			return new java.sql.Date(sdf.parse(d).getTime());
	}
	
	public String getData2(String arg0) throws Exception {
		return null;
	}

}
