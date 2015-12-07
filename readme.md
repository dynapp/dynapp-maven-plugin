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

> The overriding design goal for Markdown's
> formatting syntax is to make it as readable
> as possible. The idea is that a
> Markdown-formatted document should be
> publishable as-is, as plain text, without
> looking like it's been marked up with tags
> or formatting instructions.

##Maven Options
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
 - **jarLocation**: Jar location is the relative path of your exported Jar relative to your project.
 - **adbLocation**: The path to your OS and install *%Android SDK Location%/platform-tools/adb*
 
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
				</configuration>
			</plugin>
		</plugins>
	</build>
```
 - **jarLocation**: Jar location is the relative path of your exported Jar relative to your project.
 - **adbLocation**: The path to your OS and install *%Android SDK Location%/platform-tools/adb*