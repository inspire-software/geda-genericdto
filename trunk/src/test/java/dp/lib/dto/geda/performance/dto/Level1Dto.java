
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.performance.dto;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoCollection;
import dp.lib.dto.geda.annotations.DtoField;
import dp.lib.dto.geda.performance.Level1;
import dp.lib.dto.geda.performance.Verifiable;
import dp.lib.dto.geda.performance.dto.matcher.Level2DtoMatcher;
import dp.lib.dto.geda.performance.entity.Level1Entity;
import dp.lib.dto.geda.performance.entity.Level2Entity;

/**
 * Level 1 DTO
 * 
 * @author DPavlov
 */
@Dto
@Ignore
public class Level1Dto implements Verifiable, Level1<Level2Dto> {
	
	@DtoField
	private String field1;
	@DtoField
	private String field2;
	@DtoField
	private String field3;
	@DtoField
	private String field4;
	@DtoField
	private String field5;
	@DtoField
	private String field6;
	@DtoField
	private String field7;
	@DtoField
	private String field8;
	@DtoField
	private String field9;
	@DtoField
	private String field10;
	@DtoField
	private String field11;
	@DtoField
	private String field12;
	@DtoField
	private String field13;
	@DtoField
	private String field14;
	@DtoField
	private String field15;
	@DtoField
	private String field16;
	@DtoField
	private String field17;
	@DtoField
	private String field18;
	@DtoField
	private String field19;
	@DtoField
	private String field20;
	
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private List<Level2Dto> collection1;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private List<Level2Dto> collection2;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private List<Level2Dto> collection3;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private List<Level2Dto> collection4;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private List<Level2Dto> collection5;
	
	public Level1Dto() {
	}
	
	public Level1Dto(Level1Entity entity) {
	
		this.setField1(entity.getField1());
		this.setField2(entity.getField2());
		this.setField3(entity.getField3());
		this.setField4(entity.getField4());
		this.setField5(entity.getField5());
		this.setField6(entity.getField6());
		this.setField7(entity.getField7());
		this.setField8(entity.getField8());
		this.setField9(entity.getField9());
		this.setField10(entity.getField10());
		this.setField11(entity.getField11());
		this.setField12(entity.getField12());
		this.setField13(entity.getField13());
		this.setField14(entity.getField14());
		this.setField15(entity.getField15());
		this.setField16(entity.getField16());
		this.setField17(entity.getField17());
		this.setField18(entity.getField18());
		this.setField19(entity.getField19());
		this.setField20(entity.getField20());
		
		this.setCollection1(copyCollection(entity.getCollection1()));
		this.setCollection2(copyCollection(entity.getCollection2()));
		this.setCollection3(copyCollection(entity.getCollection3()));
		this.setCollection4(copyCollection(entity.getCollection4()));
		this.setCollection5(copyCollection(entity.getCollection5()));
	
	}
	
	private List<Level2Dto> copyCollection(List<Level2Entity> entities) {
		final List<Level2Dto> coll = new ArrayList<Level2Dto>();
		for (Level2Entity entity : entities) {
			coll.add(new Level2Dto(entity));
		}
		return coll;
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

	
	public List<Level2Dto> getCollection1() {
		return collection1;
	}

	
	public void setCollection1(List<Level2Dto> collection1) {
		this.collection1 = collection1;
	}

	
	public List<Level2Dto> getCollection2() {
		return collection2;
	}

	
	public void setCollection2(List<Level2Dto> collection2) {
		this.collection2 = collection2;
	}

	
	public List<Level2Dto> getCollection3() {
		return collection3;
	}

	
	public void setCollection3(List<Level2Dto> collection3) {
		this.collection3 = collection3;
	}

	
	public List<Level2Dto> getCollection4() {
		return collection4;
	}

	
	public void setCollection4(List<Level2Dto> collection4) {
		this.collection4 = collection4;
	}

	
	public List<Level2Dto> getCollection5() {
		return collection5;
	}

	
	public void setCollection5(List<Level2Dto> collection5) {
		this.collection5 = collection5;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isValid(Object predicate) {

		if (this == predicate)
			return true;
		if (predicate == null)
			return false;
		if (!(predicate instanceof Level1))
			return false;
		Level1 other = (Level1) predicate;
		if (collection1 == null) {
			if (other.getCollection1() != null)
				return false;
		} else if (collection1.size() != other.getCollection1().size() || !collection1.containsAll(other.getCollection1()))
			return false;
		if (collection2 == null) {
			if (other.getCollection2() != null)
				return false;
		} else if (collection2.size() != other.getCollection2().size() || !collection2.containsAll(other.getCollection2()))
			return false;
		if (collection3 == null) {
			if (other.getCollection3() != null)
				return false;
		} else if (collection3.size() != other.getCollection3().size() || !collection3.containsAll(other.getCollection3()))
			return false;
		if (collection4 == null) {
			if (other.getCollection4() != null)
				return false;
		} else if (collection4.size() != other.getCollection4().size() || !collection4.containsAll(other.getCollection4()))
			return false;
		if (collection5 == null) {
			if (other.getCollection5() != null)
				return false;
		} else if (collection5.size() != other.getCollection5().size() || !collection5.containsAll(other.getCollection5()))
			return false;
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
		result = prime * result + ((collection1 == null) ? 0 : collection1.hashCode());
		result = prime * result + ((collection2 == null) ? 0 : collection2.hashCode());
		result = prime * result + ((collection3 == null) ? 0 : collection3.hashCode());
		result = prime * result + ((collection4 == null) ? 0 : collection4.hashCode());
		result = prime * result + ((collection5 == null) ? 0 : collection5.hashCode());
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
