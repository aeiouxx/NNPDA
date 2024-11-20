import React, { useEffect, useState } from 'react';
import {
  Button,
  CircularProgress,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  Typography,
} from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import axios from 'axios';
import { DeviceDto, UserDto } from '../../../client/Types';
import { fetchDevices } from '../../../service/DeviceService';
import { fetchUsers } from '../../../service/UserService';
import { assignDevice, AssignDeviceParameters, unassignDevice } from '../../../service/UserDeviceService';
import { formatDeviceLabel } from '../Sensors/SensorsForm';

const AssignDeviceForm: React.FC = () => {
  const [users, setUsers] = useState<UserDto[]>([]);
  const [devices, setDevices] = useState<DeviceDto[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const { control, handleSubmit, formState: { isSubmitting }, reset } = useForm<AssignDeviceParameters>({
    defaultValues: {
      username: '',
      serialNumber: '',
    },
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [usersResponse, devicesResponse] = await Promise.all([
          fetchUsers(),
          fetchDevices(),
        ]);
        setUsers(usersResponse);
        setDevices(devicesResponse);
      } catch (error) {
        console.error('Error fetching users or devices', error);
      }
    };

    fetchData();
  }, []);

  const onAssign = async (data: AssignDeviceParameters) => {
    setIsLoading(true);
    try {
        await assignDevice(data);
    } catch (error) {
    } 
    finally {
        reset();
        setIsLoading(false);
    }
  };

  const onUnassign = async (data: AssignDeviceParameters) => {
    setIsLoading(true);
    try {
        await unassignDevice(data);
    } catch (error) {
    } 
    finally {
        reset();
        setIsLoading(false);
    }
  };

  return (
    <div>
      <Typography variant="h5" gutterBottom>
        Assign or Unassign Devices
      </Typography>
      <form onSubmit={handleSubmit(onAssign)}>
        <Grid container spacing={2}>
          {/* User Dropdown */}
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth>
              <InputLabel id="user-label">User</InputLabel>
              <Controller
                name="username"
                control={control}
                render={({ field }) => (
                  <Select
                    labelId="user-label"
                    {...field}
                    disabled={isSubmitting || isLoading}
                  >
                    {users.map((user) => (
                      <MenuItem key={user.username} value={user.username}>
                        {user.username} ({user.email})
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
            </FormControl>
          </Grid>

          {/* Device Dropdown */}
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth>
              <InputLabel id="device-label">Device</InputLabel>
              <Controller
                name="serialNumber"
                control={control}
                render={({ field }) => (
                  <Select
                    labelId="device-label"
                    {...field}
                    disabled={isSubmitting || isLoading}
                  >
                    {devices.map((device) => (
                      <MenuItem key={device.serialNumber} value={device.serialNumber}>
                        {formatDeviceLabel(device)}
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
            </FormControl>
          </Grid>

          {/* Buttons */}
          <Grid item xs={12}>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={isSubmitting || isLoading}
              style={{ marginRight: '8px' }}
            >
              {isLoading ? <CircularProgress size={24} /> : 'Assign Device'}
            </Button>
            <Button
              variant="outlined"
              color="secondary"
              disabled={isSubmitting || isLoading}
              onClick={handleSubmit(onUnassign)}
            >
              {isLoading ? <CircularProgress size={24} /> : 'Unassign Device'}
            </Button>
          </Grid>
        </Grid>
      </form>
    </div>
  );
};

export default AssignDeviceForm;
