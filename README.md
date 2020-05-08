<h1 align="center">Welcome to android-sqlite-toolbox 👋</h1>
<p>
  <a href="https://jitpack.io/#hbollon/android-sqlite-toolbox" target="_blank">
    <img alt="Version" src="https://jitpack.io/v/hbollon/android-sqlite-toolbox.svg" />
  </a>
  <a href="https://github.com/hbollon/android-sqlite-toolbox/blob/master/LICENSE.md" target="_blank">
    <img alt="License: GNU General Public License v3.0" src="https://img.shields.io/badge/License-GNU General Public License v3.0-yellow.svg" />
  </a>
</p>

> Android java package designed to manage a sqlite database. Includes creation of the DB and interactions with it, import and export in several formats (easily adaptable to any format) and synchronization in http (sending and receiving).

### 🏠 [Homepage](https://github.com/hbollon/android-sqlite-toolbox)

<p align="center"><strong> In early development, use it in production at your own risk </strong></p>

## Status

Done :
 - DBHandler -> Add crud support for db (easy and modulable insert, delete, update, read data in db)
 - Sqlite database export tools (Csv and Json export implemented, you can easily add other format with DBExporter abstract class). ExportConfig class is used for export configuration.
 - Http sync with remote db, sending only for the moment (local -> remote, we send db in json file). The request structure can be easily edited with FileUploadService interface. 
 - Table and Column data classes :  used for represent db table and column
 - Db creation and tables addition in DBHandler
 - Random tools in FileUtils
 
In progress :
  - Simple android exemple activity for main functions
  
Planned :
  - Db import from Json / Csv (like DBExporter structure so easily adaptable to others formats)
  - Sync db, remote -> local

## Install

This library is available int jitpack.io repository !

- Step 1 : Add this snippet to your project root build.gradle :

```sh
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- Step 2 : Add the dependency to your module build.gradle (Replace "Tag" with last release version) :

```sh
dependencies {
    // Needed libs
    implementation 'com.squareup.retrofit2:retrofit:2.8.1'
    implementation 'com.squareup.retrofit2:converter-gson:2.8.1'

    implementation 'com.github.hbollon:android-sqlite-toolbox:Tag'
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

Copyright © 2020 [Hugo Bollon](https://github.com/hbollon).<br />
This project is [GNU General Public License v3.0](https://github.com/hbollon/android-sqlite-toolbox/blob/master/LICENSE.md) licensed.
