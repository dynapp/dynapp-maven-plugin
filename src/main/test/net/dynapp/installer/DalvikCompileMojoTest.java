package net.dynapp.installer;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

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
 * Written By: brandon on 21/11/15
 */
public class DalvikCompileMojoTest {

	@Rule
	public ResourceFile res = new ResourceFile("./resources/plugin-test.jar");
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	protected DalvikCompileMojo mojo;

	@Before
	public void setUp() throws Exception {
		mojo = new DalvikCompileMojo();
		mojo.setJarLocation(res.getFile());
		mojo.setBuildDir(folder.newFolder("build"));
		mojo.setBuildName("dalvik-android");
	}

	@Test
	public void testWorkingJar() throws MojoFailureException, MojoExecutionException {
		mojo.execute();
		mojo.getLog().debug("Output: " + mojo.getBuildFile().toString());
	}

	@Test
	public void testDirectoryJar() throws MojoFailureException, MojoExecutionException {
		thrown.expect(MojoFailureException.class);
		thrown.expectMessage("Jar is not a file!");
		mojo.setJarLocation(res.file.getParentFile());
		mojo.execute();
	}
}
