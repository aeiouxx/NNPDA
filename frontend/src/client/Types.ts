export interface BaseEntity {
  id: number | string;
}


export interface DeviceDto extends BaseEntity {
    serialNumber: string;
    modelName: string;
}


export interface CreateDeviceDto {
    serialNumber: string;
    apiKeyHash: string;
    modelName: string;
}

export interface CreateSensorDto {
    serialNumber: string;
    name: string;
    deviceSerial: string;
}

export interface SensorWithDeviceDto extends BaseEntity {
    serialNumber : string,
    name : string,
    deviceSerial : string,
    deviceName: string
}