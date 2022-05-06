package YRATE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Proxy.ProxyType;
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

public class DL_219_NDB_YRATE_01 extends DownloadFrame {
	public Parameter parameter;

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public DL_219_NDB_YRATE_01(Parameter parameter) {
		this.parameter = parameter;
	}

	// 連結抓取資料處理 網頁連線 or 規則複雜的資料截取

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
//				chromeOpt.setBinary("C:\\Users\\dladmin\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");// 路徑要改

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
				
				Thread.sleep(10000);
				
				
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
		String title_1 = parameter.getMenu().get("title_2");
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
				cur_list.add(column[1]);
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

		String title_1 = parameter.getMenu().get("title_2");
		String end_1 = parameter.getMenu().get("end_2");
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
				
//				System.out.println("ll="+line);

				// 共用過濾標題
				if (line.replace("#", "").replace(" ", "").replace(":", "").equals(""))
					continue;

				// start
//				if (line.contains(title_1)) {
//					ishead = true;
//					continue;
//				}
				
				if(line.startsWith(title_1)){
					ishead = true;
					continue;
				}
				
				if(line.contains("指定時點牌告匯率查詢")){
//					System.out.println("ll="+line);

				}
				// end
				if (line.startsWith(end_1)){
					break;
				}
//				System.out.println("ll="+line);
				if (ishead) {
					String column[] = (line + " ").split("#");
					String currency[] = column[2].split(" ");

//					System.out.println("l="+line);
//					System.out.println(column[2]);
					
//					for(int i=0; i<=column.length; i++){
//						System.out.println("c="+column[i]);
//					}

					// 資料回圈中每筆資料處理都要try..catch避免剖析段中斷
					try {

						// 分析資料寫入暫存部分
						// 每兩行判斷一次
//						for (int i = 0; i < cur_list.size() / 2; i++) {

						Txwebch bean = new Txwebch();
						if(column[3].contains("買")){
							bean.setBcode(Bcode);
							bean.setBname(Bname);
							bean.setType(Type_buy);
							bean.setZdate(getDate());
							bean.setCurrency(currency[1]);
							bean.setF10(checkBigDecimal(column[4].trim()));
							bean.setF30(checkBigDecimal(column[5].trim()));
							bean.setF60(checkBigDecimal(column[6].trim()));
							bean.setF90(checkBigDecimal(column[7].trim()));
							bean.setF120(checkBigDecimal(column[8].trim()));
							bean.setF180(checkBigDecimal(column[9].trim()));
							listMap.add(bean);
							
							System.out.println(bean.toString());
						}
						
							bean = new Txwebch();
						if(column[3].contains("賣")){
							bean.setBcode(Bcode);
							bean.setBname(Bname);
							bean.setType(Type_sell);
							bean.setZdate(getDate());
							bean.setCurrency(currency[1]);
							bean.setF10(checkBigDecimal(column[4].trim()));
							bean.setF30(checkBigDecimal(column[5].trim()));
							bean.setF60(checkBigDecimal(column[6].trim()));
							bean.setF90(checkBigDecimal(column[7].trim()));
							bean.setF120(checkBigDecimal(column[8].trim()));
							bean.setF180(checkBigDecimal(column[9].trim()));
							listMap.add(bean);
							
							System.out.println(bean.toString());
						}
							

//							//caseI = 買入
//							if (line.contains(cur_list.get(2 * i))) {
//								Txwebch bean = new Txwebch();
//								bean.setBcode(Bcode);
//								bean.setBname(Bname);
//								bean.setType(Type_buy);
//								bean.setZdate(getDate());
//								bean.setCurrency(cur_list.get(2 * i + 1));
//								bean.setF10(new BigDecimal(column[5]));
//								bean.setF30(new BigDecimal(column[6]));
//								bean.setF60(new BigDecimal(column[7]));
//								bean.setF90(new BigDecimal(column[8]));
//								bean.setF120(new BigDecimal(column[9]));
//								bean.setF180(new BigDecimal(column[10].replace(" ", "")));
//								listMap.add(bean);
//								i++;
//							//caseII = 賣出
//							} else if (line.contains(cur_list.get(2 * i + 1))) {
//								Txwebch bean = new Txwebch();
//								bean.setBcode(Bcode);
//								bean.setBname(Bname);
//								bean.setType(Type_sell);
//								bean.setZdate(getDate());
//								bean.setCurrency(cur_list.get(2 * i + 1));
//								bean.setF10(new BigDecimal(column[5]));
//								bean.setF30(new BigDecimal(column[6]));
//								bean.setF60(new BigDecimal(column[7]));
//								bean.setF90(new BigDecimal(column[8]));
//								bean.setF120(new BigDecimal(column[9]));
//								bean.setF180(new BigDecimal(column[10].replace(" ", "")));
//								listMap.add(bean);
//								i++;
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
