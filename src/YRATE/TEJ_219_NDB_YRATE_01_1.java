package YRATE;

import java.util.ArrayList;
import org.apache.log4j.xml.DOMConfigurator;
import com.cake.net._useage.Parameter;
import com.tej.error.ErrorTitle;
import com.tej.frame.PorcessFrame;
import com.tej.frame.Table;
import com.tej.postgresql.DBAdminConnector;
import com.tej.postgresql.connection.ConnectorTableBuilder;
import com.tej.postgresql.connection.IDBConnector;

public class TEJ_219_NDB_YRATE_01_1 extends PorcessFrame {
	public void taskDescription(Parameter param, IDBConnector market, String[] args) {
		super.parameter = param; // 參數檔
		super.market = market; // 匯入連線資訊

		String tableName = parameter.getTableName();
		DL_219_NDB_YRATE_01_1 dl = new DL_219_NDB_YRATE_01_1(parameter);
		String url = dl.parameter.getMenu().get("url_1");

		TxwebchDAO dao = new TxwebchDAO(tableName, market);

		// 第一段 : 抓檔 與 網頁處理(若讀a取外部檔匯入可以不需此段)
		String source = "";
		ArrayList<String> cur_list = null;
		try {
			// 先抓貨幣清單
			cur_list = dl.getlist(url);
		} catch (Exception e) {
			logger.error(ErrorTitle.CONNECT_TITLE.getTitle(), e);
		}

		// 第二段 : 分析資料存入暫存處理
		try {
			logger.info("網址 : " + url);
			source = dl.getData(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Table[] tableList = null;
		try {
			tableList = dl.parseListMap(source, cur_list);
		} catch (Exception e) {
			logger.error(ErrorTitle.PROCESS_TITLE.getTitle(), e);
		}

		// 資料處理暫存筆數為0 不需再執行匯入
		if (parameter.isDltestMode()) { // 偵側網改程式，參數test
			if (tableList[0].getTableBean().length == 0)
				logger.error(ErrorTitle.IMPORT_TITLE.getTitle("截取 0 筆資料"));
		} else {
			// 第三段 : 匯入資料庫處理
			try {
				// 依照分析資料存入暫存 處理的內容決定使用的表格
				dao.modify_1(tableList[0].getTableBean()); //即期
			} catch (Exception e) {
				logger.error(ErrorTitle.IMPORT_TITLE.getTitle(), e);
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

			TEJ_219_NDB_YRATE_01_1 runPg = new TEJ_219_NDB_YRATE_01_1();

			runPg.taskDescription(param, market, args);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
