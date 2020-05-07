<h1 align="center">Welcome to android-sqlite-toolbox 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-v0.1.0-blue.svg?cacheSeconds=2592000" />
  <a href="https://github.com/hbollon/android-sqlite-toolbox/blob/master/LICENSE.md" target="_blank">
    <img alt="License: GNU General Public License v3.0" src="https://img.shields.io/badge/License-GNU General Public License v3.0-yellow.svg" />
  </a>
</p>

> Android java package designed to manage a sqlite database. Includes creation of the DB and interactions with it, import and export in several formats (easily adaptable to any format) and synchronization in http (sending and receiving).

### 🏠 [Homepage](https://github.com/hbollon/android-sqlite-toolbox)

<p align="center"><strong> In early development, not safe for production use ! </strong></p>

## Status

Done :
 - Sqlite database export tools (Csv and Json export implemented, you can easily add other format with DBExporter abstract class). ExportConfig class is used for export configuration.
 - Http sync with remote db, sending only for the moment (local -> remote, we send db in json file). The request structure can be easily edited with FileUploadService interface. 
 - Table and Column data classes :  used for represent db table and column
 - Db creation and tables addition in DBHandler
 - Random tools in FileUtils
 
In progress :
  - DBHandler -> Add crud support for db (easy and modulable insert, delete, update, read data in db)
  - Simple android exemple activity for main functions
  
Planned :
  - Db import from Json / Csv (like DBExporter structure so easily adaptable to others formats)
  - Sync db, remote -> local

## Install

For the moment, you can download and test it by download the repo, copying module folder into your android project and implement it into your gradle config file with : 

```sh
implementation project(path: ':android-sqlite-toolbox')
```

I will add it to gradle sources later in the development.

## How to use it

Coming soon ;)

## Author

👤 **Hugo Bollon**

* Github: [@hbollon](https://github.com/hbollon)
* LinkedIn: [@Hugo Bollon](https:/www.linkedin.com/in/hugo-bollon-68a2381a4/)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/hbollon/android-sqlite-toolbox/issues). 

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2020 [Hugo Bollon](https://github.com/hbollon).<br />
This project is [GNU General Public License v3.0](https://github.com/hbollon/android-sqlite-toolbox/blob/master/LICENSE.md) licensed.
