# Dynapp Maven Plugin

The plugin handles installing dalvik compiled jars to the android devices. The system converts java specific code to android specific code and moves it onto the selected ADB devices. This requires the device is in developer mode, the device has USB debugging enabled, and the computer has ADB installed. The module was built on maven 2.0. There are many configuration options based off personal preference. 

##Setup
### How to enable developer mode?
  1. Find the Android Build number in Settings
![The screens of where the build number is generally found](http://images.techhive.com/images/article/2014/07/buildnumber-100361660-large.idge.png)
  2. How to reach the build number on popular devices
    - **Stock Android**: Settings > About phone > Build number
    - **LG**: Settings > About phone > Software information > Build number 
    - **HTC One (M8)**: Settings > About > Software information > More > Build number
  3. Once you’ve found the Build number section of the settings, tap on the section 7 times. After two taps, a small pop up notification should appear saying "you are now X steps away from being a developer" with a number that counts down with every additional tap.

### How to enable ADB debugging?
 1. Go into developer options *(Covered in the previous question on how enable developer mode)*
 2. Check USB Debugging
![USB Debugging Menu example and where to click](http://www.companionlink.com/support/wiki/images/thumb/0/09/USBdebugSetttingAnd4.jpg/700px-USBdebugSetttingAnd4.jpg)
 3. Exit the settings app
 4. Connect to the Computer through a micro usb.
 
### How to install ADB on my computer?
*This will not cover the logistics of installing [Android Studio](http://developer.android.com/sdk/index.html) however you can go to the link attached to install it on your operating system. *
![Location of each item to click in android studio](http://i.prntscr.com/d13efab7dca54cb9a1a7948653e39f68.png) 
 1. Click the android guy download icon near the top left.
 2. Click on the SDK Tools tab.
 3. Check the SDK Platform Tools
 4. Click "Apply" and ensure the status says installed afterwards.
 5. The path to your adb should be should be **%Android SDK Location%/platform-tools/adb** replace the **%Android SDK Location%** with the path highlighted in the photo *(Relative to your operating system)*

## Maven Options
### Maven Repository
Added the following repository to your maven list. This hosts the robot core library and the plugin. It is Dynapp's central repository and acts as a medium for jar hosting. 
tng. 
```xml
<repository>
    <id>net.dynapp</id>
    <name>Dynapp</name>
    <url>https://raw.githubusercontent.com/dynapp/maven-repository/master/repo/</url>
</repository>
```
### Required Options
*These are all the required parameters to run the most basic version of dynapp maven plugin. There are more options for advanced users*
```xml
	<build>
		<plugins>
			<plugin>
				<groupId>net.dynapp.installer</groupId>
				<artifactId>dynapp-maven-plugin</artifactId>
				<version>1.3.0</version>
				<configuration>
					<jarLocation></jarLocation>
					<adbLocation></adbLocation>
				</configuration>
			</plugin>
		</plugins>
	</build>
```
 - **jarLocation**: This is the location of the input jar. It is important for the jar to be generated before this task is ran. After the jar is generated it will be recompiled by the android dalvik compiler and exported. It then can be used on any android device as a library or expansion pack.
 - **adbLocation**: This is the location of the adb executable. This is required to install a jar onto the android device. It cannot be the platform-tools directory but the actual file. If it is invalid or can not be found an exception will be thrown. The path to your OS and adb install *%Android SDK Location%/platform-tools/adb*
 
### Additional Options
 *Here are options for the more advanced user or someone looking to customize the way ADB works. It is recommended that you do not use all of these because they may affect and or break if improperly setup*
 ```xml
	<build>
		<plugins>
			<plugin>
				<groupId>net.dynapp.installer</groupId>
				<artifactId>dynapp-maven-plugin</artifactId>
				<version>1.3.0</version>
				<configuration>
					<jarLocation></jarLocation>
					<adbLocation></adbLocation>
					<buildDir></buildDir>
					<buildName></buildName>
					<devices>
						<device></device>
					</devices>
				</configuration>
			</plugin>
		</plugins>
	</build>
```
 - **jarLocation**: This is the location of the input jar. It is important for the jar to be generated before this task is ran. After the jar is generated it will be recompiled by the android dalvik compiler and exported. It then can be used on any android device as a library or expansion pack.
 - **adbLocation**: This is the location of the adb executable. This is required to install a jar onto the android device. It cannot be the platform-tools directory but the actual file. If it is invalid or can not be found an exception will be thrown. The path to your OS and adb install *%Android SDK Location%/platform-tools/adb*
 - **buildDir**: This is the location where the dalvik compiled Jar will go. This must be a directory and it does not have to exist. If you do not have a custom location to place to jar then there is no reason to change this value.
 - **buildName**: This is the name of the .jar file. The .jar suffix is automatically appended on during file generation so do not put the file type in for the value. Also certain characters are removed from the file to prevent OS errors. It is recommended not to modify this name.
 - **devices**: This is the list of preselected devices to install the dalvik jar to. This is ideal for not having to continually select a device. Add a list of device ID's to automatically select a device to install to.
 
## Latest Version

1.5.0

## Licensee

MIT Licensee