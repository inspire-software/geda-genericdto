/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Spring XML parser handler.
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 4:08:07 PM
 */
public class GeDANamespaceHandler extends NamespaceHandlerSupport {

    /** {@inheritDoc} */
    public void init() {
        final AnnotationDrivenGeDABeanDefinitionParser bdpAnnotations = new AnnotationDrivenGeDABeanDefinitionParser();
        this.registerBeanDefinitionParser("annotation-driven", bdpAnnotations);

        final XmlDrivenGeDABeanDefinitionParser bdpInject = new XmlDrivenGeDABeanDefinitionParser();
        this.registerBeanDefinitionParser("xml-driven", bdpInject);
        this.registerBeanDefinitionDecorator("dtosupport", bdpInject);
    }
}
