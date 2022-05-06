package YRATE;

import java.sql.Date;

import org.ini4j.spi.BeanAccess;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import java.math.BigDecimal;


public class Txwebch {
	
	private String bcode;
	private Date zdate;
	private String currency;
	private String type;
	private String bname;
	private BigDecimal spot;
	private BigDecimal cash;
	private BigDecimal f10;
	private BigDecimal f30;
	private BigDecimal f60;
	private BigDecimal f90;
	private BigDecimal f120;
	private BigDecimal f150;
	private BigDecimal f180;
	private BigDecimal f210;
	private BigDecimal f240;
	private BigDecimal f270;
	private BigDecimal f300;
	private BigDecimal f330;
	private BigDecimal f360;
	public String getBcode(){
		return bcode;
	}
	public void setBcode(String Bcode ){
		this.bcode = Bcode;
	}
	public Date getZdate(){
		return zdate;
	}
	public void setZdate(Date Zdate ){
		this.zdate = Zdate;
	}
	public String getCurrency(){
		return currency;
	}
	public void setCurrency(String Currency ){
		this.currency = Currency;
	}
	public String getType(){
		return type;
	}
	public void setType(String Type ){
		this.type = Type;
	}
	public String getBname(){
		return bname;
	}
	public void setBname(String Bname ){
		this.bname = Bname;
	}
	public BigDecimal getSpot(){
		return spot;
	}
	public void setSpot(BigDecimal Spot ){
		this.spot = Spot;
	}
	public BigDecimal getCash(){
		return cash;
	}
	public void setCash(BigDecimal Cash ){
		this.cash = Cash;
	}
	public BigDecimal getF10(){
		return f10;
	}
	public void setF10(BigDecimal F10 ){
		this.f10 = F10;
	}
	public BigDecimal getF30(){
		return f30;
	}
	public void setF30(BigDecimal F30 ){
		this.f30 = F30;
	}
	public BigDecimal getF60(){
		return f60;
	}
	public void setF60(BigDecimal F60 ){
		this.f60 = F60;
	}
	public BigDecimal getF90(){
		return f90;
	}
	public void setF90(BigDecimal F90 ){
		this.f90 = F90;
	}
	public BigDecimal getF120(){
		return f120;
	}
	public void setF120(BigDecimal F120 ){
		this.f120 = F120;
	}
	public BigDecimal getF150(){
		return f150;
	}
	public void setF150(BigDecimal F150 ){
		this.f150 = F150;
	}
	public BigDecimal getF180(){
		return f180;
	}
	public void setF180(BigDecimal F180 ){
		this.f180 = F180;
	}
	public BigDecimal getF210(){
		return f210;
	}
	public void setF210(BigDecimal F210 ){
		this.f210 = F210;
	}
	public BigDecimal getF240(){
		return f240;
	}
	public void setF240(BigDecimal F240 ){
		this.f240 = F240;
	}
	public BigDecimal getF270(){
		return f270;
	}
	public void setF270(BigDecimal F270 ){
		this.f270 = F270;
	}
	public BigDecimal getF300(){
		return f300;
	}
	public void setF300(BigDecimal F300 ){
		this.f300 = F300;
	}
	public BigDecimal getF330(){
		return f330;
	}
	public void setF330(BigDecimal F330 ){
		this.f330 = F330;
	}
	public BigDecimal getF360(){
		return f360;
	}
	public void setF360(BigDecimal F360 ){
		this.f360 = F360;
	}
	public String getPK(){
		return  bcode+","+zdate+","+currency+","+type;
	}
	
	@Override
	public String toString() {
		return "A [bcode=" + bcode + ", currency=" + currency + ", zdate=" + zdate + ", type="
				+ type + ", bname=" + bname + ", spot=" + spot + ", cash=" + cash + ", F10="+f10+", F30="+f30+", F60="+f60+", F90="+f90+", F120="+f120+", F180="+f180+ "]";
	}

}
