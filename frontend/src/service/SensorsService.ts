import protectedAxios from '../client/AxiosToken';
import { CreateSensorDto, SensorWithDeviceDto } from '../client/Types';


export const fetchSensors = async () : Promise<SensorWithDeviceDto[]> => {
    const response = await protectedAxios.get('/sensors');
    console.log("Fetched: ", JSON.stringify(response.data));
    return response.data;
}

export const createSensor = async (device : Partial<CreateSensorDto>) : Promise<void> => {
    await protectedAxios.post('/sensors', device);
}

export const deleteSensor = async (id : number | string) : Promise<void> => {
    var encoded = encodeURIComponent(id);
    await protectedAxios.delete(`/sensors/${encoded}`);
}

export const editSensor = async (id : number | string, sensor: Partial<SensorWithDeviceDto>) : Promise<void> => {
    var encoded = encodeURIComponent(id);
    await protectedAxios.put(`/sensors/${encoded}`, 
        {
            name: sensor.name!,
            serialNumber: sensor.serialNumber!,
            deviceSerialNumber: sensor.deviceSerial!
        }
    );
}