import { useState, useEffect, useCallback } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  TextField,
  Alert,
} from "@mui/material";
import { BaseEntity } from "../client/Types";

export interface Column<T, K> {
  key: keyof T;
  header: string;
  editable?: boolean;
  renderCell?: (value: any) => React.ReactNode;
  renderEditCell?: (value: any, onChange: (newValue: any) => void) => React.ReactNode;


  sendFullItem?: boolean;
}

interface ManagerProps<T extends BaseEntity> {
  managedItemName: string;
  columns: Column<T, any>[];
  fetchItems: () => Promise<T[]>;
  createItem: (item: Partial<T>) => Promise<void>;
  deleteItem: (id: number | string) => Promise<void>;
  editItem?: (id: number | string, updatedItem: Partial<T>) => Promise<void>;
  FormComponent: React.ComponentType<{ onSubmit: (item: Partial<T>) => void }>;
  itemTransform?: (item: any) => T;
}

export function mapFieldToId<T extends BaseEntity>(field: keyof T, item: T) : T {
  return {
    ...item,
    id: item[field]
  }
}

const ManagerBase = <T extends BaseEntity>({
  managedItemName,
  columns,
  fetchItems,
  createItem,
  deleteItem,
  editItem,
  FormComponent,
  itemTransform,
}: ManagerProps<T>) => {
  const [items, setItems] = useState<T[]>([]);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [editingItem, setEditingItem] = useState<T | null>(null);

  const fetchData = useCallback(async () => {
    try {
      const response = await fetchItems();
      const mappedItems = itemTransform ? response.map(itemTransform) : response;
      setItems(mappedItems);
    } catch (error) {
      setErrorMessage("Failed to fetch items.");
      console.error(error);
    }
  }, [fetchItems, itemTransform]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleCreate = async (item: Partial<T>) => {
    try {
      await createItem(item);
      fetchData();
    } catch (error) {
      setErrorMessage("Failed to create item.");
      console.error(error);
    }
  };

  const handleDelete = async (id: number | string) => {
    try {
      await deleteItem(id);
      fetchData();
    } catch (error) {
      setErrorMessage("Failed to delete item.");
      console.error(error);
    }
  };

  const handleEdit = (item: T) => {
    setEditingItem(item);
  };

  const handleSaveEdit = async (id: number | string, updatedItem: Partial<T>) => {
    if (!editItem) return;
    try {
      await editItem(id, updatedItem);
      fetchData();
      setEditingItem(null);
    } catch (error) {
      setErrorMessage("Failed to update item.");
      console.error(error);
    }
  };

  return (
    <div className="p-4">
      <h2 className="text-2xl mb-4 text-left">{managedItemName} Manager</h2>
      <FormComponent onSubmit={handleCreate} />
      {errorMessage && (
        <div className="flex mb-4">
          <Alert severity="error">
            {errorMessage}
            <Button onClick={() => setErrorMessage(null)} className="text-white p-2 rounded ml-4">
              Close
            </Button>
          </Alert>
        </div>
      )}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              {columns.map((column) => (
                <TableCell key={column.key as string}>{column.header}</TableCell>
              ))}
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {items.map((item) => (
              <TableRow key={item.id}>
                {columns.map((column) => (
                  <TableCell key={column.key as string}>
                    {editingItem && editingItem.id === item.id && column.editable
                      ? column.renderEditCell
                        ? column.renderEditCell(
                            editingItem[column.key],
                            (newValue) => setEditingItem({ ...editingItem, [column.key]: newValue })
                          )
                        : (
                          <TextField
                            value={editingItem[column.key] as string}
                            onChange={(e) =>
                              setEditingItem({ ...editingItem, [column.key]: e.target.value })
                            }
                          />
                        )
                      : column.renderCell 
                        ? column.renderCell(column.sendFullItem
                            ? item
                            : item[column.key])
                        : item[column.key] as string}
                  </TableCell>
                ))}
                <TableCell>
                  {editingItem && editingItem.id === item.id ? (
                    <div className="flex space-x-2">
                      <Button
                        variant="contained"
                        color="success"
                        onClick={() => handleSaveEdit(item.id, editingItem)}
                      >
                        Save
                      </Button>
                      <Button
                        variant="contained"
                        color="error"
                        onClick={() => setEditingItem(null)}
                      >
                        Cancel
                      </Button>
                    </div>
                  ) : (
                    <div className="flex space-x-2">
                      <Button variant="contained" color="primary" onClick={() => handleEdit(item)}>
                        Edit
                      </Button>
                      <Button
                        variant="contained"
                        color="error"
                        onClick={() => handleDelete(item.id)}
                      >
                        Delete
                      </Button>
                    </div>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default ManagerBase;
