/**
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright Denis Pavlov 2009 
 * Web: http://www.inspire-software.com 
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */
package dp.lib.dto.geda.adapter;

/**
 * Bean Factory for generating Domain Entity bean instances.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public interface BeanFactory {

	/**
	 * @param entityBeanKey string key reference to the bean required
	 * @return new domain entity instance
	 */
	Object get(String entityBeanKey);
	
}
