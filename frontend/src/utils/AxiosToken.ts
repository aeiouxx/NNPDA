import axios from "axios";
import config from "../config";
const protectedAxios = axios.create({
  baseURL: config.apiBaseUrl,
});
protectedAxios.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
},
(error) => {
  return Promise.reject(error);
});
export default protectedAxios;