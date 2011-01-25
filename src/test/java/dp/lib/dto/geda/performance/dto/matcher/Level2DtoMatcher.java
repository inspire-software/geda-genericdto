package dp.lib.dto.geda.performance.dto.matcher;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.performance.dto.Level2Dto;
import dp.lib.dto.geda.performance.entity.Level2Entity;


public class Level2DtoMatcher implements DtoToEntityMatcher<Level2Dto, Level2Entity>
{

	public boolean match(Level2Dto dto, Level2Entity entity) {
		return dto != null && dto.getField1() != null && entity != null && dto.getField1().equals(entity.getField1());
	}

}
