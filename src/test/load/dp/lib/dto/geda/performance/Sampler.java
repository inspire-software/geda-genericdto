
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.performance;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

import dp.lib.dto.geda.performance.entity.Level1Entity;
import dp.lib.dto.geda.performance.entity.Level2Entity;
import dp.lib.dto.geda.performance.entity.Level3Entity;

/**
 * Service class to build large samples of DTO's and Entities to test GeDA under load and in
 * multi-threading environment.
 * 
 * @author DPavlov
 */
@Ignore
public class Sampler {

	/**
	 * @param id start counter for ids
	 * @param count count of level 1 entities
	 * @param subCount count of level 2 entities
	 * @return spawn sample of {@link Level1Entity} entities
	 */
	public List<Level1Entity> getLevel1Entities(final int id, final int count, final int subCount) {
		final List<Level1Entity> coll = new ArrayList<Level1Entity>();
		for (int i = 0; i < count; i++) {
			coll.add(getLevel1Entity(id + 10000000 * i, subCount));
		}
		return coll;
	}

	/**
	 * @param id id of this entity
	 * @param subCount count of sub entities in the collections
	 * @return level 1 entity
	 */
	public Level1Entity getLevel1Entity(final int id, final int subCount) {
		
		return new Level1Entity(String.valueOf(id),
				"field2" + id,
				"field3" + id,
				"field4" + id,
				"field5" + id,
				"field6" + id,
				"field7" + id,
				"field8" + id,
				"field9" + id,
				"field10" + id,
				"field11" + id,
				"field12" + id,
				"field13" + id,
				"field14" + id,
				"field15" + id,
				"field16" + id,
				"field17" + id,
				"field18" + id,
				"field19" + id,
				"field20" + id,
				getLevel2Entities(id + 10000, subCount),
				getLevel2Entities(id + 20000, subCount),
				getLevel2Entities(id + 30000, subCount),
				getLevel2Entities(id + 40000, subCount),
				getLevel2Entities(id + 50000, subCount)
				);
		
	}
	
	/**
	 * @param id initial id (100 + id)
	 * @param count number of entities to spawn
	 * @return spawn level 2 entities
	 */
	public List<Level2Entity> getLevel2Entities(final int id, final int count) {
		final List<Level2Entity> coll = new ArrayList<Level2Entity>();
		for (int i = 0; i < count; i++) {
			coll.add(getLevel2Entity(id + 100 * i));
		}
		return coll;
	}
	
	/**
	 * @param id id of this entity
	 * @return level 2 entity
	 */
	public Level2Entity getLevel2Entity(final int id) {
		
		return new Level2Entity(String.valueOf(id),
				"field2" + id,
				"field3" + id,
				"field4" + id,
				"field5" + id,
				"field6" + id,
				"field7" + id,
				"field8" + id,
				"field9" + id,
				"field10" + id,
				"field11" + id,
				"field12" + id,
				"field13" + id,
				"field14" + id,
				"field15" + id,
				"field16" + id,
				"field17" + id,
				"field18" + id,
				"field19" + id,
				"field20" + id,
				getLevel3Entity(id + 1),
				getLevel3Entity(id + 2),
				getLevel3Entity(id + 3),
				getLevel3Entity(id + 4),
				getLevel3Entity(id + 5)
				);
		
	}
	
	/**
	 * @param count number of entities to spawn
	 * @return lis of level 3 entities
	 */
	public List<Level3Entity> getLevel3Entities(final int count) {
		final List<Level3Entity> coll = new ArrayList<Level3Entity>();
		for (int i = 0; i < count; i++) {
			coll.add(getLevel3Entity(i));
		}
		return coll;
	}
	
	/**
	 * @param id id
	 * @return level 3 entity instance
	 */
	public Level3Entity getLevel3Entity(final int id) {
		
		return new Level3Entity(String.valueOf(id),
				"field2" + id,
				"field3" + id,
				"field4" + id,
				"field5" + id,
				"field6" + id,
				"field7" + id,
				"field8" + id,
				"field9" + id,
				"field10" + id,
				"field11" + id,
				"field12" + id,
				"field13" + id,
				"field14" + id,
				"field15" + id,
				"field16" + id,
				"field17" + id,
				"field18" + id,
				"field19" + id,
				"field20" + id
				);
		
	}
	
}
