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
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Alert,
} from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { DeviceDto, SensorWithDeviceDto } from '../../client/Types';
import { useOutletContext } from 'react-router-dom';
import { ProtectedContext } from '../../components/ProtectedRoute';
import protectedAxios from '../../client/AxiosToken';
import config from '../../config';


interface UserDeviceSensorsFormValues {
  deviceSerialNumber: string;
}

const DeviceSchema = z.object({
  deviceSerialNumber: z.string().min(1, 'Please select a device.'),
});

const UserDeviceSensors: React.FC = () => {
  const { user } = useOutletContext<ProtectedContext>();
  const [devices, setDevices] = useState<DeviceDto[]>([]);
  const [sensors, setSensors] = useState<SensorWithDeviceDto[]>([]);
  const [isLoadingDevices, setIsLoadingDevices] = useState(false);
  const [isLoadingSensors, setIsLoadingSensors] = useState(false);
    const [fetchAttempted, setFetchAttempted] = useState(false); 
  const {
    control,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<UserDeviceSensorsFormValues>({
    resolver: zodResolver(DeviceSchema),
    defaultValues: {
      deviceSerialNumber: '',
    },
  });

  useEffect(() => {
    const fetchDevices = async () => {
      setIsLoadingDevices(true);
      try {
        const url = `${user!.username}/devices`;
        const response = await protectedAxios.get(url);
        console.log("Fetched: ", JSON.stringify(response));
        setDevices(response.data);
      } catch (error) {
        console.error('Failed to fetch devices:', error);
      } finally {
        setIsLoadingDevices(false);
      }
    };

    fetchDevices();
  }, []);

  const fetchSensors = async (data: UserDeviceSensorsFormValues) => {
    setIsLoadingSensors(true);
    try {
      const url = `${user!.username}/sensors/${data.deviceSerialNumber}`;
      const response = await protectedAxios.get(url);
      console.log("Fetched: ", JSON.stringify(response));
      setSensors(response.data);
    } catch (error) {
      console.error('Failed to fetch sensors:', error);
    } finally {
      setFetchAttempted(true);
      setIsLoadingSensors(false);
    }
  };


  const handleViewInKibana = (sensorSerialNumber: string) => {
    const kibanaUrl = `${config.kibanaBaseUrl}}/app/dashboards#/view/{dashboardId}?_a=(filters:!((query:(match:(sensor_serial_number:(query:'${sensorSerialNumber}'))))))`;
    window.open(kibanaUrl, "_blank");
  };

  return (
    <div>
      <Typography variant="h5" gutterBottom>
        Device Sensors
      </Typography>
      <form onSubmit={handleSubmit(fetchSensors)}>
        <Grid container spacing={2}>
          {/* Device Selector */}
          <Grid item xs={12}>
            <FormControl fullWidth>
              <InputLabel id="device-label">Select Device</InputLabel>
              <Controller
                name="deviceSerialNumber"
                control={control}
                render={({ field }) => (
                  <Select
                    labelId="device-label"
                    {...field}
                    disabled={isSubmitting || isLoadingDevices}
                  >
                    {devices.map((device) => (
                      <MenuItem key={device.serialNumber} value={device.serialNumber}>
                        {device.modelName} - {device.serialNumber}
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
              {errors.deviceSerialNumber && (
                <Typography color="error">{errors.deviceSerialNumber.message}</Typography>
              )}
            </FormControl>
          </Grid>

          {/* Search Button */}
          <Grid item xs={12}>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={isSubmitting || isLoadingDevices}
            >
              {isSubmitting || isLoadingSensors ? (
                <CircularProgress size={24} />
              ) : (
                'Search Sensors'
              )}
            </Button>
          </Grid>
        </Grid>
      </form>

      {/* Sensors Table */}
        <TableContainer component={Paper} style={{ marginTop: 20 }}>
        {isLoadingSensors ? (
          <CircularProgress size={24} />
        ) : sensors.length > 0 ? (
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Sensor Name</TableCell>
                <TableCell>Sensor Serial Number</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {sensors.map((sensor) => (
                <TableRow key={sensor.serialNumber}>
                  <TableCell>{sensor.name}</TableCell>
                  <TableCell>{sensor.serialNumber}</TableCell>
                  <TableCell>
                    <Button
                      variant="outlined"
                      color="primary"
                      onClick={() => handleViewInKibana(sensor.serialNumber)}
                    >
                      View in Kibana
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        ) : (
          fetchAttempted && (
            <Alert severity="info" style={{ marginTop: 20 }}>
              No sensors found for the selected device.
            </Alert>
          )
        )}
      </TableContainer>
    </div>
  );
};

export default UserDeviceSensors;
