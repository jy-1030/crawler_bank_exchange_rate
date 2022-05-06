package YRATE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.By;
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
import com.cake.net.http.html.HTML;
import com.cake.net.http.html.HTMLX;
import com.cake.net.http.html.htmlparser.Extractor_Property;
import com.cake.net.http.html.htmlparser.Extractor_Result;
import com.cake.net.http.html.htmlparser.HtmlParser;
import com.tej.error.ErrorTitle;
import com.tej.frame.DownloadFrame;
import com.tej.frame.Table;
import com.tej.setting.IP;

public class DL_219_NDB_YRATE_01_1 extends DownloadFrame {
	public Parameter parameter;
	byte[] encodedBytes = null;


	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public DL_219_NDB_YRATE_01_1(Parameter parameter) {
		this.parameter = parameter;
	}
	
	List<String> prox = IP.getIPFromTable();// 換IP


	@Override
	public String getData(String url) throws Exception {
		//驅動chrome的EXE路徑
//		System.setProperty("webdriver.chrome.driver","A:\\\\selenium\\Webdriver\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", parameter.getMenu().get("setProperty"));
		WebDriver driver =null;
		String source = "";
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
//				chromeOpt.setBinary("C:\\Users\\dladmin\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");// 路徑要改



				//關閉自動跳出視窗
//				chromeOpt.addArguments("--headless");
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
				
//				driver.findElement(By.id("select_spot_time")).click();
				driver.findElement(By.xpath("//*[@id='select_spot_time']/option[8]")).click();
//				Thread.sleep(3000);
				driver.findElement(By.xpath("/html/body/div[1]/main/section[4]/div/div/div[2]/form/div/div[2]/div/div[1]/a")).click();
				Thread.sleep(10000);
				
				//取得source
				source = driver.getPageSource();
				return source;

			} catch (Exception e) {
				throw e;
			} finally{
				//一定要關閉!!
				Thread.sleep(10000);
				if(driver!=null)driver.quit();
			}
				
	}
	
//	public String getData(String url) throws Exception {
//
//		HTML htmlx = new HTMLX(url, false);
//		htmlx.setIgnoreSSLCertificate(true);
//		htmlx.setConnectTimeout(parameter.getConnectTimeout(), parameter.getRetry());
//		// 設定網頁連結多久中斷 ,為避免連線過久卡在網頁
//		htmlx.setReadTimeout(parameter.getReadTimeout(), parameter.getRetry());
//		htmlx.connect();
//		htmlx.extractInputStream(parameter.getEncoding());
//		String source = htmlx.getCodeStringtype();
//		
//		
////		System.out.println("source="+source);
//		return source;
//	}
	
	
	// 先抓清單
	public ArrayList<String> getlist(String url) throws Exception {

		HTML htmlx = new HTMLX(url, false);
		htmlx.setIgnoreSSLCertificate(true);
		htmlx.setConnectTimeout(parameter.getConnectTimeout(), parameter.getRetry());
		// 設定網頁連結多久中斷 ,為避免連線過久卡在網頁
		htmlx.setReadTimeout(parameter.getReadTimeout(), parameter.getRetry());
		htmlx.connect();
		htmlx.extractInputStream(parameter.getEncoding());

		String source = htmlx.getCodeStringtype();

		ArrayList<String> cur_list = new ArrayList<>();
		String title_1 = parameter.getMenu().get("title_1");
		String end_1 = parameter.getMenu().get("end_1");

		// 網頁標籤轉換資料分隔格式
		Extractor_Property extract = new Extractor_Property();
		extract.setEncoding(parameter.getEncoding());
		HtmlParser parser = new HtmlParser(source.toString(), extract);
		Extractor_Result extractResult = parser.getExtractResult();

		String lines[] = extractResult.getResult();
		boolean ishead = false;
		for (String line : lines) {

//			System.out.println("l="+line);
			// start
			if (line.contains(title_1)) {
				ishead = true;
				continue;
			}
			// end
			if (line.contains(end_1))
				break;

			if (line.contains(":"))
				continue;

			if (line.replace("#", "").replace(" ", "").equals(""))
				continue;

			if (ishead) {
				String column[] = (line + " ").split("#");
//				cur_list.add(column[1]);
				cur_list.add(column[2]);
				
//				System.out.println("c="+cur_list);
			}
		}
		return cur_list;

	}

	public Table[] parseListMap(String source, ArrayList<String> cur_list) {

		// 表格陣列
		List<Table> tableList = new ArrayList<Table>();

		// 宣告表格資料暫存陣列
		List<Txwebch> listMap = new ArrayList<Txwebch>();

		// 此處應只有網頁核心資料已截取過區塊
		// 外部檔案讀取可直接由此處讀入並寫入暫存

		String title_1 = parameter.getMenu().get("title_1");
		String end_1 = parameter.getMenu().get("end_1");
		String Bcode = parameter.getMenu().get("Bcode");
		String Bname = parameter.getMenu().get("Bname");
		String Type_buy = parameter.getMenu().get("Type_buy");
		String Type_sell = parameter.getMenu().get("Type_sell");

		try {

			// 網頁標籤轉換資料分隔格式
			Extractor_Property extract = new Extractor_Property();
			extract.setEncoding(parameter.getEncoding());
			HtmlParser parser = new HtmlParser(source.toString(), extract);
			Extractor_Result extractResult = parser.getExtractResult();

			String lines[] = extractResult.getResult();
			boolean ishead = false;
			for (String line : lines) {

				// 共用過濾標題
				if (line.replace("#", "").replace(" ", "").replace(":", "").equals(""))
					continue;

//				if (line.contains(title_1)) {
//					ishead = true;
//					continue;
//				}
				
				if(line.startsWith(title_1)){
					ishead = true;
					continue;
				}
				// end
				if (line.contains(end_1)) {
					break;
				}

				if (ishead) {
					String column[] = (line + " ").split("#");
					String currency[] = column[2].split(" ");

//					System.out.println("l5="+line);
					try {
						Txwebch bean = new Txwebch();

							bean.setBcode(Bcode);
							bean.setBname(Bname);
							bean.setZdate(getDate());
							bean.setCurrency(currency[1]);								
							bean.setType(Type_buy);
							bean.setSpot(checkBigDecimal(column[3].trim()));
							bean.setCash(checkBigDecimal(column[5].trim()));
							listMap.add(bean);
							System.out.println(bean.toString());

						
						bean = new Txwebch();
							bean.setBcode(Bcode);
							bean.setBname(Bname);
							bean.setZdate(getDate());
							bean.setCurrency(currency[1]);								
							bean.setType(Type_sell);
							bean.setSpot(checkBigDecimal(column[4].trim()));
							bean.setCash(checkBigDecimal(column[6].trim()));
							listMap.add(bean);
							System.out.println(bean.toString());




						// 分析資料寫入暫存部分
						// 每兩行判斷一次
//						for (int i = 0; i < cur_list.size() / 2; i++) {
//							for (int j = 0; j < 2; j++) {
//								// case I
//								if (line.contains(cur_list.get(2 * i))) {
//									if (j == 0) {
//
//										if (column[3].replace(" ", "").equals(""))
//											continue;
//
//										else {
//											try {
//												bean.setSpot(new BigDecimal(column[3]));
//												if (!column[4].replace(" ", "").equals("")) {
//													bean.setCash(new BigDecimal(column[4]));
//												}
//												bean.setBcode(Bcode);
//												bean.setBname(Bname);
//												bean.setType(Type_buy);
//												bean.setZdate(getDate());
//												bean.setCurrency(cur_list.get(2 * i + 1));
//												listMap.add(bean);
//											} catch (Exception e) {
//												logger.error(ErrorTitle.CONTENT_TITLE.getTitle(line), e);
//											}
//										}
//									}
//								}
//								// case II
//								else if (line.contains(cur_list.get(2 * i + 1))) {
//									// 部分資料為空值，須跳過
//									if (j == 0) {
//										if (column[3].replace(" ", "").equals(""))
//											continue;
//										else {
//											bean.setSpot(new BigDecimal(column[3]));
//											if (!column[4].replace(" ", "").equals("")) {
//												bean.setCash(new BigDecimal(column[4]));
//											}
//											bean.setBcode(Bcode);
//											bean.setBname(Bname);
//											bean.setType(Type_sell);
//											bean.setZdate(getDate());
//											bean.setCurrency(cur_list.get(2 * i + 1));
//											listMap.add(bean);
//										}
//									}
//								}
//							}
//						}
					} catch (Exception e) {
						logger.error(ErrorTitle.CONTENT_TITLE.getTitle(line), e);
					}
				}
			}

			if (!ishead)
				logger.error(ErrorTitle.CONTENT_TITLE.getTitle("表頭異動,無法匯入資料"));

		} catch (Exception e) {
			// 網頁轉換 或 其餘例外錯誤
			logger.error(ErrorTitle.ANALYSIS_TITLE.getTitle(), e);
		} finally {
			tableList.add(new Table(listMap.toArray(new Txwebch[0]), "表格註解"));
		}

		// 此function例外錯誤由抽象類別(DownloadFrame)統一回傳

		return tableList.toArray(new Table[0]);
	}

	private java.sql.Date getDate() throws ParseException {
		Date date = new Date();

		return new java.sql.Date(date.getTime());

	}
	
	

	/**
	 * BigDecimal 格式檢查
	 * 
	 * @param d
	 * @return
	 */
	private BigDecimal checkBigDecimal(String d) {

		d = d.replace(",", "").trim().replaceAll("[A-Za-z]", "");
		if (d.replace("-", "").trim().equals(""))
			return null;
		else
			return new BigDecimal(d);
	}

}
