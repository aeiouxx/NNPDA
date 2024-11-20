import { AxiosError } from "axios";
export interface ValidationErrorResponse {
  [key: string]: string;
}
export type CustomError = AxiosError<ValidationErrorResponse> | Error;