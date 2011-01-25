package dp.lib.dto.geda.performance.dto;

import java.util.ArrayList;
import java.util.Collection;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoCollection;
import dp.lib.dto.geda.annotations.DtoField;
import dp.lib.dto.geda.performance.dto.matcher.Level2DtoMatcher;
import dp.lib.dto.geda.performance.entity.Level1Entity;
import dp.lib.dto.geda.performance.entity.Level2Entity;


@Dto
public class Level1Dto
{
	
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
	private Collection<Level2Dto> collection1;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private Collection<Level2Dto> collection2;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private Collection<Level2Dto> collection3;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private Collection<Level2Dto> collection4;
	@DtoCollection(dtoToEntityMatcher = Level2DtoMatcher.class, entityGenericType = Level2Entity.class, dtoBeanKey = "lvl2")
	private Collection<Level2Dto> collection5;
	
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
	
	private Collection<Level2Dto> copyCollection(Collection<Level2Entity> entities) {
		final Collection<Level2Dto> coll = new ArrayList<Level2Dto>();
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

	
	public Collection<Level2Dto> getCollection1() {
		return collection1;
	}

	
	public void setCollection1(Collection<Level2Dto> collection1) {
		this.collection1 = collection1;
	}

	
	public Collection<Level2Dto> getCollection2() {
		return collection2;
	}

	
	public void setCollection2(Collection<Level2Dto> collection2) {
		this.collection2 = collection2;
	}

	
	public Collection<Level2Dto> getCollection3() {
		return collection3;
	}

	
	public void setCollection3(Collection<Level2Dto> collection3) {
		this.collection3 = collection3;
	}

	
	public Collection<Level2Dto> getCollection4() {
		return collection4;
	}

	
	public void setCollection4(Collection<Level2Dto> collection4) {
		this.collection4 = collection4;
	}

	
	public Collection<Level2Dto> getCollection5() {
		return collection5;
	}

	
	public void setCollection5(Collection<Level2Dto> collection5) {
		this.collection5 = collection5;
	}
	
	

}
