import protectedAxios from '../client/AxiosToken';
import { CreateDeviceDto, DeviceDto } from '../client/Types';


export const fetchDevices = async () : Promise<DeviceDto[]> => {
    const response = await protectedAxios.get('/devices');
    console.log("Fetched: ", JSON.stringify(response.data));
    
    return response.data;
}

export const createDevice = async (device : Partial<CreateDeviceDto>) : Promise<void> => {
    console.log(JSON.stringify(device));
    await protectedAxios.post('/devices', device);
}

export const deleteDevice = async (id : number | string) : Promise<void> => {
    var encoded = encodeURIComponent(id);
    await protectedAxios.delete(`/devices/${encoded}`);
}

export const editDevice = async (id : number | string, device: Partial<CreateDeviceDto>) : Promise<void> => {
    var encoded = encodeURIComponent(id);
    await protectedAxios.put(`/devices/${encoded}`, device);
}