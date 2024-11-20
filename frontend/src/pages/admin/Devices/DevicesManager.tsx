import { DeviceDto } from "../../../client/Types"
import ManagerBase, { Column, mapFieldToId } from "../../../components/ManagerBase"
import { createDevice, deleteDevice, editDevice, fetchDevices } from "../../../service/DeviceService"
import DeviceForm from "./DeviceForm"


const DevicesManager: React.FC = () => {
    const columns: Array<Column<DeviceDto, any>> = 
    [
        { key: 'modelName', header: 'Model', editable:true },
        { key: 'serialNumber', header: "Serial", editable:true }
    ]

    return (
        <ManagerBase
            managedItemName="Devices"
            columns={columns}
            fetchItems={fetchDevices}
            createItem={createDevice}
            deleteItem={deleteDevice}
            editItem={editDevice}
            FormComponent={DeviceForm}
            itemTransform={(item) => mapFieldToId('serialNumber', item)}/>
    )
}

export default DevicesManager;