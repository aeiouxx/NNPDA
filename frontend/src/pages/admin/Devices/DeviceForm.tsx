import { useState } from "react";
import { Button, TextField } from "@mui/material";

export interface CreateDeviceDto {
  serialNumber: string;
  apiKeyHash: string;
  modelName: string;
}

interface DeviceFormProps {
  onSubmit: (item: Partial<CreateDeviceDto>) => void;
}

const DeviceForm: React.FC<DeviceFormProps> = ({ onSubmit }) => {
  const [serialNumber, setSerialNumber] = useState<string>("");
  const [apiKeyHash, setApiKeyHash] = useState<string>("");
  const [modelName, setModelName] = useState<string>("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (serialNumber.trim() && apiKeyHash.trim() && modelName.trim()) {
      onSubmit({ serialNumber, apiKeyHash, modelName });
      setSerialNumber("");
      setApiKeyHash("");
      setModelName("");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4 flex flex-col space-y-4">
      <TextField
        value={serialNumber}
        onChange={(e) => setSerialNumber(e.target.value)}
        placeholder="Serial Number"
        variant="outlined"
        label="Serial Number"
      />
      <TextField
        value={apiKeyHash}
        onChange={(e) => setApiKeyHash(e.target.value)}
        placeholder="API Key Hash"
        variant="outlined"
        label="API Key Hash"
      />
      <TextField
        value={modelName}
        onChange={(e) => setModelName(e.target.value)}
        placeholder="Model Name"
        variant="outlined"
        label="Model Name"
      />
      <Button
        type="submit"
        variant="contained"
        color="primary"
        disabled={
          !serialNumber.trim() || !apiKeyHash.trim() || !modelName.trim()
        }
      >
        Add Device
      </Button>
    </form>
  );
};

export default DeviceForm;
