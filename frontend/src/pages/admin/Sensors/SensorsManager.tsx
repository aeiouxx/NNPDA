import { SensorWithDeviceDto } from "../../../client/Types"
import SelectorCell from "../../../components/cells/SelectorCell"
import ManagerBase, { Column, mapFieldToId } from "../../../components/ManagerBase"
import { fetchDevices } from "../../../service/DeviceService"
import { createSensor, deleteSensor, editSensor, fetchSensors } from "../../../service/SensorsService"
import SensorsForm, { formatDeviceLabel, formatLabel } from "./SensorsForm"

const SensorsManager: React.FC = () => {
    const columns: Array<Column<SensorWithDeviceDto, any>> = 
    [
        { key: 'name', header: 'Model', editable:true },
        { key: 'serialNumber', header: "Serial", editable:true },
        { 
            key: 'deviceSerial',
            header: "Device", 
            editable:true, 
            renderEditCell: (value, onChange) => (
                <SelectorCell
                    value={value}
                    onChange={onChange}
                    fetchItems={fetchDevices}
                    label="Device"
                    transformLabel={formatDeviceLabel}
                    itemKey={"serialNumber"}
                    itemValue={"serialNumber"}/>
            ),
            renderCell: (value : SensorWithDeviceDto) => {
                return formatLabel(value.deviceSerial, value.deviceName);
            },
            sendFullItem: true
        },
    ]

    return (
        <ManagerBase
            managedItemName="Sensors"
            columns={columns}
            fetchItems={fetchSensors}
            createItem={createSensor}
            deleteItem={deleteSensor}
            editItem={editSensor}
            FormComponent={SensorsForm}
            itemTransform={(item) => mapFieldToId('serialNumber', item)}/>
    )
}

export default SensorsManager;