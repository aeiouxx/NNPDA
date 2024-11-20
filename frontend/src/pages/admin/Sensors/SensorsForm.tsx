import { z } from "zod";
import { CreateSensorDto, DeviceDto} from "../../../client/Types"
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { fetchDevices } from "../../../service/DeviceService";
import { mapFieldToId } from "../../../components/ManagerBase";
import { Button, CircularProgress, FormControl, FormHelperText, Grid, InputLabel, MenuItem, Select, TextField } from "@mui/material";

interface SensorFormProps {
    onSubmit: (sensor: Partial<CreateSensorDto>) => void;
}

const SensorSchema = z.object({
    serialNumber: z.string().min(1, "Serial number is required"),
    name: z.string().min(1, "Name is required"),
    deviceSerialNumber: z.string().min(1, "Device serial number is required")
});

export const formatDeviceLabel = (device: DeviceDto): string => {
  if (!device) return "-";
  return formatLabel(device.serialNumber, device.modelName);
};

export const formatLabel = (serialNumber: string, modelName: string): string => {
  if (!serialNumber && !modelName) return "-"; 
  const maxLength = 60; 

  const combined = `${serialNumber} - ${modelName}`;

  if (combined.length > maxLength) {
      const availableSpace = maxLength - serialNumber.length - 3;
      const truncatedName = modelName.slice(0, availableSpace) + '...';
      return `${serialNumber} - ${truncatedName}`;
  }

  return combined;
}

const SensorsForm : React.FC<SensorFormProps> = ( { onSubmit }) => {
  const { register, handleSubmit, control, formState : {errors, isSubmitting}, reset} = useForm<Partial<CreateSensorDto>>({
    resolver: zodResolver(SensorSchema),
  });
  const [devices, setDevices] = useState<DeviceDto[]>([]);

  useEffect(() => {
    const loadDevices = async () => {
        try {
            const devices = await fetchDevices();
            const mapped = devices.map(
                (device) => mapFieldToId('serialNumber', device)
            )
            setDevices(mapped);
        } catch (error) {
            console.error("Failed to fetch devices");
        }
    }
  
    loadDevices();
  }, [])

  const onSubmitHandler = async (data : Partial<CreateSensorDto>) => {
    console.log("Sensor creating submitting: ", JSON.stringify(data));
    await onSubmit(data);
    reset();
  }


  return (
    <form onSubmit={handleSubmit(onSubmitHandler)} className="mx-8 mb-8">
      <Grid container spacing={2}>
        {/* Serial Number Field */}
        <Grid item xs={12} sm={6}>
          <TextField
            label="Serial Number"
            {...register('serialNumber')}
            fullWidth
            margin="normal"
            error={!!errors.serialNumber}
            helperText={errors.serialNumber?.message}
            disabled={isSubmitting}
          />
        </Grid>

        {/* Name Field */}
        <Grid item xs={12} sm={6}>
          <TextField
            label="Name"
            {...register('name')}
            fullWidth
            margin="normal"
            error={!!errors.name}
            helperText={errors.name?.message}
            disabled={isSubmitting}
          />
        </Grid>

        {/* Device Serial Number Dropdown */}
        <Grid item xs={12}>
          <FormControl
            fullWidth
            margin="normal"
            error={!!errors.deviceSerialNumber}
            disabled={isSubmitting}
          >
            <InputLabel id="device-serial-label">Device</InputLabel>
            <Controller
              name="deviceSerialNumber"
              control={control}
              render={({ field }) => (
                <Select
                  labelId="device-serial-label"
                  {...field}
                  value={field.value || ''}
                  label="Device"
                >
                  {devices.map((device) => (
                    <MenuItem key={device.serialNumber} value={device.serialNumber}>
                      {formatDeviceLabel(device)}
                    </MenuItem>
                  ))}
                </Select>
              )}
            />
            {errors.deviceSerialNumber && (
              <FormHelperText>{errors.deviceSerialNumber.message}</FormHelperText>
            )}
          </FormControl>
        </Grid>

        <Grid item xs={12}>
          <Button type="submit" variant="contained" color="primary" disabled={isSubmitting}>
            {isSubmitting ? <CircularProgress size={24} /> : 'Submit'}
          </Button>
        </Grid>
      </Grid>
    </form>
  );
};

export default SensorsForm;