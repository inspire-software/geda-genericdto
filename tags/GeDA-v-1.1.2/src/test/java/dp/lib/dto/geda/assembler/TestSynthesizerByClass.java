/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import org.junit.Ignore;


/**
 * Basic class test.
 * 
 * @author denispavlov
 *
 */
@Ignore
public class TestSynthesizerByClass implements TestSynthesizerByInterface {

	// primitives
	private boolean bool;
	private byte by;
	private char ch;
	private short sh;
	private int in;
	private float fl;
	private long lo;
	private double db;

	// wrappers
	private Boolean boolo;
	private Byte byo;
	private Character cho;
	private Short sho;
	private Integer ino;
	private Float flo;
	private Long loo;
	private Double dbo;

	// refs
	private Object ob;
	private String str;
	
	private EnumVal enumVal;
	
	/** {@inheritDoc} */
	public EnumVal getEnum() {
		return enumVal;
	}
	/** {@inheritDoc} */
	public void setEnum(final EnumVal enumVal) {
		this.enumVal = enumVal;
	}
	/** {@inheritDoc} */
	public boolean isBool() {
		return bool;
	}
	/** {@inheritDoc} */
	public void setBool(final boolean bool) {
		this.bool = bool;
	}
	/** {@inheritDoc} */
	public byte getBy() {
		return by;
	}
	/** {@inheritDoc} */
	public void setBy(final byte by) {
		this.by = by;
	}
	/** {@inheritDoc} */
	public char getCh() {
		return ch;
	}
	/** {@inheritDoc} */
	public void setCh(final char ch) {
		this.ch = ch;
	}
	/** {@inheritDoc} */
	public short getSh() {
		return sh;
	}
	/** {@inheritDoc} */
	public void setSh(final short sh) {
		this.sh = sh;
	}
	/** {@inheritDoc} */
	public int getIn() {
		return in;
	}
	/** {@inheritDoc} */
	public void setIn(final int in) {
		this.in = in;
	}
	/** {@inheritDoc} */
	public float getFl() {
		return fl;
	}
	/** {@inheritDoc} */
	public void setFl(final float fl) {
		this.fl = fl;
	}
	/** {@inheritDoc} */
	public long getLo() {
		return lo;
	}
	/** {@inheritDoc} */
	public void setLo(final long lo) {
		this.lo = lo;
	}
	/** {@inheritDoc} */
	public double getDb() {
		return db;
	}
	/** {@inheritDoc} */
	public void setDb(final double db) {
		this.db = db;
	}
	/** {@inheritDoc} */
	public Boolean getBoolo() {
		return boolo;
	}
	/** {@inheritDoc} */
	public void setBoolo(final Boolean boolo) {
		this.boolo = boolo;
	}
	/** {@inheritDoc} */
	public Byte getByo() {
		return byo;
	}
	/** {@inheritDoc} */
	public void setByo(final Byte byo) {
		this.byo = byo;
	}
	/** {@inheritDoc} */
	public Character getCho() {
		return cho;
	}
	/** {@inheritDoc} */
	public void setCho(final Character cho) {
		this.cho = cho;
	}
	/** {@inheritDoc} */
	public Short getSho() {
		return sho;
	}
	/** {@inheritDoc} */
	public void setSho(final Short sho) {
		this.sho = sho;
	}
	/** {@inheritDoc} */
	public Integer getIno() {
		return ino;
	}
	/** {@inheritDoc} */
	public void setIno(final Integer ino) {
		this.ino = ino;
	}
	/** {@inheritDoc} */
	public Float getFlo() {
		return flo;
	}
	/** {@inheritDoc} */
	public void setFlo(final Float flo) {
		this.flo = flo;
	}
	/** {@inheritDoc} */
	public Long getLoo() {
		return loo;
	}
	/** {@inheritDoc} */
	public void setLoo(final Long loo) {
		this.loo = loo;
	}
	/** {@inheritDoc} */
	public Double getDbo() {
		return dbo;
	}
	/** {@inheritDoc} */
	public void setDbo(final Double dbo) {
		this.dbo = dbo;
	}
	/** {@inheritDoc} */
	public Object getOb() {
		return ob;
	}
	/** {@inheritDoc} */
	public void setOb(final Object ob) {
		this.ob = ob;
	}
	/** {@inheritDoc} */
	public String getStr() {
		return str;
	}
	/** {@inheritDoc} */
	public void setStr(final String str) {
		this.str = str;
	}

}
