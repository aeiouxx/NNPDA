import axios from "axios";
import config from "../config";
const protectedAxios = axios.create({
  baseURL: config.apiBaseUrl,
});
protectedAxios.interceptors.request.use((config) => {
  const token = localStorage.getItem("jwtToken");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  console.log("Intercepting request to", config.url);
  console.log("Intercepting config:", JSON.stringify(config.headers));
  return config;
},
(error) => {
  return Promise.reject(error);
});
export default protectedAxios;