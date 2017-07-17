'use strict'

import { NativeModules } from 'react-native'
// name as defined via ReactContextBaseJavaModule's getName

const HumaniqApiLib = NativeModules.HumaniqApiLib
const HumaniqProfileApiLib = NativeModules.HumaniqProfileApiLib
const HumaniqDownloadFileLib = NativeModules.HumaniqDownloadFileLib
const HumaniqContactsApiLib = NativeModules.HumaniqContactsApiLib

export {HumaniqApiLib, HumaniqProfileApiLib, HumaniqDownloadFileLib, HumaniqContactsApiLib}
