/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.synth;

import org.junit.Ignore;

/**
 * Basic synthesizer test interface.
 * 
 * @author denispavlov
 *
 */
@Ignore
public interface TestSynthesizerByInterface {
	
	/** enum val. */
	enum EnumVal { One, Two, Three }
	
	/** @return val */
	EnumVal getEnum();
	/** @param enumVal val */
	void setEnum(EnumVal enumVal);

	/** @return val */
	boolean isBool();
	/** @param bool val */
	void setBool(boolean bool);

	/** @return val */
	byte getBy();
	/** @param by val */
	void setBy(byte by);

	/** @return val */
	char getCh();
	/** @param ch val */
	void setCh(char ch);

	/** @return val */
	short getSh();
	/** @param sh val */
	void setSh(short sh);

	/** @return val */
	int getIn();
	/** @param in val */
	void setIn(int in);

	/** @return val */
	float getFl();
	/** @param fl val */
	void setFl(float fl);

	/** @return val */
	long getLo();
	/** @param lo val */
	void setLo(long lo);

	/** @return val */
	double getDb();
	/** @param db val */
	void setDb(double db);

	/** @return val */
	Boolean getBoolo();
	/** @param boolo val */
	void setBoolo(Boolean boolo);

	/** @return val */
	Byte getByo();
	/** @param byo val */
	void setByo(Byte byo);

	/** @return val */
	Character getCho();
	/** @param cho val */
	void setCho(Character cho);

	/** @return val */
	Short getSho();
	/** @param sho val */
	void setSho(Short sho);

	/** @return val */
	Integer getIno();
	/** @param ino val */
	void setIno(Integer ino);

	/** @return val */
	Float getFlo();
	/** @param flo val */
	void setFlo(Float flo);

	/** @return val */
	Long getLoo();
	/** @param loo val */
	void setLoo(Long loo);

	/** @return val */
	Double getDbo();
	/** @param dbo val */
	void setDbo(Double dbo);

	/** @return val */
	Object getOb();
	/** @param ob val */
	void setOb(Object ob);

	/** @return val */
	String getStr();
	/** @param str val */
	void setStr(String str);

}