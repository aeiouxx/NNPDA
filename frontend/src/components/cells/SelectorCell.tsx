import { FormControl, FormControlProps, InputLabel, MenuItem, Select, SelectProps } from "@mui/material";
import { useEffect, useState } from "react";


interface SelectorProps<T> {
  value: any;
  onChange: (newValue: any) => void;
  fetchItems: () => Promise<T[]>;

  label: string;
  itemKey: keyof T;
  itemValue: keyof T;

  transformLabel?: (data: T) => string; 

  formControlProps?: FormControlProps;
  selectProps?: SelectProps;
}

const SelectorCell = <T,>(
  { 
    value,
    onChange,
    fetchItems,
    label,
    itemKey,
    itemValue,
    transformLabel,
    formControlProps, 
    selectProps,
  }: SelectorProps<T>) => {
    const [items, setItems] = useState<T[]>([]);

    useEffect(() => {
      const loadItems = async () => {
        try {
          const items = await fetchItems();
          setItems(items);
        } catch (error) {
          console.error('Failed to fetch items', error);
        }
      };
      loadItems();
    }, [fetchItems]);

    return (
      <FormControl
        margin="normal"
        {...formControlProps}>
          <InputLabel>
            {label}
          </InputLabel>
          <Select
            label={label}
            value={value}
            onChange={(e) => onChange(e.target.value as T)}
            {...selectProps}>
              {items.map((item) => (
                <MenuItem key={item[itemKey] as string} value={item[itemKey] as string}>
                    {transformLabel ? transformLabel(item) : (item[itemValue] as string)}
                </MenuItem>
              ))}
            </Select>
        </FormControl>
    );
};

export default SelectorCell;
  