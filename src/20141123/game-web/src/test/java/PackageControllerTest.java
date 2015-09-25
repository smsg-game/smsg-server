import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.web.controller.PackageController;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class PackageControllerTest extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private PackageController packageController;
	
	@Test
	public void checkStaticPackage() throws IOException {
		this.packageController.checkStaticFiles(null, null);
	}
	
	@Test 
	public void binarySearchTest() {
		String[] uids = {"00b9d493516249b9ac8a3b5b4a546aa7", "013c5541719b4de7bfadf61d8a8c2d8c", "042a951765944a82af64fb100e6b7a8d"};
		Arrays.sort(uids);
		int index = Arrays.binarySearch(uids, "00b9d493516249b9ac8a3b5b4a546aa7");
		System.out.println(index);
	}
}
