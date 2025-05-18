# Lost & Found - University Campus App

![Lost & Found App](https://via.placeholder.com/800x400?text=Lost+and+Found+App)

## Overview

Lost & Found is an Android application designed to help university students and staff report, track, and claim lost or found items on campus. The app provides a centralized platform for the campus community to connect lost items with their owners.

## Features

### Core Functionality
- **Report Lost Items**: Submit details about items you've lost on campus
- **Report Found Items**: Submit details about items you've found on campus
- **Item Tracking**: Monitor the status of reported items (lost, found, claimed)
- **Item Search**: Find items by category, location, or description
- **Claim Process**: Simple process for owners to claim their lost items

### Campus-Specific Features
- **Building & Room Tracking**: Specify exact campus locations
- **Campus Area Selection**: Filter items by specific campus zones
- **University Contact Integration**: Connect with campus security or lost & found offices

### User Experience
- **Image Support**: Add photos of lost or found items
- **Comment System**: Communicate with others about specific items
- **Contact Options**: Email, call, or SMS item reporters directly
- **Dark Mode Support**: Comfortable viewing in any lighting condition
- **Responsive Design**: Works on phones and tablets of all sizes

## Screenshots

<table>
  <tr>
    <td><img src="https://via.placeholder.com/250x500?text=Home+Screen" alt="Home Screen" width="250"/></td>
    <td><img src="https://via.placeholder.com/250x500?text=Item+Details" alt="Item Details" width="250"/></td>
    <td><img src="https://via.placeholder.com/250x500?text=Report+Item" alt="Report Item" width="250"/></td>
  </tr>
  <tr>
    <td><img src="https://via.placeholder.com/250x500?text=Comments" alt="Comments" width="250"/></td>
    <td><img src="https://via.placeholder.com/250x500?text=Dark+Mode" alt="Dark Mode" width="250"/></td>
    <td><img src="https://via.placeholder.com/250x500?text=Settings" alt="Settings" width="250"/></td>
  </tr>
</table>

## Technical Details

### Requirements
- Android 7.0 (API 24) or higher
- Camera permission for item photos
- Storage permission for saving images

### Architecture
- **Pattern**: Model-View-Controller (MVC)
- **Data Storage**: SharedPreferences with Gson serialization
- **UI Components**: AndroidX and Material Design
- **Image Handling**: Glide library for efficient loading

### Dependencies
```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'com.google.android.material:material:1.12.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.2.1'
    implementation 'com.github.bumptech.glide:glide:4.15.1'
    implementation 'com.google.code.gson:gson:2.10.1'
}
```

## Installation

### From Source
1. Clone the repository:
   ```
   git clone https://github.com/yourusername/lost-found-app.git
   ```
2. Open the project in Android Studio
3. Build and run on your device or emulator

### From Release
1. Download the latest APK from the [Releases](https://github.com/aman7082/lost-found-app/releases) page
2. Enable installation from unknown sources in your device settings
3. Install the APK

## Usage

### Reporting a Lost Item
1. Open the app and tap the "+" button
2. Select "Lost Item"
3. Fill in the details (title, description, location, etc.)
4. Add a photo if available
5. Submit the report

### Finding Items
1. Use the search bar to find specific items
2. Filter by category, date, or location
3. Browse the list of recently reported items
4. Contact the reporter through the app if you find a match

### Claiming an Item
1. Navigate to the item details page
2. Tap "Claim This Item"
3. Provide verification details
4. Arrange pickup with the finder

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- University of Example for supporting the initial concept
- All contributors who have helped improve the app
- The Android development community for valuable resources and libraries

## Contact

Project Link: [https://github.com/aman7082e/lost-found-app](https://github.com/aman7082/lost-found-app)

---

Made with ❤️ for university communities
