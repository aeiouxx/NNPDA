// import { CancelablePromise, OpenAPIConfig } from "../Api";
// import { ApiRequestOptions } from "../api/core/ApiRequestOptions";
// import protectedAxios from "./AxiosToken";

// /**
//  * Custom request function
//  * @param config OpenAPI configuration object
//  * @param options API request options
//  * @returns CancelablePromise
//  */
// export const customRequest = <T>(
//     config: OpenAPIConfig,
//     options: ApiRequestOptions
// ): CancelablePromise<T> => {
//     return new CancelablePromise(async (resolve, reject, onCancel) => {
//         try {
//             const url = options.url; // Use the options to construct the URL
//             const method = options.method;
//             const headers = options.headers || {};
//             const data = options.body || null;

//             const response = await protectedAxios.request<T>({
//                 url,
//                 method,
//                 headers,
//                 data,
//             });

//             resolve(response.data);
//         } catch (error) {
//             reject(error);
//         }
//     });
// };
