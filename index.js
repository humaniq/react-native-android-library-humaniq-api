'use strict'

import { NativeModules } from 'react-native'
// name as defined via ReactContextBaseJavaModule's getName

const HumaniqToastApiLib = NativeModules.HumaniqToastApiLib
const HumaniqProfileApiLib = NativeModules.HumaniqProfileApiLib
const HumaniqDownloadFileLib = NativeModules.HumaniqDownloadFileLib
const HumaniqContactsApiLib = NativeModules.HumaniqContactsApiLib
const HumaniqBlockchainApiLib = NativeModules.HumaniqBlockchainApiLib
const HumaniqTokenApiLib = NativeModules.HumaniqTokenApiLib

export {
HumaniqToastApiLib,
HumaniqProfileApiLib,
HumaniqDownloadFileLib,
HumaniqContactsApiLib, 
HumaniqBlockchainApiLib,
HumaniqTokenApiLib
}
