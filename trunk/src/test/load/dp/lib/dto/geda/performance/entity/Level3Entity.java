package com.inspiresoftware.lib.dto.geda.performance.entity;


/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

import org.junit.Ignore;

import com.inspiresoftware.lib.dto.geda.performance.Level3;
import com.inspiresoftware.lib.dto.geda.performance.Verifiable;

/**
 * Level 3 entity.
 * 
 * @author DPavlov
 */
@Ignore
public class Level3Entity implements Verifiable, Level3 {
	
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;
	private String field8;
	private String field9;
	private String field10;
	private String field11;
	private String field12;
	private String field13;
	private String field14;
	private String field15;
	private String field16;
	private String field17;
	private String field18;
	private String field19;
	private String field20;
	
	public Level3Entity() {
		
	}
	
	public Level3Entity(
			String field1, String field2, String field3, String field4, String field5, String field6, 
			String field7, String field8, String field9, String field10, String field11, String field12, 
			String field13, String field14, String field15, String field16, String field17, String field18, 
			String field19, String field20)
	{
		super();
		this.field1 = field1;
		this.field10 = field10;
		this.field11 = field11;
		this.field12 = field12;
		this.field13 = field13;
		this.field14 = field14;
		this.field15 = field15;
		this.field16 = field16;
		this.field17 = field17;
		this.field18 = field18;
		this.field19 = field19;
		this.field2 = field2;
		this.field20 = field20;
		this.field3 = field3;
		this.field4 = field4;
		this.field5 = field5;
		this.field6 = field6;
		this.field7 = field7;
		this.field8 = field8;
		this.field9 = field9;
	}

	public String getField1() {
		return field1;
	}
	
	public void setField1(String field1) {
		this.field1 = field1;
	}
	
	public String getField2() {
		return field2;
	}
	
	public void setField2(String field2) {
		this.field2 = field2;
	}
	
	public String getField3() {
		return field3;
	}
	
	public void setField3(String field3) {
		this.field3 = field3;
	}
	
	public String getField4() {
		return field4;
	}
	
	public void setField4(String field4) {
		this.field4 = field4;
	}
	
	public String getField5() {
		return field5;
	}
	
	public void setField5(String field5) {
		this.field5 = field5;
	}
	
	public String getField6() {
		return field6;
	}
	
	public void setField6(String field6) {
		this.field6 = field6;
	}
	
	public String getField7() {
		return field7;
	}
	
	public void setField7(String field7) {
		this.field7 = field7;
	}
	
	public String getField8() {
		return field8;
	}
	
	public void setField8(String field8) {
		this.field8 = field8;
	}
	
	public String getField9() {
		return field9;
	}
	
	public void setField9(String field9) {
		this.field9 = field9;
	}
	
	public String getField10() {
		return field10;
	}
	
	public void setField10(String field10) {
		this.field10 = field10;
	}
	
	public String getField11() {
		return field11;
	}
	
	public void setField11(String field11) {
		this.field11 = field11;
	}
	
	public String getField12() {
		return field12;
	}
	
	public void setField12(String field12) {
		this.field12 = field12;
	}
	
	public String getField13() {
		return field13;
	}
	
	public void setField13(String field13) {
		this.field13 = field13;
	}
	
	public String getField14() {
		return field14;
	}
	
	public void setField14(String field14) {
		this.field14 = field14;
	}
	
	public String getField15() {
		return field15;
	}
	
	public void setField15(String field15) {
		this.field15 = field15;
	}
	
	public String getField16() {
		return field16;
	}
	
	public void setField16(String field16) {
		this.field16 = field16;
	}
	
	public String getField17() {
		return field17;
	}
	
	public void setField17(String field17) {
		this.field17 = field17;
	}
	
	public String getField18() {
		return field18;
	}
	
	public void setField18(String field18) {
		this.field18 = field18;
	}
	
	public String getField19() {
		return field19;
	}
	
	public void setField19(String field19) {
		this.field19 = field19;
	}
	
	public String getField20() {
		return field20;
	}
	
	public void setField20(String field20) {
		this.field20 = field20;
	}

	public boolean isValid(Object predicate) {
		if (this == predicate)
			return true;
		if (predicate == null)
			return false;
		if (!(predicate instanceof Level3))
			return false;
		Level3 other = (Level3) predicate;
		if (field1 == null) {
			if (other.getField1() != null)
				return false;
		} else if (!field1.equals(other.getField1()))
			return false;
		if (field10 == null) {
			if (other.getField10() != null)
				return false;
		} else if (!field10.equals(other.getField10()))
			return false;
		if (field11 == null) {
			if (other.getField11() != null)
				return false;
		} else if (!field11.equals(other.getField11()))
			return false;
		if (field12 == null) {
			if (other.getField12() != null)
				return false;
		} else if (!field12.equals(other.getField12()))
			return false;
		if (field13 == null) {
			if (other.getField13() != null)
				return false;
		} else if (!field13.equals(other.getField13()))
			return false;
		if (field14 == null) {
			if (other.getField14() != null)
				return false;
		} else if (!field14.equals(other.getField14()))
			return false;
		if (field15 == null) {
			if (other.getField15() != null)
				return false;
		} else if (!field15.equals(other.getField15()))
			return false;
		if (field16 == null) {
			if (other.getField16() != null)
				return false;
		} else if (!field16.equals(other.getField16()))
			return false;
		if (field17 == null) {
			if (other.getField17() != null)
				return false;
		} else if (!field17.equals(other.getField17()))
			return false;
		if (field18 == null) {
			if (other.getField18() != null)
				return false;
		} else if (!field18.equals(other.getField18()))
			return false;
		if (field19 == null) {
			if (other.getField19() != null)
				return false;
		} else if (!field19.equals(other.getField19()))
			return false;
		if (field2 == null) {
			if (other.getField2() != null)
				return false;
		} else if (!field2.equals(other.getField2()))
			return false;
		if (field20 == null) {
			if (other.getField20() != null)
				return false;
		} else if (!field20.equals(other.getField20()))
			return false;
		if (field3 == null) {
			if (other.getField3() != null)
				return false;
		} else if (!field3.equals(other.getField3()))
			return false;
		if (field4 == null) {
			if (other.getField4() != null)
				return false;
		} else if (!field4.equals(other.getField4()))
			return false;
		if (field5 == null) {
			if (other.getField5() != null)
				return false;
		} else if (!field5.equals(other.getField5()))
			return false;
		if (field6 == null) {
			if (other.getField6() != null)
				return false;
		} else if (!field6.equals(other.getField6()))
			return false;
		if (field7 == null) {
			if (other.getField7() != null)
				return false;
		} else if (!field7.equals(other.getField7()))
			return false;
		if (field8 == null) {
			if (other.getField8() != null)
				return false;
		} else if (!field8.equals(other.getField8()))
			return false;
		if (field9 == null) {
			if (other.getField9() != null)
				return false;
		} else if (!field9.equals(other.getField9()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field1 == null) ? 0 : field1.hashCode());
		result = prime * result + ((field10 == null) ? 0 : field10.hashCode());
		result = prime * result + ((field11 == null) ? 0 : field11.hashCode());
		result = prime * result + ((field12 == null) ? 0 : field12.hashCode());
		result = prime * result + ((field13 == null) ? 0 : field13.hashCode());
		result = prime * result + ((field14 == null) ? 0 : field14.hashCode());
		result = prime * result + ((field15 == null) ? 0 : field15.hashCode());
		result = prime * result + ((field16 == null) ? 0 : field16.hashCode());
		result = prime * result + ((field17 == null) ? 0 : field17.hashCode());
		result = prime * result + ((field18 == null) ? 0 : field18.hashCode());
		result = prime * result + ((field19 == null) ? 0 : field19.hashCode());
		result = prime * result + ((field2 == null) ? 0 : field2.hashCode());
		result = prime * result + ((field20 == null) ? 0 : field20.hashCode());
		result = prime * result + ((field3 == null) ? 0 : field3.hashCode());
		result = prime * result + ((field4 == null) ? 0 : field4.hashCode());
		result = prime * result + ((field5 == null) ? 0 : field5.hashCode());
		result = prime * result + ((field6 == null) ? 0 : field6.hashCode());
		result = prime * result + ((field7 == null) ? 0 : field7.hashCode());
		result = prime * result + ((field8 == null) ? 0 : field8.hashCode());
		result = prime * result + ((field9 == null) ? 0 : field9.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return isValid(obj);
	}

}
