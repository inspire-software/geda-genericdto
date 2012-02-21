

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.examples.autowire.TestDto1Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.autowire.TestDto1Interface;
import com.inspiresoftware.lib.dto.geda.assembler.examples.collections.TestDto12CollectionClass;
import com.inspiresoftware.lib.dto.geda.assembler.examples.converter.TestEntity3Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.maps.TestDto12MapToMapClass;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.SunJavaToolsMethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Test for synthesizing classes.
 * 
 * @author DPavlov
 */
public class SunJavaToolsMethodSynthesizerTest {
	
	private static final String WORKING_DIRECTORY = "./target/test-classes/com/inspiresoftware/lib/dto/geda/assembler";

	/**
	 * Test reader creation using class as base for class.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testSynthesizeReaderOnClass() throws GeDAException {
		
		final TestDto1Class dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataReader readerMyString = new SunJavaToolsMethodSynthesizer(WORKING_DIRECTORY).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Class.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Class.class)		
				)		
		);
		
		assertEquals("Hello", readerMyString.read(dto));
		assertEquals(String.class, readerMyString.getReturnType());
		
	}

	/**
	 * Test reader creation using class as base for class when value is primitive.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testSynthesizeReaderOnClassOnPrimitive() throws GeDAException {
		
		final TestEntity3Class dto = new TestEntity3Class();
		dto.setDecision(true);
		
		final DataReader readerMyString = new SunJavaToolsMethodSynthesizer(WORKING_DIRECTORY).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestEntity3Class.class, "decision", 
						PropertyInspector.getPropertyDescriptorsForClass(TestEntity3Class.class)		
				)		
		);
		
		assertTrue((Boolean) readerMyString.read(dto));
		assertEquals(Boolean.class, readerMyString.getReturnType());
		
	}

	/**
	 * Test reader creation using interface as base for class.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testSynthesizeReaderOnInterface() throws GeDAException {
		
		final TestDto1Interface dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataReader readerMyString = new SunJavaToolsMethodSynthesizer(WORKING_DIRECTORY).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Interface.class)		
				)		
		);
		
		assertEquals("Hello", readerMyString.read(dto));
		assertEquals(String.class, readerMyString.getReturnType());
		
	}
	
	/**
	 * Test reader creation with collection properties.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testSynthesizeReaderOnCollectionReturnType() throws GeDAException {
		
		final TestDto12CollectionClass dto = new TestDto12CollectionClass();
		
		final DataReader readerMyString = new SunJavaToolsMethodSynthesizer(WORKING_DIRECTORY).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto12CollectionClass.class, "items", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto12CollectionClass.class)		
				)		
		);
		
		assertNull(readerMyString.read(dto));
		assertEquals(Collection.class, readerMyString.getReturnType());
		
	}
	
	/**
	 * Test reader creation with map properties.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testSynthesizeReaderOnMapReturnType() throws GeDAException {
		
		final TestDto12MapToMapClass dto = new TestDto12MapToMapClass();
		
		final DataReader readerMyString = new SunJavaToolsMethodSynthesizer(WORKING_DIRECTORY).synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto12MapToMapClass.class, "items", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto12MapToMapClass.class)		
				)		
		);
		
		assertNull(readerMyString.read(dto));
		assertEquals(Map.class, readerMyString.getReturnType());
		
	}
	
	/**
	 * Test writer creation using class as base for class.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testSynthesizeWriterOnClass() throws GeDAException {
		
		final TestDto1Class dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataWriter writerMyString = new SunJavaToolsMethodSynthesizer(WORKING_DIRECTORY).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Class.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Class.class)		
				)		
		);
		
		assertEquals(String.class, writerMyString.getParameterType());
		writerMyString.write(dto, "Goodbye");
		assertEquals("Goodbye", dto.getMyString());
		
	}

	/**
	 * Test writer creation using interface as base for class.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testSynthesizeWriterOnInterface() throws GeDAException {
		
		final TestDto1Interface dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataWriter writerMyString = new SunJavaToolsMethodSynthesizer(WORKING_DIRECTORY).synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Interface.class)		
				)		
		);
		
		assertEquals(String.class, writerMyString.getParameterType());
		writerMyString.write(dto, "Goodbye");
		assertEquals("Goodbye", dto.getMyString());
		
	}

}
