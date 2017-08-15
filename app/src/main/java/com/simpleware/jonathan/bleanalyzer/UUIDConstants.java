package com.simpleware.jonathan.bleanalyzer;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by jdavis on 8/15/2017.
 */

public class UUIDConstants {

    final static HashMap<UUID, String> UUID_NAME_MAP = new HashMap<>();

    static  {
        // Load the services
        UUID_NAME_MAP.put(Services.V1C_SERVICE, "V1connection LE Service");
        UUID_NAME_MAP.put(Services.GENERIC_ACCESS_PROFILE_SERVICE, "Generic Access Profile (GAP)");
        UUID_NAME_MAP.put(Services.GENERIC_ATTRIBUTE_PROFILE_SERVICE, "Generic Attribute Profile");
        UUID_NAME_MAP.put(Services.DEVICE_INFORMATION_SERVICE, "Device Information");
        // Load the characteristic
        UUID_NAME_MAP.put(Characteristics.V1_OUT_SHORT_CHARAC, "V1 out Client In Short");
        UUID_NAME_MAP.put(Characteristics.V1_OUT_LONG_CHARAC, "V1 out Client In Long");
        UUID_NAME_MAP.put(Characteristics.CLIENT_OUT_OUT_SHORT_CHARAC, "Client Out V1 in Short");
        UUID_NAME_MAP.put(Characteristics.CLIENT_OUT_OUT_LONG_CHARAC, "Client Out V1 in Long");

        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_SYSTEM_ID_CHARAC, "System ID");
        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_MODEL_NUMBER_CHARAC, "Model Number String");
        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_SERIAL_NUMBER_CHARAC, "Serial Number String");
        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_FIRMWARE_REV_CHARAC, "Firmware Revision String");
        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_HARDWARE_REV_CHARAC, "Hardware Revision String");
        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_SOFTWARE_REV_CHARAC, "Software Revision String");
        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_MANUFAC_NAME_CHARAC, "Manufacturer Name String");
        UUID_NAME_MAP.put(Characteristics.DEVICEINFO_PNP_ID_CHARAC, "PnP ID");

        UUID_NAME_MAP.put(Characteristics.GAP_DEVICE_NAME_CHARAC, "Device Name");
        UUID_NAME_MAP.put(Characteristics.GAP_APPEARANCE_CHARAC, "Appearance");
        UUID_NAME_MAP.put(Characteristics.GAP_PERIPHERIAL_PRIV_CHARAC, "Peripheral Privacy Flag");
        UUID_NAME_MAP.put(Characteristics.GAP_RECONN_ADDR_CHARAC, "Reconnection Address");
        UUID_NAME_MAP.put(Characteristics.GAP_PREFERRED_CONN_PARAM_CHARAC, "Peripheral Preferred Connection Parameters");

        UUID_NAME_MAP.put(Characteristics.SERVICE_CHANGED_CHARAC, "Service Changed");
    }

    public static class Services {
        public final static UUID V1C_SERVICE =                            UUID.fromString("92a0aff4-9e05-11e2-aa59-f23c91aec05e");
        public static final UUID GENERIC_ACCESS_PROFILE_SERVICE =         UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
        public static final UUID GENERIC_ATTRIBUTE_PROFILE_SERVICE =      UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
        public static final UUID DEVICE_INFORMATION_SERVICE =             UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    }

    public static class Characteristics {
        //V1C_SERVICE CHARACTERISTICS//
        public final static UUID V1_OUT_SHORT_CHARAC =                    UUID.fromString("92a0b2ce-9e05-11e2-aa59-f23c91aec05e");
        public final static UUID V1_OUT_LONG_CHARAC =                     UUID.fromString("92a0b4e0-9e05-11e2-aa59-f23c91aec05e");
        public final static UUID CLIENT_OUT_OUT_SHORT_CHARAC =            UUID.fromString("92a0b6d4-9e05-11e2-aa59-f23c91aec05e");
        public final static UUID CLIENT_OUT_OUT_LONG_CHARAC =             UUID.fromString("92a0b8d2-9e05-11e2-aa59-f23c91aec05e");
        //DEVICE_INFORMATION_SERVICE CHARACTERISTICS//
        public final static UUID DEVICEINFO_SYSTEM_ID_CHARAC =            UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");
        public final static UUID DEVICEINFO_MODEL_NUMBER_CHARAC =         UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
        public final static UUID DEVICEINFO_SERIAL_NUMBER_CHARAC =        UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
        public final static UUID DEVICEINFO_FIRMWARE_REV_CHARAC =         UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
        public final static UUID DEVICEINFO_HARDWARE_REV_CHARAC =         UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
        public final static UUID DEVICEINFO_SOFTWARE_REV_CHARAC =         UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
        public final static UUID DEVICEINFO_MANUFAC_NAME_CHARAC =         UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
        public final static UUID DEVICEINFO_PNP_ID_CHARAC =               UUID.fromString("00002a50-0000-1000-8000-00805f9b34fb");
        //GENERIC_ATTRIBUTE_PROFILE_SERVICE CHARACTERISTICS//
        public final static UUID GAP_DEVICE_NAME_CHARAC =                 UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
        public final static UUID GAP_APPEARANCE_CHARAC =                  UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
        public final static UUID GAP_PERIPHERIAL_PRIV_CHARAC =            UUID.fromString("00002a02-0000-1000-8000-00805f9b34fb");
        public final static UUID GAP_RECONN_ADDR_CHARAC =                 UUID.fromString("00002a03-0000-1000-8000-00805f9b34fb");
        public final static UUID GAP_PREFERRED_CONN_PARAM_CHARAC =        UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");
        //SERVICE_CHANGED_CHARAC CHARACTERISTICS//
        public final static UUID SERVICE_CHANGED_CHARAC =                 UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb");
    }

    public static String getNameForUUID(UUID uuid) {
        String name = UUID_NAME_MAP.get(uuid);
        return name == null ? "Unknown" : name;
    }

    public static String getNameForServiceUUID(UUID uuid) {
        String name = UUID_NAME_MAP.get(uuid);
        return name == null ? "Unknown Service" : name;
    }

    public static String getNameForCharacteristicUUID(UUID uuid) {
        String name = UUID_NAME_MAP.get(uuid);
        return name == null ? "Unknown Characteristic" : name;
    }
}
