Mood Tracker App

<n></n>   <img width="360" alt="Screenshot 2024-09-17 at 11 28 43" src="https://github.com/user-attachments/assets/3b24703c-2cb8-4eac-9c31-36b53e25fd8f">

Overview

Mood Tracker is an app designed to help users monitor their emotional wellbeing by tracking their moods daily. It offers insights into mood patterns, helping users identify triggers and improve their mental health.

Features

Daily Mood Logging: Record your mood multiple times a day with just a few taps.
Mood Insights: View graphs and trends based on your mood history.
Community: Share comments under post, discuss with other users

System Requirements
The Application only designed to run on Android. In can be run in Android Studio. The project uses Java 1.8 compatibility settings.

Kotlin JVM target 1.8
Minimum SDK Version 27
Target SDK Version 34

Running the Program

1. Setup Android Studio:
   - Open Android Studio and configure it to use JDK 1.8, which is essential for compatibility with the project settings.

2. Import the Project:
   - Start Android Studio and select "Open an existing Android Studio project" from the options on the welcome screen or from the "File" menu.
   - Navigate to the directory where you have the project files and select the project folder.

3. Configure the Gradle Sync:
   - Android Studio will automatically attempt to synchronize the project with the necessary Gradle files. Allow it to complete this process. If there are any issues, such as missing SDK platforms or tools, Android Studio will prompt you to download them.

4. Set Up the Android Emulator or Connect a Device:
   - Emulator: Setup an Android emulator by accessing the AVD Manager (Android Virtual Device) in Android Studio under the "Tools > AVD Manager" menu. Create a new virtual device that meets the minimum SDK requirement (Android 8.1, API level 27) or higher.
   - Physical Device: Connect your Android device via USB and enable USB debugging in the developer options. Ensure the device’s Android version is at least Android 8.1 (API level 27).

5. Check Project Configuration:
   - Verify the project’s “build.gradle” settings to ensure they match the system requirements mentioned:
     - minSdkVersion 27
     - targetSdkVersion 34
     - kotlinOptions { jvmTarget = "1.8" }
   - Make sure any necessary API keys or configuration variables defined in the “buildConfigField” are correctly set in your local “gradle.properties” file or provided during the build process.

6. Run the Application:
   - In Android Studio, click on the "Run" icon in the toolbar, or press `Shift + F10` to start the application.
   - Choose the device or emulator you configured earlier. The application will compile, and Android Studio will install it on the selected device or emulator.



Using the Application:

1. Create a new user account / Login to an existing account
   - Enter an email and a password to login
   - Select 'Sign up' to create a new user
  <img width="360" alt="Screenshot 2024-09-17 at 11 28 00" src="https://github.com/user-attachments/assets/147c1b32-7958-4009-b1fe-65503f200b73">

2. Main page
   - Select the day you want to log your mood for
   - Select the rating that represents your mood
   - Select the mood that represents your current state
   - hit save
   <img width="360" alt="Screenshot 2024-09-17 at 11 28 43" src="https://github.com/user-attachments/assets/3b24703c-2cb8-4eac-9c31-36b53e25fd8f">

3. Insights Page
   - See your average mood rating for past 7 days
   - Get personalized articles that can help you improve your mood
   <img width="356" alt="Screenshot 2024-09-17 at 11 28 54" src="https://github.com/user-attachments/assets/c10b50a8-d0ae-441f-8cb9-069489a67295">

4. Personalized Avatar
   - Create your personalized avatar to remain fully anonymous to the community
   <img width="361" alt="Screenshot 2024-09-17 at 11 29 19" src="https://github.com/user-attachments/assets/4e1017b9-fa33-486c-aeb6-c9af0d177c89">
  
