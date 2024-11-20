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
  FormHelperText,
} from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { DeviceDto, UserDto } from '../../../client/Types';
import { fetchUsers } from '../../../service/UserService';
import { fetchDevices } from '../../../service/DeviceService';
import { assignDevice, unassignDevice } from '../../../service/UserDeviceService';

export interface AssignDeviceParameters {
  username: string;
  serialNumber: string;
}


const assignDeviceSchema = z.object({
  username: z.string().min(1, "User is required"),
  serialNumber: z.string().min(1, "Device is required"),
});

const AssignDeviceForm: React.FC = () => {
  const [users, setUsers] = useState<UserDto[]>([]);
  const [devices, setDevices] = useState<DeviceDto[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const {
    control,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<AssignDeviceParameters>({
    defaultValues: {
      username: '',
      serialNumber: '',
    },
    resolver: zodResolver(assignDeviceSchema), // Use Zod resolver for validation
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
      alert('Device assigned successfully!');
    } catch (error) {
      console.error('Error assigning device', error);
      alert('Failed to assign device.');
    } finally {
      reset();
      setIsLoading(false);
    }
  };

  const onUnassign = async (data: AssignDeviceParameters) => {
    setIsLoading(true);
    try {
      await unassignDevice(data); 
      alert('Device unassigned successfully!');
    } catch (error) {
      console.error('Error unassigning device', error);
      alert('Failed to unassign device.');
    } finally {
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
            <FormControl fullWidth error={!!errors.username}>
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
              {errors.username && (
                <FormHelperText>{errors.username.message}</FormHelperText>
              )}
            </FormControl>
          </Grid>

          {/* Device Dropdown */}
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth error={!!errors.serialNumber}>
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
                        {device.modelName} - {device.serialNumber}
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
              {errors.serialNumber && (
                <FormHelperText>{errors.serialNumber.message}</FormHelperText>
              )}
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
