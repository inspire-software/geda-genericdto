/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.testng.Assert.*;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 6:06:16 PM
 */
public class AdviceConfigResolverImplTest {

    public static class TestMethods {

        public void methAnn1() {

        }

        public void methAnn2() {

        }

        public void methBlack1() {

        }

        public void methBlack2() {

        }

    }

    @Test
    public void testResolveMethod() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final Method methAnn1 = TestMethods.class.getMethod("methAnn1");
        final Method methAnn2 = TestMethods.class.getMethod("methAnn2");
        final Method methBlack1 = TestMethods.class.getMethod("methBlack1");
        final Method methBlack2 = TestMethods.class.getMethod("methBlack2");

        final Map cfg1 = ctrl.createMock("cfg1", Map.class);
        final Map cfg2 = ctrl.createMock("cfg2", Map.class);

        final RuntimeAdviceConfigResolverImpl resolver = new RuntimeAdviceConfigResolverImpl() {

            private int count = 0;

            @Override
            Map<Occurrence, AdviceConfig> resolveConfiguration(final Method method,
                                                              final Class<?> targetClass) {
                assertTrue(count < 4);
                if (method == methAnn1) {
                    count++;
                    return cfg1;
                } else if (method == methAnn2) {
                    count++;
                    return cfg2;
                }
                return Collections.emptyMap();
            }

        };

        expect(cfg1.isEmpty()).andReturn(false).anyTimes();
        expect(cfg2.isEmpty()).andReturn(false).anyTimes();

        ctrl.replay();

        final Map<Occurrence, AdviceConfig> map1 = resolver.resolve(methAnn1, null);
        assertNotNull(map1);
        assertSame(map1, cfg1);

        final Map<Occurrence, AdviceConfig> map1_1 = resolver.resolve(methAnn1, null);
        assertNotNull(map1_1);
        assertSame(map1_1, cfg1);

        assertSame(map1, map1_1);

        final Map<Occurrence, AdviceConfig> map1_2 = resolver.resolve(methAnn1, null);
        assertNotNull(map1_2);
        assertSame(map1_2, cfg1);

        assertSame(map1, map1_2);

        assertTrue(resolver.isCached(resolver.methodCacheKey(methAnn1, null)));
        assertFalse(resolver.isBlacklisted(resolver.methodCacheKey(methAnn1, null)));

        final Map<Occurrence, AdviceConfig> map2 = resolver.resolve(methAnn2, null);
        assertNotNull(map2);
        assertSame(map2, cfg2);

        final Map<Occurrence, AdviceConfig> map2_1 = resolver.resolve(methAnn2, null);
        assertNotNull(map2_1);
        assertSame(map2_1, cfg2);

        final Map<Occurrence, AdviceConfig> map2_2 = resolver.resolve(methAnn2, null);
        assertNotNull(map2_2);
        assertSame(map2_2, cfg2);

        assertSame(map2, map2_1);

        assertTrue(resolver.isCached(resolver.methodCacheKey(methAnn2, null)));
        assertFalse(resolver.isBlacklisted(resolver.methodCacheKey(methAnn2, null)));


        final Map<Occurrence, AdviceConfig> map3 = resolver.resolve(methBlack1, null);
        assertNotNull(map3);
        assertTrue(map3.isEmpty());

        final Map<Occurrence, AdviceConfig> map3_1 = resolver.resolve(methBlack1, null);
        assertNotNull(map3_1);
        assertTrue(map3_1.isEmpty());

        assertSame(map3, map3_1);

        assertFalse(resolver.isCached(resolver.methodCacheKey(methBlack1, null)));
        assertTrue(resolver.isBlacklisted(resolver.methodCacheKey(methBlack1, null)));


        final Map<Occurrence, AdviceConfig> map4 = resolver.resolve(methBlack2, null);
        assertNotNull(map4);
        assertTrue(map4.isEmpty());

        final Map<Occurrence, AdviceConfig> map4_1 = resolver.resolve(methBlack2, null);
        assertNotNull(map4_1);
        assertTrue(map4_1.isEmpty());

        assertSame(map4, map4_1);

        assertFalse(resolver.isCached(resolver.methodCacheKey(methBlack2, null)));
        assertTrue(resolver.isBlacklisted(resolver.methodCacheKey(methBlack2, null)));

        ctrl.verify();

    }

    public static class TestMethodsProxy extends TestMethods {

    }

    @Test
    public void testResolveMethodByClass() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final Method methAnn1 = TestMethods.class.getMethod("methAnn1");
        final Method methBlack1 = TestMethods.class.getMethod("methBlack1");

        final Map cfg1 = ctrl.createMock("cfg1", Map.class);
        final Map cfg2 = ctrl.createMock("cfg2", Map.class);

        final RuntimeAdviceConfigResolverImpl resolver = new RuntimeAdviceConfigResolverImpl() {

            private int count = 0;

            @Override
            Map<Occurrence, AdviceConfig> resolveConfiguration(final Method method,
                                                              final Class<?> targetClass) {
                assertTrue(count < 3);
                if (method == methAnn1) {
                    count++;
                    return cfg1;
                }
                return Collections.emptyMap();
            }

        };

        expect(cfg1.isEmpty()).andReturn(false).anyTimes();
        expect(cfg2.isEmpty()).andReturn(false).anyTimes();

        ctrl.replay();

        final Map<Occurrence, AdviceConfig> map1 = resolver.resolve(methAnn1, null);
        assertNotNull(map1);
        assertSame(map1, cfg1);

        final Map<Occurrence, AdviceConfig> map1_1 = resolver.resolve(methAnn1, TestMethods.class);
        assertNotNull(map1_1);
        assertSame(map1_1, cfg1);

        assertSame(map1, map1_1);

        final Map<Occurrence, AdviceConfig> map1_2 = resolver.resolve(methAnn1, TestMethodsProxy.class);
        assertNotNull(map1_2);
        assertSame(map1_2, cfg1);

        assertSame(map1, map1_2);

        assertTrue(resolver.isCached(resolver.methodCacheKey(methAnn1, null)));
        assertTrue(resolver.isCached(resolver.methodCacheKey(methAnn1, TestMethods.class)));
        assertTrue(resolver.isCached(resolver.methodCacheKey(methAnn1, TestMethodsProxy.class)));
        assertFalse(resolver.isBlacklisted(resolver.methodCacheKey(methAnn1, null)));
        assertFalse(resolver.isBlacklisted(resolver.methodCacheKey(methAnn1, TestMethods.class)));
        assertFalse(resolver.isBlacklisted(resolver.methodCacheKey(methAnn1, TestMethodsProxy.class)));



        final Map<Occurrence, AdviceConfig> map3 = resolver.resolve(methBlack1, null);
        assertNotNull(map3);
        assertTrue(map3.isEmpty());

        final Map<Occurrence, AdviceConfig> map3_1 = resolver.resolve(methBlack1, TestMethodsProxy.class);
        assertNotNull(map3_1);
        assertTrue(map3_1.isEmpty());

        assertSame(map3, map3_1);

        assertFalse(resolver.isCached(resolver.methodCacheKey(methBlack1, null)));
        assertFalse(resolver.isCached(resolver.methodCacheKey(methBlack1, TestMethods.class)));
        assertFalse(resolver.isCached(resolver.methodCacheKey(methBlack1, TestMethodsProxy.class)));
        assertTrue(resolver.isBlacklisted(resolver.methodCacheKey(methBlack1, null)));
        assertTrue(resolver.isBlacklisted(resolver.methodCacheKey(methBlack1, TestMethods.class)));
        assertTrue(resolver.isBlacklisted(resolver.methodCacheKey(methBlack1, TestMethodsProxy.class)));


        ctrl.verify();

    }

}
