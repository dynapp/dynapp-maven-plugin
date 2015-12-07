package net.dynapp.installer;

import com.android.ddmlib.AndroidDebugBridge;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.Map;

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Written By: brandon on 7/12/15
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MavenInstallTests {

	private static final String ENV_VAR = "ADB_LOC";

	@Rule
	public ResourceFile jar = new ResourceFile("./resources/plugin-test.jar");

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private AdbInstallMojo mojo;

	@Before
	public void setUp() throws Exception {
		mojo = new AdbInstallMojo();
		mojo.setAdbLocation(getADBLocation());
		mojo.setJarLocation(jar.getFile());
		mojo.setBuildDir(folder.newFolder("build"));
		mojo.setBuildName("dalvik-android");
	}

	@Test
	public void testSuccessfulInstall() throws Exception {
		AndroidDebugBridge.initIfNeeded(false);
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge(getADBLocation().getAbsolutePath(), true);

		int trials = 10;
		while (trials > 0) {
			Thread.sleep(50);
			if (adb.isConnected()) {
				break;
			}
			trials--;
		}

		if (!adb.isConnected()) {
			throw new Exception("Couldn't connect to ADB server");
		}

		trials = 10;
		while (trials > 0) {
			Thread.sleep(50);
			if (adb.hasInitialDeviceList()) {
				break;
			}
			trials--;
		}

		if (!adb.hasInitialDeviceList()) {
			throw new Exception("Couldn't list connected devices");
		}

		trials = 250;
		mojo.getLog().info("Waiting for device(s)...");
		while (trials > 0) {
			Thread.sleep(50);
			if (adb.getDevices().length > 0) {
				break;
			}
			trials--;
		}
		if (adb.getDevices().length <= 0) {
			throw new Exception("Failed to find device! Please connect ADB enabled android device!");
		}
		mojo.getLog().info("Found: " + adb.getDevices()[0].getName());
		mojo.setDeviceIds(new String[]{adb.getDevices()[0].getSerialNumber()});
		mojo.execute();
	}

	private File getADBLocation() throws Exception {
		Map<String, String> vars = System.getenv();
		String path;
		if (!vars.containsKey(ENV_VAR)) {
			throw new Exception("Use the enviorment variable: " + ENV_VAR + " to set the ADB location.");
		} else {
			path = vars.get(ENV_VAR);
		}
		return new File(path);
	}
}
