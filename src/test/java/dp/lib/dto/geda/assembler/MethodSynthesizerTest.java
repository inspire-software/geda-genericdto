

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import dp.lib.dto.geda.assembler.examples.autowire.TestDto1Interface;
import dp.lib.dto.geda.assembler.examples.synth.TestSynthesizerByClass;
import dp.lib.dto.geda.assembler.examples.synth.TestSynthesizerByInterface;
import dp.lib.dto.geda.assembler.examples.synth.TestSynthesizerByInterface.EnumVal;
import dp.lib.dto.geda.assembler.extension.DataReader;
import dp.lib.dto.geda.assembler.extension.DataWriter;
import dp.lib.dto.geda.exception.GeDAException;
import dp.lib.dto.geda.utils.ParameterizedSynthesizer;
import dp.lib.dto.geda.utils.ParameterizedSynthesizer.Parameters;

/**
 * Test for synthesizing classes.
 * 
 * @author DPavlov
 */
@RunWith(value = ParameterizedSynthesizer.class)
public class MethodSynthesizerTest {
	
	private static final boolean BB_T = true;
	private static final Boolean BO_T = Boolean.TRUE;
	
	private static final byte YY_1 = 1;
	private static final Byte YO_1 = Byte.valueOf(YY_1);
	
	private static final char CC_D = 'd';
	private static final Character CO_D = Character.valueOf(CC_D);
	
	private static final short SS_3 = 3;
	private static final Short SO_3 = Short.valueOf(SS_3);

	private static final int II_5 = 5;
	private static final Integer IO_5 = Integer.valueOf(II_5);
	
	private static final float FF_3 = 3.5f;
	private static final Float FO_3 = Float.valueOf(FF_3);
	
	private static final long LL_4 = 4;
	private static final Long LO_4 = Long.valueOf(LL_4);

	private static final double DD_4 = 4.5;
	private static final Double DO_4 = Double.valueOf(DD_4);

	private static final Object OBJ = new Object();
	private static final String STR = new String("STR");
	
	private String synthesizer;
	
	/**
	 * @param synthesizer parameter
	 */
	public MethodSynthesizerTest(final String synthesizer) {
		super();
		this.synthesizer = synthesizer;
	}

	/**
	 * @return synthesizers keys
	 */
	@Parameters
	public static Collection<String[]> data() {
		final List<String[]> params = new ArrayList<String[]>();
		for (final String param : MethodSynthesizerProxy.getAvailableSynthesizers()) {
			params.add(new String[] { param });
		}
		return params;
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPbool() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setBool(BB_T);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "bool", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Boolean.class, reader.getReturnType());
		assertEquals(BO_T, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPbool() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "bool", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Boolean.class, writer.getParameterType());
		writer.write(dto, BB_T);
		assertEquals(BB_T, dto.isBool());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePbool() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setBool(BB_T);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "bool", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Boolean.class, reader.getReturnType());
		assertEquals(BO_T, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePbool() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "bool", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Boolean.class, writer.getParameterType());
		writer.write(dto, BB_T);
		assertEquals(BB_T, dto.isBool());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCbool() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setBoolo(BO_T);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "boolo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Boolean.class, reader.getReturnType());
		assertEquals(BO_T, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCbool() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "boolo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Boolean.class, writer.getParameterType());
		writer.write(dto, BO_T);
		assertEquals(BO_T, dto.getBoolo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCbool() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setBoolo(BO_T);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "boolo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Boolean.class, reader.getReturnType());
		assertEquals(BO_T, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCbool() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "boolo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Boolean.class, writer.getParameterType());
		writer.write(dto, BO_T);
		assertEquals(BO_T, dto.getBoolo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPby() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setBy(YY_1);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "by", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Byte.class, reader.getReturnType());
		assertEquals(YO_1, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPby() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "by", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Byte.class, writer.getParameterType());
		writer.write(dto, YY_1);
		assertEquals(YY_1, dto.getBy());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePby() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setBy(YY_1);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "by", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Byte.class, reader.getReturnType());
		assertEquals(YO_1, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePby() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "by", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Byte.class, writer.getParameterType());
		writer.write(dto, YY_1);
		assertEquals(YY_1, dto.getBy());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCby() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setByo(YO_1);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "byo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Byte.class, reader.getReturnType());
		assertEquals(YO_1, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCby() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "byo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Byte.class, writer.getParameterType());
		writer.write(dto, YO_1);
		assertEquals(YO_1, dto.getByo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCby() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setByo(YO_1);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "byo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Byte.class, reader.getReturnType());
		assertEquals(YO_1, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCby() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "byo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Byte.class, writer.getParameterType());
		writer.write(dto, YO_1);
		assertEquals(YO_1, dto.getByo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPch() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setCh(CC_D);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ch", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Character.class, reader.getReturnType());
		assertEquals(CO_D, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPch() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ch", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Character.class, writer.getParameterType());
		writer.write(dto, CC_D);
		assertEquals(CC_D, dto.getCh());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePch() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setCh(CC_D);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ch", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Character.class, reader.getReturnType());
		assertEquals(CO_D, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePch() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ch", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Character.class, writer.getParameterType());
		writer.write(dto, CC_D);
		assertEquals(CC_D, dto.getCh());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCch() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setCho(CO_D);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "cho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Character.class, reader.getReturnType());
		assertEquals(CO_D, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCch() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "cho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Character.class, writer.getParameterType());
		writer.write(dto, CO_D);
		assertEquals(CO_D, dto.getCho());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCch() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setCho(CO_D);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "cho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Character.class, reader.getReturnType());
		assertEquals(CO_D, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCch() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "cho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Character.class, writer.getParameterType());
		writer.write(dto, CO_D);
		assertEquals(CO_D, dto.getCho());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPsh() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setSh(SS_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sh", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Short.class, reader.getReturnType());
		assertEquals(SO_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPsh() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sh", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Short.class, writer.getParameterType());
		writer.write(dto, SS_3);
		assertEquals(SS_3, dto.getSh());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePsh() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setSh(SS_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sh", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Short.class, reader.getReturnType());
		assertEquals(SO_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePsh() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sh", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Short.class, writer.getParameterType());
		writer.write(dto, SS_3);
		assertEquals(SS_3, dto.getSh());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCsh() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setSho(SO_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Short.class, reader.getReturnType());
		assertEquals(SO_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCsh() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Short.class, writer.getParameterType());
		writer.write(dto, SO_3);
		assertEquals(SO_3, dto.getSho());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCsh() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setSho(SO_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Short.class, reader.getReturnType());
		assertEquals(SO_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCsh() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "sho", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Short.class, writer.getParameterType());
		writer.write(dto, SO_3);
		assertEquals(SO_3, dto.getSho());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPin() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setIn(II_5);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "in", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Integer.class, reader.getReturnType());
		assertEquals(IO_5, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPin() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "in", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Integer.class, writer.getParameterType());
		writer.write(dto, IO_5);
		assertEquals(II_5, dto.getIn());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePin() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setIn(II_5);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "in", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Integer.class, reader.getReturnType());
		assertEquals(IO_5, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePin() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "in", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Integer.class, writer.getParameterType());
		writer.write(dto, IO_5);
		assertEquals(II_5, dto.getIn());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCin() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setIno(IO_5);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ino", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Integer.class, reader.getReturnType());
		assertEquals(IO_5, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCin() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ino", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Integer.class, writer.getParameterType());
		writer.write(dto, IO_5);
		assertEquals(IO_5, dto.getIno());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCin() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setIno(IO_5);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ino", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Integer.class, reader.getReturnType());
		assertEquals(IO_5, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCin() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ino", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Integer.class, writer.getParameterType());
		writer.write(dto, IO_5);
		assertEquals(IO_5, dto.getIno());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPfl() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setFl(FF_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "fl", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Float.class, reader.getReturnType());
		assertEquals(FF_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPfl() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "fl", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Float.class, writer.getParameterType());
		writer.write(dto, FO_3);
		assertEquals(FF_3, dto.getFl(), 0);
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePfl() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setFl(FF_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "fl", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Float.class, reader.getReturnType());
		assertEquals(FO_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePfl() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "fl", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Float.class, writer.getParameterType());
		writer.write(dto, FO_3);
		assertEquals(FO_3, dto.getFl(), 0);
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCfl() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setFlo(FO_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "flo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Float.class, reader.getReturnType());
		assertEquals(FO_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCfl() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "flo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Float.class, writer.getParameterType());
		writer.write(dto, FO_3);
		assertEquals(FO_3, dto.getFlo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCfl() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setFlo(FO_3);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "flo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Float.class, reader.getReturnType());
		assertEquals(FO_3, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCfl() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "flo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Float.class, writer.getParameterType());
		writer.write(dto, FO_3);
		assertEquals(FO_3, dto.getFlo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPlo() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setLo(LL_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "lo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Long.class, reader.getReturnType());
		assertEquals(LL_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPlo() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "lo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Long.class, writer.getParameterType());
		writer.write(dto, LO_4);
		assertEquals(LL_4, dto.getLo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePlo() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setLo(LL_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "lo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Long.class, reader.getReturnType());
		assertEquals(LO_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePlo() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "lo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Long.class, writer.getParameterType());
		writer.write(dto, LO_4);
		assertEquals(LL_4, dto.getLo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassClo() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setLoo(LO_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "loo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Long.class, reader.getReturnType());
		assertEquals(LO_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassClo() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "loo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Long.class, writer.getParameterType());
		writer.write(dto, LO_4);
		assertEquals(LO_4, dto.getLoo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceClo() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setLoo(LO_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "loo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Long.class, reader.getReturnType());
		assertEquals(LO_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceClo() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "loo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Long.class, writer.getParameterType());
		writer.write(dto, LO_4);
		assertEquals(LO_4, dto.getLoo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassPdb() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setDb(DD_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "db", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Double.class, reader.getReturnType());
		assertEquals(DD_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassPdb() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "db", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Double.class, writer.getParameterType());
		writer.write(dto, DD_4);
		assertEquals(DD_4, dto.getDb(), 0);
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfacePdb() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setDb(DD_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "db", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Double.class, reader.getReturnType());
		assertEquals(DD_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfacePdb() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "db", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Double.class, writer.getParameterType());
		writer.write(dto, DD_4);
		assertEquals(DD_4, dto.getDb(), 0);
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCdb() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setDbo(DO_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "dbo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Double.class, reader.getReturnType());
		assertEquals(DO_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCdb() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "dbo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Double.class, writer.getParameterType());
		writer.write(dto, DO_4);
		assertEquals(DO_4, dto.getDbo(), 0);
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCdb() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setDbo(DD_4);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "dbo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Double.class, reader.getReturnType());
		assertEquals(DD_4, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCdb() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "dbo", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Double.class, writer.getParameterType());
		writer.write(dto, DO_4);
		assertEquals(DO_4, dto.getDbo());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCob() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setOb(OBJ);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ob", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Object.class, reader.getReturnType());
		assertEquals(OBJ, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCob() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ob", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(Object.class, writer.getParameterType());
		writer.write(dto, OBJ);
		assertEquals(OBJ, dto.getOb());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCob() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setOb(OBJ);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ob", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Object.class, reader.getReturnType());
		assertEquals(OBJ, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCob() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "ob", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(Object.class, writer.getParameterType());
		writer.write(dto, OBJ);
		assertEquals(OBJ, dto.getOb());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCstr() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setStr(STR);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "str", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(String.class, reader.getReturnType());
		assertEquals(STR, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCstr() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "str", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(String.class, writer.getParameterType());
		writer.write(dto, STR);
		assertEquals(STR, dto.getStr());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCstr() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setStr(STR);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "str", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(String.class, reader.getReturnType());
		assertEquals(STR, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCstr() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "str", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(String.class, writer.getParameterType());
		writer.write(dto, STR);
		assertEquals(STR, dto.getStr());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnClassCenum() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		dto.setEnum(EnumVal.Three);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "enum", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(EnumVal.class, reader.getReturnType());
		assertEquals(EnumVal.Three, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnClassCenum() throws GeDAException {
		
		final TestSynthesizerByClass dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "enum", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByClass.class)		
				)		
		);
		
		assertEquals(EnumVal.class, writer.getParameterType());
		writer.write(dto, EnumVal.Three);
		assertEquals(EnumVal.Three, dto.getEnum());
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testReaderOnIfaceCenum() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		dto.setEnum(EnumVal.Three);
		
		final DataReader reader = new MethodSynthesizerProxy(this.synthesizer).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "enum", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(EnumVal.class, reader.getReturnType());
		assertEquals(EnumVal.Three, reader.read(dto));
		
	}
	
	/**
	 * Test reader synthesizer.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testWriterOnIfaceCenum() throws GeDAException {
		
		final TestSynthesizerByInterface dto = new TestSynthesizerByClass();
		
		final DataWriter writer = new MethodSynthesizerProxy(this.synthesizer).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "enum", 
						PropertyInspector.getPropertyDescriptorsForClass(TestSynthesizerByInterface.class)		
				)		
		);
		
		assertEquals(EnumVal.class, writer.getParameterType());
		writer.write(dto, EnumVal.Three);
		assertEquals(EnumVal.Three, dto.getEnum());
		
	}

}
