package net.dynapp.installer;

import com.android.dx.command.dexer.Main;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * This tool generates a Dalivk jar compatible with the
 * android OS that could be used for a plethora of purposes
 * but is required to do the install process.
 */
@Mojo(name="compile", defaultPhase= LifecyclePhase.INSTALL)
public class DalvikCompileMojo extends DynappMojo {

	/**
	 * This is the location of the input jar. It is important
	 * for the jar to be generated before this task is ran.
	 * After the jar is generated it will be recompiled by the
	 * android dalvik compiler and exported. It then can be used
	 * on any android device as a library or expansion pack.
	 */
	@Parameter(required=true)
	private File jarLocation;

	public void execute() throws MojoFailureException, MojoExecutionException {
		getLog().info("Checking if jar validity...");

		if (!jarLocation.exists()) {
			throw new MojoFailureException("File does not exist!");
		}

		if (!jarLocation.isFile()) {
			throw new MojoFailureException("Jar is not a file!");
		}

		if (!jarLocation.canRead()) {
			throw new MojoFailureException("Cannot read file!");
		}

		getLog().info("Jar is a valid file!");
		getLog().info("Compiling....");

		getBuildDir().mkdirs();

		Main.Arguments arguments = new Main.Arguments();
		arguments.fileNames = new String[]{jarLocation.getAbsolutePath()};
		arguments.jarOutput = true;
		arguments.outName = getBuildFile().getAbsolutePath();
		try {
			Main.run(arguments);
		} catch (IOException e) {
			getLog().error("Unable to compile...");
			throw new MojoExecutionException("Unknown compile failure", e);
		}
		getLog().info("Compiled!");
	}

	protected File getJarLocation() {
		return jarLocation;
	}

	protected void setJarLocation(File jarLocation) {
		this.jarLocation = jarLocation;
	}
}
