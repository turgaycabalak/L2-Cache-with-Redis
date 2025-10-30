package com.cache.supercache.enums;

public enum CustomerInitializerTypeEnum {
  BASIC, // sadece customer + address
  WITH_ADDRESS_TYPE, // address + addressType
  FULL // addressType + interest + email
}
