package dp.lib.dto.geda.performance;

import java.util.ArrayList;
import java.util.Collection;

import dp.lib.dto.geda.performance.entity.Level1Entity;
import dp.lib.dto.geda.performance.entity.Level2Entity;
import dp.lib.dto.geda.performance.entity.Level3Entity;


public class Sampler
{

	public Collection<Level1Entity> getLevel1Entities(final int id, final int count, final int subCount) {
		final Collection<Level1Entity> coll = new ArrayList<Level1Entity>();
		for (int i = 0; i < count; i++) {
			coll.add(getLevel1Entity(id + 10000000 * i, subCount));
		}
		return coll;
	}

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
	
	public Collection<Level2Entity> getLevel2Entities(final int id, final int count) {
		final Collection<Level2Entity> coll = new ArrayList<Level2Entity>();
		for (int i = 0; i < count; i++) {
			coll.add(getLevel2Entity(id + 100 * i));
		}
		return coll;
	}
	
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
