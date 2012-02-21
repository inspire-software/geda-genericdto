

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.BCELMethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.JavassitMethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.ReflectionMethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.SunJavaToolsMethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;
import org.junit.Test;

import java.beans.PropertyDescriptor;

import static org.junit.Assert.*;


/**
 * Test proxy that enables runtime configuration of the assembler method synthesizer.
 * 
 * @author denispavlov
 *
 */
public class MethodSynthesizerProxyTest {

	/**
	 * Test default setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testUndefinedConfigCreatesDefaultInstance() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		proxy.configure(null, null);
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertEquals(syn.getClass().getCanonicalName(), MethodSynthesizerProxy.getDefaultImpl());
	}
	
	/**
	 * Test javassist setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testJavassistConfig() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		proxy.configure("synthesizerImpl", "javassist");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof JavassitMethodSynthesizer);
	}

	/**
	 * Test javassist setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testJavassistConfigConstructor() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy("javassist");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof JavassitMethodSynthesizer);
	}
	
	/**
	 * Test sun tools setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testSunToolsConfig() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		proxy.configure("synthesizerImpl", "suntools");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof SunJavaToolsMethodSynthesizer);
	}
	
	/**
	 * Test sun tools setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testSunToolsConfigConstructor() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy("suntools");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof SunJavaToolsMethodSynthesizer);
	}
	
	/**
	 * Test sun tools setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testReflectionConfig() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		proxy.configure("synthesizerImpl", "reflection");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof ReflectionMethodSynthesizer);
	}
	
	/**
	 * Test sun tools setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testReflectionConfigConstructor() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy("reflection");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof ReflectionMethodSynthesizer);
	}
	
	/**
	 * Test CGLib setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testCGLibConfig() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		proxy.configure("synthesizerImpl", "bcel");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof BCELMethodSynthesizer);
	}
	
	/**
	 * Test CGLib setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testCGLibConfigConstructor() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy("bcel");
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof BCELMethodSynthesizer);
	}
	
	/**
	 * Test class name setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testClassnameConfig() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		proxy.configure("synthesizerImpl", JavassitMethodSynthesizer.class.getCanonicalName());
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof JavassitMethodSynthesizer);
	}
	
	/**
	 * Test class name setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testClassnameConfigConstructor() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy(JavassitMethodSynthesizer.class.getCanonicalName());
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof JavassitMethodSynthesizer);
	}
	
	/**
	 * Test class name setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testInstanceConfig() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		final MethodSynthesizer mock = new MethodSynthesizer() {
			public DataReader synthesizeReader(final PropertyDescriptor descriptor)
					throws InspectionPropertyNotFoundException,
					UnableToCreateInstanceException, GeDARuntimeException { return null; }
			public DataWriter synthesizeWriter(final PropertyDescriptor descriptor)
					throws InspectionPropertyNotFoundException,
					UnableToCreateInstanceException, GeDARuntimeException { return null; }
			public boolean configure(final String configuration, final Object value) { return false; }
		};
		proxy.configure("synthesizerImpl", mock);
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertSame(syn, mock);
	}
	
	/**
	 * Test class name setup.
	 * @throws GeDAException exception
	 */
	@Test
	public void testInstanceConfigConstructor() throws GeDAException {
		final MethodSynthesizer mock = new MethodSynthesizer() {
			public DataReader synthesizeReader(final PropertyDescriptor descriptor)
					throws InspectionPropertyNotFoundException,
					UnableToCreateInstanceException, GeDARuntimeException { return null; }
			public DataWriter synthesizeWriter(final PropertyDescriptor descriptor)
					throws InspectionPropertyNotFoundException,
					UnableToCreateInstanceException, GeDARuntimeException { return null; }
			public boolean configure(final String configuration, final Object value) { return false; }
		};
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy(mock);
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertSame(syn, mock);
	}
	
	/**
	 * Test class name setup.
	 * @throws GeDAException exception
	 */
	@Test(expected = UnableToCreateInstanceException.class)
	public void testWrongConfigThrowsException() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy();
		proxy.configure("synthesizerImpl", new Integer(1));
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof JavassitMethodSynthesizer);
	}
	
	/**
	 * Test class name setup.
	 * @throws GeDAException exception
	 */
	@Test(expected = UnableToCreateInstanceException.class)
	public void testWrongConfigConstructorThrowsException() throws GeDAException {
		final MethodSynthesizerProxy proxy = new MethodSynthesizerProxy(new Integer(1));
		final MethodSynthesizer syn = proxy.getSynthesizer();
		assertTrue(syn instanceof JavassitMethodSynthesizer);
	}
	
}
