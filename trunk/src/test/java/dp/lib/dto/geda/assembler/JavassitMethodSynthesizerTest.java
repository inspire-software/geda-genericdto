

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

import org.junit.Test;

/**
 * Test for synthesizing classes.
 * 
 * @author DPavlov
 */
public class JavassitMethodSynthesizerTest
{
	
	@Test
	public void testSynthesizeReaderOnClass() {
		
		final TestDto1Class dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataReader readerMyString = new JavassitMethodSynthesizer().synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Class.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Class.class)		
				)		
		);
		
		assertEquals("Hello", readerMyString.read(dto));
		
	}

	@Test
	public void testSynthesizeReaderOnInterface() {
		
		final TestDto1Interface dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataReader readerMyString = new JavassitMethodSynthesizer().synthesizeReader(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Interface.class)		
				)		
		);
		
		assertEquals("Hello", readerMyString.read(dto));
		
	}
	
	@Test
	public void testSynthesizeWriterOnClass() {
		
		final TestDto1Class dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataWriter readerMyString = new JavassitMethodSynthesizer().synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Class.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Class.class)		
				)		
		);
		
		readerMyString.write(dto, "Goodbye");
		assertEquals("Goodbye", dto.getMyString());
		
	}

	@Test
	public void testSynthesizeWriterOnInterface() {
		
		final TestDto1Interface dto = new TestDto1Class();
		dto.setMyString("Hello");
		
		final DataWriter readerMyString = new JavassitMethodSynthesizer().synthesizeWriter(
				PropertyInspector.getDtoPropertyDescriptorForField(
						TestDto1Interface.class, "myString", 
						PropertyInspector.getPropertyDescriptorsForClass(TestDto1Interface.class)		
				)		
		);
		
		readerMyString.write(dto, "Goodbye");
		assertEquals("Goodbye", dto.getMyString());
		
	}

}
