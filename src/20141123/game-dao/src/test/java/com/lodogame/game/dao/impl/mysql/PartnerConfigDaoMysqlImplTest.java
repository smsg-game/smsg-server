/**
 * @author : langgui

 * Created : 05/15/2013

 */

package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.lodogame.game.dao.PartnerConfigDao;
import com.lodogame.model.PartnerConfig;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class PartnerConfigDaoMysqlImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private PartnerConfigDao partnerConfigDaoMysqlImpl;

	/**
	 * 
	 * @see com.lodogame.game.dao.impl.mysql.PartnerConfigDaoMysqlImpl#getById(String)
	 */
	@Test
	public void getById() {
		PartnerConfig partnerConfig = partnerConfigDaoMysqlImpl.getById("1002");
		AssertJUnit.assertNull(partnerConfig);
		partnerConfig = partnerConfigDaoMysqlImpl.getById("1001");
		AssertJUnit.assertNotNull(partnerConfig);
	}

	public PartnerConfigDao getPartnerConfigDaoMysqlImpl() {
		return partnerConfigDaoMysqlImpl;
	}

	public void setPartnerConfigDaoMysqlImpl(PartnerConfigDao partnerConfigDaoMysqlImpl) {
		this.partnerConfigDaoMysqlImpl = partnerConfigDaoMysqlImpl;
	}

}
