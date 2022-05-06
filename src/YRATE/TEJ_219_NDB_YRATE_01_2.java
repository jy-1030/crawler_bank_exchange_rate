package YRATE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.log4j.xml.DOMConfigurator;
import com.cake.net._useage.Parameter;
import com.tej.error.ErrorTitle;
import com.tej.frame.PorcessFrame;
import com.tej.frame.Table;
import com.tej.postgresql.DBAdminConnector;
import com.tej.postgresql.connection.ConnectorTableBuilder;
import com.tej.postgresql.connection.IDBConnector;

public class TEJ_219_NDB_YRATE_01_2 extends PorcessFrame {
	public void taskDescription(Parameter param, IDBConnector market, String[] args) {
		super.parameter = param; // 參數檔
		super.market = market; // 匯入連線資訊

		String tableName = parameter.getTableName();

		DL_219_NDB_YRATE_01_2 dl = new DL_219_NDB_YRATE_01_2(parameter);
		String url = dl.parameter.getMenu().get("url_3");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		String[] day = new String[2];
		if (args.length == 0) {
			try {
				System.out.println("[WarningMessage]無輸入參數,抓取前七天資料!!");
				Calendar cal = Calendar.getInstance();
				day[1] =sdf.format(cal.getTime());
				cal.add(Calendar.DATE, -7);
				day[0] = sdf.format(cal.getTime());
			} catch (Exception e) {
				logger.error(ErrorTitle.UNKNOW_TITLE.getTitle("取前七天日期有誤"));
			}

		} else if (args.length == 1) {
			if (args[0].matches("[0-9]{8}")) {
				try {
					System.out.println("輸入指定日期成功");
					day[0] = args[0];
					day[1] = args[0];
				} catch (Exception e) {
					logger.error(ErrorTitle.UNKNOW_TITLE.getTitle("取指定日期有誤"));
				}
			} else {
				logger.error(ErrorTitle.PARAMETER_TITLE.getTitle("格式錯誤:" + args[0]));
			}
		} else if (args.length == 2) {
			if (args[0].matches("[0-9]{8}") && args[1].matches("[0-9]{8}")) {
				System.out.println("輸入指定區間成功");
				day[0] = args[0];
				day[1] = args[0];
			} else {
				logger.error(ErrorTitle.PARAMETER_TITLE.getTitle("格式錯誤:" + args[0] + "~" + args[1]));
			}
		}

		// 先抓貨幣清單
		ArrayList<String> cur_list = new ArrayList<String>();
		try {
			cur_list = dl.getlist(url);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("抓取清單錯誤，請確認是否網改!!");
		}

		for (int i = 0; i < cur_list.size(); i++) {

			// 第一段 : 抓檔 與 網頁處理(若讀a取外部檔匯入可以不需此段)
			String source = "";

			try {
//				source = dl.getData(url, cur_list.get(i), day[0], day[1]);
				source = dl.getData(url);
			} catch (Exception e) {
				logger.error(ErrorTitle.CONNECT_TITLE.getTitle(), e);
			}
			// 第二段 : 分析資料存入暫存處理
			Table[] tableList = null;
			try {
				tableList = dl.parseListMap(source, cur_list.get(i));
			} catch (Exception e) {
				logger.error(ErrorTitle.PROCESS_TITLE.getTitle(), e);
			}
			// 資料處理暫存筆數為0 不需再執行匯入
			if (parameter.isDltestMode()) { // 偵側網改程式，參數test
				if (tableList[0].getTableBean().length == 0)
					logger.error(ErrorTitle.IMPORT_TITLE.getTitle("截取 0 筆資料"));
			} else {
				// 第三段 : 匯入資料庫處理
				TxwebchDAO dao = new TxwebchDAO(tableName, market);
				try {
					// 依照分析資料存入暫存 處理的內容決定使用的表格
					dao.modify_1(tableList[0].getTableBean());
				} catch (Exception e) {
					logger.error(ErrorTitle.IMPORT_TITLE.getTitle(), e);
				}
			}

		}
	}

	public static void main(String[] args) {

		DOMConfigurator.configure(".\\log4j.xml");

		// admin連線資訊
		IDBConnector admin = DBAdminConnector.getInstance();

		// 宣告RFP名稱字串
		String spt = System.getProperty("file.separator");
		String propertyPath = System.getProperty("user.dir") + spt + "property" + spt + "TEJ_219_NDB_YRATE_01.property";

		// 宣告參數檔物件(給予檔案路徑)
		Parameter param = new Parameter(propertyPath);

		// 初始化物件
		param.initial();

		// 欲匯入表格連線資訊
		ConnectorTableBuilder builder = new ConnectorTableBuilder(admin, param.getDbName());

		try {
			IDBConnector market = builder.buildConnector();

			TEJ_219_NDB_YRATE_01_2 runPg = new TEJ_219_NDB_YRATE_01_2();

			runPg.taskDescription(param, market, args);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
