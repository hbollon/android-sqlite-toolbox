<h1 align="center">Welcome to android-sqlite-toolbox 👋</h1>
<p align="center">
  <a href="https://jitpack.io/#hbollon/android-sqlite-toolbox" target="_blank">
    <img alt="Version" src="https://jitpack.io/v/hbollon/android-sqlite-toolbox.svg" />
  </a>
  <a href="https://github.com/hbollon/android-sqlite-toolbox/blob/master/LICENSE" target="_blank">
    <img alt="License: MIT" src="https://img.shields.io/badge/License-MIT-yellow.svg" />
  </a>
</p>

> Android java package designed to easily manage a sqlite database. Include creation of the DB and interactions with it, import and export in several formats (easily adaptable to any other format) and synchronization through http.

### 🏠 [Homepage](https://github.com/hbollon/android-sqlite-toolbox)

<p align="center"><strong> In development, use it in production at your own risk </strong></p>

## Features

- Easy creation of databases and tables addition
- CRUD support for db, easy and modulable: insert, delete, update and read
- Object representation for all DB elements (Table, Column and Data) for easy manipluation!
- Sqlite database export tools (Csv and Json export implemented, you can easily add other format with DBExporter abstract class).
- Db importer which handle natively import from Json (like DBExporter structure so easily adaptable to others formats)
- Http sync with remote db, sending only for the moment (local -> remote, it sends db in json file). The request structure can be easily edited with FileUploadService interface and requestBuilder method. 

### Planned features
- Simple android example activity for main functions
- Add csv native support for import.
- Sync db, remote -> local

## Install

This library is available in jitpack.io repository !

- Step 1 : Add this repo source to your project root build.gradle :

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- Step 2 : Add the dependency to your module build.gradle (Replace "Tag" with last release version) :

```gradle
dependencies {
    implementation 'com.github.hbollon:android-sqlite-toolbox:Tag'

    // Needed libs if you override requestBuilder() or any http related function
    implementation 'com.squareup.retrofit2:retrofit:2.8.1'
    implementation 'com.squareup.retrofit2:converter-gson:2.8.1'
}
```

That's it ! You can now import all library classes into your android project !

## How to use it

Coming soon ;)

## Author

👤 **Hugo Bollon**

* Github: [@hbollon](https://github.com/hbollon)
* LinkedIn: [@Hugo Bollon](https://www.linkedin.com/in/hugo-bollon-68a2381a4/)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/hbollon/android-sqlite-toolbox/issues). 

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

This library use [Retrofit2](https://github.com/square/retrofit) lib for http request under [Apache License 2.0](https://github.com/square/retrofit/blob/master/LICENSE.txt)<br />

Copyright © 2020 [Hugo Bollon](https://github.com/hbollon).<br />
This project is [MIT License](https://github.com/hbollon/android-sqlite-toolbox/blob/master/LICENSE) licensed.
