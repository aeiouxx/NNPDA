import protectedAxios from '../client/AxiosToken';

export interface AssignDeviceParameters {
    username: string;
    serialNumber: string;
}

export const assignDevice = async (parameters : AssignDeviceParameters) : Promise<void> => {
    const { username, serialNumber} = parameters;
    await protectedAxios.post(`/assign/${username}/devices/${serialNumber}`)
}

export const unassignDevice = async (parameters : AssignDeviceParameters) : Promise<void> => {
    const { username, serialNumber} = parameters;
    await protectedAxios.delete(`/assign/${username}/devices/${serialNumber}`)
}