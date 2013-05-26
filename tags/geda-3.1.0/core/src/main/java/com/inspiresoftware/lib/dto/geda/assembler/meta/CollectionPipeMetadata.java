
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.meta;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.Collection;
import java.util.Map;


/**
 * Metadata specific to collection pipes.
 * 
 * @author DPavlov
 */
@SuppressWarnings("unchecked")
public interface CollectionPipeMetadata extends PipeMetadata {

	/**
	 * @param beanFactory bean factory used during assembly runtime
	 * @return new collection instance.
	 * 
	 * @throws UnableToCreateInstanceException  if unable to create collection instance
	 * @throws BeanFactoryNotFoundException if no bean factory provided
	 */
	Collection newDtoCollection(BeanFactory beanFactory) throws UnableToCreateInstanceException, BeanFactoryNotFoundException;

	/**
	 * @param beanFactory bean factory used during assembly runtime
	 * @return new collection instance.
	 * 
	 * @throws UnableToCreateInstanceException if unable to create collection instance 
	 * @throws BeanFactoryNotFoundException if no bean factory provided
	 */
	Collection newEntityCollection(BeanFactory beanFactory) throws UnableToCreateInstanceException, BeanFactoryNotFoundException;

	/**
	 * @return the entity's collection generic type to identity the type of items in entity collection.
     *
     * @throws BeanFactoryUnableToLocateRepresentationException if unable to locate representative class
     * @throws BeanFactoryNotFoundException if no bean factory provided
     */
	Class< ? > getReturnType(BeanFactory beanFactory) throws BeanFactoryUnableToLocateRepresentationException, BeanFactoryNotFoundException;

	/**
	 * @param converters converters passed during runtime
	 * @return matcher instance that will help synchronize collections.
	 * 
	 * @throws DtoToEntityMatcherNotFoundException when matcher cannot be found in converters map
	 * @throws NotDtoToEntityMatcherException when found converter is not a matcher
	 */
	DtoToEntityMatcher getDtoToEntityMatcher(Map<String, Object> converters)
		throws DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException;

}
