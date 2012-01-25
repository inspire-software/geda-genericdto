
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.performance;

import java.util.List;

/**
 * Interface to make DTO and Entities compatible for easier assertion.
 * 
 * @author DPavlov
 */
public interface Level1<T extends Level2> extends Verifiable {

	String getField1();

	void setField1(String field1);

	String getField2();

	void setField2(String field2);

	String getField3();

	void setField3(String field3);

	String getField4();

	void setField4(String field4);

	String getField5();

	void setField5(String field5);

	String getField6();

	void setField6(String field6);

	String getField7();

	void setField7(String field7);

	String getField8();

	void setField8(String field8);

	String getField9();

	void setField9(String field9);

	String getField10();

	void setField10(String field10);

	String getField11();

	void setField11(String field11);

	String getField12();

	void setField12(String field12);

	String getField13();

	void setField13(String field13);

	String getField14();

	void setField14(String field14);

	String getField15();

	void setField15(String field15);

	String getField16();

	void setField16(String field16);

	String getField17();

	void setField17(String field17);

	String getField18();

	void setField18(String field18);

	String getField19();

	void setField19(String field19);

	String getField20();

	void setField20(String field20);

	List<T> getCollection1();

	void setCollection1(List<T> collection1);

	List<T> getCollection2();

	void setCollection2(List<T> collection2);

	List<T> getCollection3();

	void setCollection3(List<T> collection3);

	List<T> getCollection4();

	void setCollection4(List<T> collection4);

	List<T> getCollection5();

	void setCollection5(List<T> collection5);
	
	int hashCode();
	
	boolean equals(Object obj);

}
