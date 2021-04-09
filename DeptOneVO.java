package day0409;

public class DeptOneVO {
//	DEPTNO	DNAME	LOC	//컨 + 쉬 + Y/X = 소/대
	private String dname, loc;
	
	public DeptOneVO() {
	}

	public DeptOneVO(String dname, String loc) {
		this.dname = dname;
		this.loc = loc;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

			
}//class
