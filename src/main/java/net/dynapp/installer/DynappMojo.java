package net.dynapp.installer;

import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

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
public abstract class DynappMojo extends AbstractMojo {

	private static final String DEVICE_PATH = "/sdcard/FIRST/";
	/**
	 * This is the location where the dalvik compiled Jar will go.
	 * This must be a directory and it does not have to exist.
	 * If you do not have a custom location to place to jar then
	 * there is no reason to change this value.
	 */
	@Parameter(required = false, defaultValue = "${project.build.directory}")
	private File buildDir;

	/**
	 * This is the name of the .jar file. The .jar suffix is automatically
	 * appended on during file generation so do not put the file type in
	 * for the value. Also certain characters are removed from the file
	 * to prevent OS errors. It is reccommended not to modify this name.
	 */
	@Parameter(required = false, defaultValue = "dalvik-android")
	private String buildName;

	/**
	 * The version and groupId needed to export the jar.
	 */
	@Parameter(defaultValue="${project}", readonly=true, required=true)
	private MavenProject project;

	public String getExportPath() {
		Model model;
		if (project == null) {
			model = new Model();
			model.setArtifactId("unknown-plugin");
			model.setVersion("0.0.0");
		} else {
			model = project.getModel();
		}
		String path = DEVICE_PATH + model.getArtifactId().replaceAll("[^a-zA-Z0-9.-]", "-").replaceAll("-+", "-");
		path = path.endsWith("-") ? path.substring(0, path.length() - 1) : path;
		return path + "/" + model.getVersion() + ".jar";
	}

	public File getBuildFile() {
		return new File(getBuildDir(), getBuildName());
	}

	public File getBuildDir() {
		return buildDir;
	}

	public String getBuildName() {
		int index = buildName.lastIndexOf('.');
		return (index > 0 ? buildName.substring(0, index) : buildName).replaceAll("[^a-zA-Z0-9.-]", "-") + ".jar";
	}

	protected void setBuildDir(File buildDir) {
		this.buildDir = buildDir;
	}

	protected void setBuildName(String buildName) {
		this.buildName = buildName;
	}
}
