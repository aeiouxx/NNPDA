
import protectedAxios from '../client/AxiosToken';
import { UserDto } from '../client/Types';


export const fetchUsers = async () : Promise<UserDto[]> => {
    const response = await protectedAxios.get('/user/all');
    return response.data;
}