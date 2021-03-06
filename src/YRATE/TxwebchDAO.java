package YRATE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tej.error.ErrorTitle;
import com.tej.frame.DaoFrame;
import com.tej.postgresql.connection.IDBConnector;


public class TxwebchDAO extends DaoFrame{
	private String tableName;
	private IDBConnector market;
	public TxwebchDAO(String tableName  , IDBConnector market){
		this.tableName = tableName;
	    this.market = market;
	}

	
	/**
	 * 非版本別 先修改後新增
	 * @param ListMap
	 * @throws SQLException 
	 * @throws Exception
	 */
	//遠期
	public void modify(Object[] tempMap) throws Exception {
		
		if(tempMap.length == 0){
			logger.warn("本次資料0筆,不處理匯入");
			return ;
		}
		
		Txwebch[] ListMap = (Txwebch[])tempMap;

		int Counter = 0;
		PreparedStatement pstmt = null ;
		Connection conn = null;
		try{
			conn = this.market.getConnection();
			
			//設定連線
			conn.setAutoCommit(false);
			
			logger.info(this.getClass().getName()+" 本次資料共"+ListMap.length+"筆");
//			System.out.println("本次資料共"+ListMap.length+"筆");
			//建立表格
			logger.info("step1 更新資料(SQL):");
			
			String updateSQL = getUpadteSQL();
			pstmt = conn.prepareStatement(updateSQL);
			logger.debug("更新指令:"+updateSQL);
			
			int[] ok = new int[ListMap.length] ;
			for(int j=0;j<ListMap.length;j++){
				Txwebch obj = ListMap[j];
				try{
					pstmt.clearParameters();
					
					pstmt.setString(1,obj.getBname());
//					pstmt.setBigDecimal(2,obj.getSpot());
//					pstmt.setBigDecimal(3,obj.getCash());
					pstmt.setBigDecimal(2,obj.getF10());
					pstmt.setBigDecimal(3,obj.getF30());
					pstmt.setBigDecimal(4,obj.getF60());
					pstmt.setBigDecimal(5,obj.getF90());
					pstmt.setBigDecimal(6,obj.getF120());
					pstmt.setBigDecimal(7,obj.getF180());
					pstmt.setString(8,obj.getBcode());
					pstmt.setDate(9,obj.getZdate());
					pstmt.setString(10,obj.getCurrency());
					pstmt.setString(11,obj.getType());

					ok[j] = pstmt.executeUpdate();
					
					conn.commit();
					Counter++;
				}catch(Exception e){
				    logger.error(ErrorTitle.UPDATE_TITLE.getTitle(obj.getPK()) , e);
					
					ok[j] = -1;
					conn.rollback();
				}
			}
			logger.info("更新完畢, 共"+Counter+"筆執行更新指令");
			Counter = 0;
			
			logger.info("step2 新增資料(SQL):");
			String insertSQL = this.getInsertSQL();
			pstmt = conn.prepareStatement(insertSQL);
			logger.debug("新增指令:"+insertSQL);
			
			for(int i = 0;i<ListMap.length;i++){
				//已 update 過的不再新增
				if(ok[i]==1)continue ;
				Txwebch obj = ListMap[i];
				try{
					pstmt.clearParameters();
					
					pstmt.setString(1,obj.getBcode());
					pstmt.setDate(2,obj.getZdate());
					pstmt.setString(3,obj.getCurrency());
					pstmt.setString(4,obj.getType());
					pstmt.setString(5,obj.getBname());
//					pstmt.setBigDecimal(6,obj.getSpot());
//					pstmt.setBigDecimal(7,obj.getCash());
					pstmt.setBigDecimal(6,obj.getF10());
					pstmt.setBigDecimal(7,obj.getF30());
					pstmt.setBigDecimal(8,obj.getF60());
					pstmt.setBigDecimal(9,obj.getF90());
					pstmt.setBigDecimal(10,obj.getF120());
					pstmt.setBigDecimal(11,obj.getF180());
					
					pstmt.execute();
					conn.commit();
					Counter++;
				}catch(Exception e){
					logger.error(ErrorTitle.INSERT_TITLE.getTitle(obj.getPK()) , e);
					
					ok[i] = -1;
					conn.rollback();
				}
			}
			pstmt.close();
			logger.info("新增完畢, 共"+Counter+"筆新增");
			Counter = 0;
			
		}catch (SQLException e) {
			try { if (conn != null) conn.rollback(); } catch(Exception ee) { }
			throw e ;
		}catch (Exception e) {
			try { if (conn != null) conn.rollback(); } catch(Exception ee) { }
			throw e ;
		}finally{
            try { if (pstmt != null) pstmt.close();  } catch(Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); conn = null; } catch(Exception e) { e.printStackTrace();}
		}
		
		//此function例外錯誤(log紀錄)由抽象類別(DaoFrame)統一回傳
	}

	/**
	 * 新增
	 * @return
	 * @throws Exception
	 */
	public String getInsertSQL(){
		
		StringBuilder sql = new StringBuilder();
		
//		sql.append(" INSERT INTO "+tableName);
//		sql.append("( bcode,zdate,currency,type,bname,spot,cash,f10,f30,f60,f90,f120,f180)");
//		sql.append("  values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  )");

		sql.append(" INSERT INTO "+tableName);
		sql.append("( bcode,zdate,currency,type,bname,f10,f30,f60,f90,f120,f180)");
		sql.append("  values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )");

				
		return sql.toString();
		
	}	
	/**
	 * 更新
	 * @return
	 * @throws Exception
	 */
	public String getUpadteSQL(){
		
		StringBuilder sql = new StringBuilder();

//		sql.append(" UPDATE "+tableName);
//		sql.append(" SET bname = ? ,spot = ? ,cash = ? ,f10 = ? ,f30 = ? ,f60 = ? ,f90 = ? ,f120 = ?,f180 = ?"); 
//		sql.append(" WHERE (bcode = ?  AND zdate = ?  AND currency = ?  AND type = ?  )");

		
		sql.append(" UPDATE "+tableName);
		sql.append(" SET bname = ? ,f10 = ? ,f30 = ? ,f60 = ? ,f90 = ? ,f120 = ?,f180 = ?"); 
		sql.append(" WHERE (bcode = ?  AND zdate = ?  AND currency = ?  AND type = ?  )");


				
		return sql.toString();
		
	}

public void modify_1(Object[] tempMap) throws Exception {
		
		if(tempMap.length == 0){
			logger.warn("本次資料0筆,不處理匯入");
			return ;
		}
		
		Txwebch[] ListMap = (Txwebch[])tempMap;

		int Counter = 0;
		PreparedStatement pstmt = null ;
		Connection conn = null;
		try{
			conn = this.market.getConnection();
			
			//設定連線
			conn.setAutoCommit(false);
			
			logger.info(this.getClass().getName()+" 本次資料共"+ListMap.length+"筆");
//			System.out.println("本次資料共"+ListMap.length+"筆");
			//建立表格
			logger.info("step1 更新資料(SQL):");
			
			String updateSQL = getUpadteSQL_1();
			pstmt = conn.prepareStatement(updateSQL);
			logger.debug("更新指令:"+updateSQL);
			
			int[] ok = new int[ListMap.length] ;
			for(int j=0;j<ListMap.length;j++){
				Txwebch obj = ListMap[j];
				try{
					pstmt.clearParameters();
					
					pstmt.setString(1,obj.getBname());
					pstmt.setBigDecimal(2,obj.getSpot());
					pstmt.setBigDecimal(3,obj.getCash());
					pstmt.setString(4,obj.getBcode());
					pstmt.setDate(5,obj.getZdate());
					pstmt.setString(6,obj.getCurrency());
					pstmt.setString(7,obj.getType());
//					pstmt.setBigDecimal(4,obj.getF10());
//					pstmt.setBigDecimal(5,obj.getF30());
//					pstmt.setBigDecimal(6,obj.getF60());
//					pstmt.setBigDecimal(7,obj.getF90());
//					pstmt.setBigDecimal(8,obj.getF120());
//					pstmt.setBigDecimal(9,obj.getF180());

					ok[j] = pstmt.executeUpdate();
					
					conn.commit();
					Counter++;
				}catch(Exception e){
				    logger.error(ErrorTitle.UPDATE_TITLE.getTitle(obj.getPK()) , e);
					
					ok[j] = -1;
					conn.rollback();
				}
			}
			logger.info("更新完畢, 共"+Counter+"筆執行更新指令");
			Counter = 0;
			
			logger.info("step2 新增資料(SQL):");
			String insertSQL = this.getInsertSQL_1();
			pstmt = conn.prepareStatement(insertSQL);
			logger.debug("新增指令:"+insertSQL);
			
			for(int i = 0;i<ListMap.length;i++){
				//已 update 過的不再新增
				if(ok[i]==1)continue ;
				Txwebch obj = ListMap[i];
				try{
					pstmt.clearParameters();
					
					pstmt.setString(1,obj.getBcode());
					pstmt.setDate(2,obj.getZdate());
					pstmt.setString(3,obj.getCurrency());
					pstmt.setString(4,obj.getType());
					pstmt.setString(5,obj.getBname());
					pstmt.setBigDecimal(6,obj.getSpot());
					pstmt.setBigDecimal(7,obj.getCash());
//					pstmt.setBigDecimal(8,obj.getF10());
//					pstmt.setBigDecimal(9,obj.getF30());
//					pstmt.setBigDecimal(10,obj.getF60());
//					pstmt.setBigDecimal(11,obj.getF90());
//					pstmt.setBigDecimal(12,obj.getF120());
//					pstmt.setBigDecimal(13,obj.getF180());
					
					pstmt.execute();
					conn.commit();
					Counter++;
				}catch(Exception e){
					logger.error(ErrorTitle.INSERT_TITLE.getTitle(obj.getPK()) , e);
					
					ok[i] = -1;
					conn.rollback();
				}
			}
			pstmt.close();
			logger.info("新增完畢, 共"+Counter+"筆新增");
			Counter = 0;
			
		}catch (SQLException e) {
			try { if (conn != null) conn.rollback(); } catch(Exception ee) { }
			throw e ;
		}catch (Exception e) {
			try { if (conn != null) conn.rollback(); } catch(Exception ee) { }
			throw e ;
		}finally{
            try { if (pstmt != null) pstmt.close();  } catch(Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); conn = null; } catch(Exception e) { e.printStackTrace();}
		}
		
		//此function例外錯誤(log紀錄)由抽象類別(DaoFrame)統一回傳
	}

	/**
	 * 新增
	 * @return
	 * @throws Exception
	 */
	public String getInsertSQL_1(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" INSERT INTO "+tableName);
		sql.append("( bcode,zdate,currency,type,bname,spot,cash)");
		sql.append("  values (   ? , ? , ? , ? , ? , ? , ?  )");
		

				
		return sql.toString();
		
	}	
	/**
	 * 更新
	 * @return
	 * @throws Exception
	 */
	public String getUpadteSQL_1(){
		
		StringBuilder sql = new StringBuilder();

		sql.append(" UPDATE "+tableName);
		sql.append(" SET bname = ? ,spot = ? ,cash = ?"); 
		sql.append(" WHERE (bcode = ?  AND zdate = ?  AND currency = ?  AND type = ?  )");
		


				
		return sql.toString();
		
	}
	
	
	
	  
}
