package net.dynapp.installer;

import com.android.ddmlib.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This will install the jar on to any connected devices selected
 * by the parameter or through user input. It requires having
 * ADB installs and will create a new ADB server instance to
 * install the jar.
 */
@Mojo(name = "compile-install", defaultPhase = LifecyclePhase.INSTALL)
public class AdbInstallMojo extends DalvikCompileMojo {

	/**
	 * This is the location of the adb executable. This is
	 * required to install a jar onto the android device.
	 * It cannot be the platform-tools directory but the
	 * actual file. If it is invalid or can not be found
	 * an exception will be thrown.
	 */
	@Parameter(required = true)
	private File adbLocation;

	/**
	 * This is the list of preselected devices to install
	 * the dalvik jar to. This is ideal for not having to
	 * continually select a device.
	 */
	@Parameter(required = false)
	private String[] deviceIds;

	@Component
	private Prompter prompter;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Compiling...");
		super.execute();
		getLog().info("");

		getLog().info("Installing...");
		if (adbLocation.isDirectory()) {
			throw new MojoFailureException("ADB location cannot be a folder!");
		}
		if (!adbLocation.canExecute()) {
			throw new MojoFailureException("ADB location is not executable!");
		}

		AndroidDebugBridge.initIfNeeded(false);
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge(adbLocation.getAbsolutePath(), true);

		try {
			int trials = 10;
			while (trials > 0) {
				Thread.sleep(50);
				if (adb.isConnected()) {
					break;
				}
				trials--;
			}

			if (!adb.isConnected()) {
				throw new MojoFailureException("Couldn't connect to ADB server");
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
				throw new MojoFailureException("Couldn't list connected devices");
			}

			Set<IDevice> selectedDevices = new HashSet<IDevice>();
			IDevice[] devices = adb.getDevices();
			if (devices.length <= 0) {
				getLog().warn("Skipping because there are no devices connected!");
				return;
			}
			if (deviceIds == null || deviceIds.length <= 0) {
				getLog().info("Please choose a device number the list: ");
				for (int i = 0; i < devices.length; i++) {
					IDevice device = devices[i];
					getLog().info(String.format("\t[%d] %s - %s", i, device.getName(), device.getSerialNumber()));

					String prompt = prompter.prompt("ID [X]").trim();
					if (!prompt.matches("[0-9]+")) {
						throw new MojoFailureException("Not a valid id!");
					}

					int selectedDevice = Integer.parseInt(prompt);
					if (selectedDevice >= devices.length || selectedDevice < 0) {
						throw new MojoExecutionException("ID not in list!");
					}

					selectedDevices.add(devices[selectedDevice]);
				}
			} else {
				for (String deviceId : deviceIds) {
					IDevice device = null;
					for (IDevice testDevice : devices) {
						if (!testDevice.getSerialNumber().equalsIgnoreCase(deviceId)) {
							continue;
						}
						device = testDevice;
					}
					if (device == null) {
						getLog().warn("Failed to find device: " + deviceId);
						continue;
					}
					selectedDevices.add(device);
				}
			}

			getLog().info("Installed Jar");
			for (IDevice device : selectedDevices) {
				device.pushFile(getBuildFile().toString(), getExportPath());
			}
		} catch (InterruptedException e) {
			throw new MojoExecutionException("Failed to use thread stop", e);
		} catch (PrompterException e) {
			throw new MojoExecutionException("Failed to read user input!", e);
		} catch (TimeoutException e) {
			throw new MojoExecutionException("Device is unresponsive!", e);
		} catch (AdbCommandRejectedException e) {
			throw new MojoExecutionException("Error within command!", e);
		} catch (SyncException e) {
			throw new MojoExecutionException("Failed to copy file", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to communicate with device!", e);
		} finally {
			AndroidDebugBridge.disconnectBridge();
		}

		AndroidDebugBridge.terminate();
	}

	protected void setAdbLocation(File adbLocation) {
		this.adbLocation = adbLocation;
	}

	protected void setDeviceIds(String[] deviceIds) {
		this.deviceIds = deviceIds;
	}
}
