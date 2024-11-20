/* eslint-disable */
/* tslint:disable */
/*
 * ---------------------------------------------------------------
 * ## THIS FILE WAS GENERATED VIA SWAGGER-TYPESCRIPT-API        ##
 * ##                                                           ##
 * ## AUTHOR: acacode                                           ##
 * ## SOURCE: https://github.com/acacode/swagger-typescript-api ##
 * ---------------------------------------------------------------
 */

export interface ChangePasswordRequest {
  /**
   * Old password of the user
   * @example "Password.12345678"
   */
  oldPassword?: string;
  /**
   * New password for the user
   * @example "Password.12345678!!"
   */
  newPassword?: string;
}

export interface NewPasswordRequest {
  /**
   * Generated token for the reset request
   * @example "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   */
  token?: string;
  /**
   * New password for the user
   * @example "password1234"
   */
  password?: string;
}

export interface SensorWithDeviceDto {
  /**
   * Sensor serial number
   * @example "DEADBEEF1234"
   */
  serialNumber?: string;
  /**
   * Sensor name
   * @example "Temperature sensor"
   */
  name: string;
  /**
   * Device serial number
   * @example "DEADBEEF"
   */
  deviceSerialNumber?: string;
}

export interface DeviceDto {
  /**
   * Device serial number
   * @example "DEADBEEF1234"
   */
  serialNumber?: string;
  /**
   * Device model name
   * @example "ESP32"
   */
  modelName: string;
}

export interface SensorWithDeviceResponseDto {
  serialNumber?: string;
  name?: string;
  deviceDto?: DeviceDto;
}

export interface CreateDeviceWithSensorSerialsDto {
  /**
   * Device serial number
   * @example "DEADBEEF1234"
   */
  serialNumber?: string;
  /**
   * API key hash
   * @example "oasdfgh9iwerht4391082"
   */
  apiKeyHash: string;
  /**
   * Device model name
   * @example "ESP32"
   */
  modelName: string;
  /**
   * Device sensors, if any.
   * @example ["DEADBEEF1234","DEADBEEF5678"]
   */
  sensors?: string[];
}

export interface DeviceWithSensorsDto {
  /**
   * Device serial number
   * @example "DEADBEEF1234"
   */
  serialNumber?: string;
  /**
   * Device model name
   * @example "ESP32"
   */
  modelName: string;
  /** Device sensors, if any. */
  sensors?: SensorDto[];
}

/** Device sensors, if any. */
export interface SensorDto {
  /**
   * Sensor name
   * @example "Temperature"
   */
  name: string;
  /**
   * Serial number of sensor
   * @example "DEADBEEF1234"
   */
  serialNumber?: string;
}

export interface ResetPasswordRequest {
  /**
   * Username
   * @example "User"
   */
  username?: string;
}

export interface DeviceMeasurementDto {
  /** List of measurements */
  measurements: SensorMeasurementDto[];
}

/** List of measurements */
export interface SensorMeasurementDto {
  /**
   * Sensor serial number
   * @example "DEADBEEF1234"
   */
  serialNumber?: string;
  /**
   * Measurement value
   * @format double
   * @example 25.5
   */
  value: number;
  /**
   * Timestamp of the measurement in ISO 8601 format
   * @format date-time
   * @example "2023-10-13T15:23:01Z"
   */
  timestamp: string;
}

export interface TokenToValidate {
  token?: string;
}

export interface AuthenticationResponse {
  /**
   * JWT token
   * @example "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   */
  token?: string;
  /**
   * Username
   * @example "admin"
   */
  username?: string;
  /**
   * User role
   * @example "ADMIN"
   */
  roles?: string[];
}

export interface RegisterRequest {
  /**
   * Username
   * @example "admin"
   */
  username: string;
  /**
   * Email
   * @example "admin@network.io"
   */
  email: string;
  /**
   * Password
   * @example "Password.12345678"
   */
  password: string;
}

export interface LoginRequest {
  /**
   * Username
   * @example "admin"
   */
  username: string;
  /**
   * Password
   * @example "password1234"
   */
  password: string;
}

export type QueryParamsType = Record<string | number, any>;
export type ResponseFormat = keyof Omit<Body, "body" | "bodyUsed">;

export interface FullRequestParams extends Omit<RequestInit, "body"> {
  /** set parameter to `true` for call `securityWorker` for this request */
  secure?: boolean;
  /** request path */
  path: string;
  /** content type of request body */
  type?: ContentType;
  /** query params */
  query?: QueryParamsType;
  /** format of response (i.e. response.json() -> format: "json") */
  format?: ResponseFormat;
  /** request body */
  body?: unknown;
  /** base url */
  baseUrl?: string;
  /** request cancellation token */
  cancelToken?: CancelToken;
}

export type RequestParams = Omit<FullRequestParams, "body" | "method" | "query" | "path">;

export interface ApiConfig<SecurityDataType = unknown> {
  baseUrl?: string;
  baseApiParams?: Omit<RequestParams, "baseUrl" | "cancelToken" | "signal">;
  securityWorker?: (securityData: SecurityDataType | null) => Promise<RequestParams | void> | RequestParams | void;
  customFetch?: typeof fetch;
}

export interface HttpResponse<D extends unknown, E extends unknown = unknown> extends Response {
  data: D;
  error: E;
}

type CancelToken = Symbol | string | number;

export enum ContentType {
  Json = "application/json",
  FormData = "multipart/form-data",
  UrlEncoded = "application/x-www-form-urlencoded",
  Text = "text/plain",
}

export class HttpClient<SecurityDataType = unknown> {
  public baseUrl: string = "http://localhost:8080/api";
  private securityData: SecurityDataType | null = null;
  private securityWorker?: ApiConfig<SecurityDataType>["securityWorker"];
  private abortControllers = new Map<CancelToken, AbortController>();
  private customFetch = (...fetchParams: Parameters<typeof fetch>) => fetch(...fetchParams);

  private baseApiParams: RequestParams = {
    credentials: "same-origin",
    headers: {},
    redirect: "follow",
    referrerPolicy: "no-referrer",
  };

  constructor(apiConfig: ApiConfig<SecurityDataType> = {}) {
    Object.assign(this, apiConfig);
  }

  public setSecurityData = (data: SecurityDataType | null) => {
    this.securityData = data;
  };

  protected encodeQueryParam(key: string, value: any) {
    const encodedKey = encodeURIComponent(key);
    return `${encodedKey}=${encodeURIComponent(typeof value === "number" ? value : `${value}`)}`;
  }

  protected addQueryParam(query: QueryParamsType, key: string) {
    return this.encodeQueryParam(key, query[key]);
  }

  protected addArrayQueryParam(query: QueryParamsType, key: string) {
    const value = query[key];
    return value.map((v: any) => this.encodeQueryParam(key, v)).join("&");
  }

  protected toQueryString(rawQuery?: QueryParamsType): string {
    const query = rawQuery || {};
    const keys = Object.keys(query).filter((key) => "undefined" !== typeof query[key]);
    return keys
      .map((key) => (Array.isArray(query[key]) ? this.addArrayQueryParam(query, key) : this.addQueryParam(query, key)))
      .join("&");
  }

  protected addQueryParams(rawQuery?: QueryParamsType): string {
    const queryString = this.toQueryString(rawQuery);
    return queryString ? `?${queryString}` : "";
  }

  private contentFormatters: Record<ContentType, (input: any) => any> = {
    [ContentType.Json]: (input: any) =>
      input !== null && (typeof input === "object" || typeof input === "string") ? JSON.stringify(input) : input,
    [ContentType.Text]: (input: any) => (input !== null && typeof input !== "string" ? JSON.stringify(input) : input),
    [ContentType.FormData]: (input: any) =>
      Object.keys(input || {}).reduce((formData, key) => {
        const property = input[key];
        formData.append(
          key,
          property instanceof Blob
            ? property
            : typeof property === "object" && property !== null
              ? JSON.stringify(property)
              : `${property}`,
        );
        return formData;
      }, new FormData()),
    [ContentType.UrlEncoded]: (input: any) => this.toQueryString(input),
  };

  protected mergeRequestParams(params1: RequestParams, params2?: RequestParams): RequestParams {
    return {
      ...this.baseApiParams,
      ...params1,
      ...(params2 || {}),
      headers: {
        ...(this.baseApiParams.headers || {}),
        ...(params1.headers || {}),
        ...((params2 && params2.headers) || {}),
      },
    };
  }

  protected createAbortSignal = (cancelToken: CancelToken): AbortSignal | undefined => {
    if (this.abortControllers.has(cancelToken)) {
      const abortController = this.abortControllers.get(cancelToken);
      if (abortController) {
        return abortController.signal;
      }
      return void 0;
    }

    const abortController = new AbortController();
    this.abortControllers.set(cancelToken, abortController);
    return abortController.signal;
  };

  public abortRequest = (cancelToken: CancelToken) => {
    const abortController = this.abortControllers.get(cancelToken);

    if (abortController) {
      abortController.abort();
      this.abortControllers.delete(cancelToken);
    }
  };

  public request = async <T = any, E = any>({
    body,
    secure,
    path,
    type,
    query,
    format,
    baseUrl,
    cancelToken,
    ...params
  }: FullRequestParams): Promise<HttpResponse<T, E>> => {
    const secureParams =
      ((typeof secure === "boolean" ? secure : this.baseApiParams.secure) &&
        this.securityWorker &&
        (await this.securityWorker(this.securityData))) ||
      {};
    const requestParams = this.mergeRequestParams(params, secureParams);
    const queryString = query && this.toQueryString(query);
    const payloadFormatter = this.contentFormatters[type || ContentType.Json];
    const responseFormat = format || requestParams.format;

    return this.customFetch(`${baseUrl || this.baseUrl || ""}${path}${queryString ? `?${queryString}` : ""}`, {
      ...requestParams,
      headers: {
        ...(requestParams.headers || {}),
        ...(type && type !== ContentType.FormData ? { "Content-Type": type } : {}),
      },
      signal: (cancelToken ? this.createAbortSignal(cancelToken) : requestParams.signal) || null,
      body: typeof body === "undefined" || body === null ? null : payloadFormatter(body),
    }).then(async (response) => {
      const r = response.clone() as HttpResponse<T, E>;
      r.data = null as unknown as T;
      r.error = null as unknown as E;

      const data = !responseFormat
        ? r
        : await response[responseFormat]()
            .then((data) => {
              if (r.ok) {
                r.data = data;
              } else {
                r.error = data;
              }
              return r;
            })
            .catch((e) => {
              r.error = e;
              return r;
            });

      if (cancelToken) {
        this.abortControllers.delete(cancelToken);
      }

      if (!response.ok) throw data;
      return data;
    });
  };
}

/**
 * @title OpenAPI definition
 * @version v0
 * @baseUrl http://localhost:8080/api
 */
export class Api<SecurityDataType extends unknown> extends HttpClient<SecurityDataType> {
  user = {
    /**
     * @description Change password for currently authenticated user.
     *
     * @tags User
     * @name ChangePassword
     * @summary Change password for current user
     * @request PUT:/user/me/change-password
     * @secure
     */
    changePassword: (data: ChangePasswordRequest, params: RequestParams = {}) =>
      this.request<object, object>({
        path: `/user/me/change-password`,
        method: "PUT",
        body: data,
        secure: true,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * @description Change password for user with provided token.
     *
     * @tags User
     * @name ChangePasswordViaToken
     * @summary Change password via token
     * @request PUT:/user/change-password-token
     */
    changePasswordViaToken: (data: NewPasswordRequest, params: RequestParams = {}) =>
      this.request<object, object>({
        path: `/user/change-password-token`,
        method: "PUT",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * @description Reset password for user with provided username.
     *
     * @tags User
     * @name RequestPasswordReset
     * @summary Reset password for user
     * @request POST:/user/password-reset-request
     */
    requestPasswordReset: (data: ResetPasswordRequest, params: RequestParams = {}) =>
      this.request<object, object>({
        path: `/user/password-reset-request`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),
  };
  sensors = {
    /**
     * @description Returns sensor with specified serial number
     *
     * @tags Sensors
     * @name GetOne
     * @summary Get sensor by serial number
     * @request GET:/sensors/{serialNumber}
     */
    getOne: (serialNumber: string, params: RequestParams = {}) =>
      this.request<SensorDto, object>({
        path: `/sensors/${serialNumber}`,
        method: "GET",
        ...params,
      }),

    /**
     * @description Updates sensor with specified serial number
     *
     * @tags Sensors
     * @name Update
     * @summary Update sensor
     * @request PUT:/sensors/{serialNumber}
     */
    update: (serialNumber: string, data: SensorWithDeviceDto, params: RequestParams = {}) =>
      this.request<SensorWithDeviceResponseDto, object>({
        path: `/sensors/${serialNumber}`,
        method: "PUT",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * @description Deletes sensor with specified serial number
     *
     * @tags Sensors
     * @name Delete
     * @summary Delete sensor
     * @request DELETE:/sensors/{serialNumber}
     */
    delete: (serialNumber: string, params: RequestParams = {}) =>
      this.request<object, object>({
        path: `/sensors/${serialNumber}`,
        method: "DELETE",
        ...params,
      }),

    /**
     * @description Returns all registered sensors
     *
     * @tags Sensors
     * @name GetAll
     * @summary Get all sensors
     * @request GET:/sensors
     */
    getAll: (params: RequestParams = {}) =>
      this.request<SensorDto[], any>({
        path: `/sensors`,
        method: "GET",
        ...params,
      }),

    /**
     * @description Creates a new sensor
     *
     * @tags Sensors
     * @name Create
     * @summary Create sensor
     * @request POST:/sensors
     */
    create: (data: SensorWithDeviceDto, params: RequestParams = {}) =>
      this.request<SensorWithDeviceResponseDto, object>({
        path: `/sensors`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),
  };
  devices = {
    /**
     * @description Returns a device by its serial number
     *
     * @tags Devices
     * @name GetOne1
     * @summary Get a device by serial number
     * @request GET:/devices/{serialNumber}
     */
    getOne1: (
      serialNumber: string,
      query?: {
        /**
         * Include sensors in the response
         * @default false
         */
        withSensors?: boolean;
      },
      params: RequestParams = {},
    ) =>
      this.request<DeviceWithSensorsDto | DeviceDto, object>({
        path: `/devices/${serialNumber}`,
        method: "GET",
        query: query,
        ...params,
      }),

    /**
     * No description
     *
     * @tags Devices
     * @name Update1
     * @summary Update a device
     * @request PUT:/devices/{serialNumber}
     */
    update1: (serialNumber: string, data: CreateDeviceWithSensorSerialsDto, params: RequestParams = {}) =>
      this.request<DeviceWithSensorsDto, object>({
        path: `/devices/${serialNumber}`,
        method: "PUT",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags Devices
     * @name Delete1
     * @summary Delete a device
     * @request DELETE:/devices/{serialNumber}
     */
    delete1: (serialNumber: string, params: RequestParams = {}) =>
      this.request<object, object>({
        path: `/devices/${serialNumber}`,
        method: "DELETE",
        ...params,
      }),

    /**
     * @description Returns all registered devices
     *
     * @tags Devices
     * @name GetAll1
     * @summary Get all devices
     * @request GET:/devices
     */
    getAll1: (
      query?: {
        /**
         * Include sensors in the response
         * @default false
         */
        withSensors?: boolean;
      },
      params: RequestParams = {},
    ) =>
      this.request<DeviceWithSensorsDto[] | DeviceDto[], any>({
        path: `/devices`,
        method: "GET",
        query: query,
        ...params,
      }),

    /**
     * No description
     *
     * @tags Devices
     * @name Create1
     * @summary Create a new device
     * @request POST:/devices
     */
    create1: (data: CreateDeviceWithSensorSerialsDto, params: RequestParams = {}) =>
      this.request<DeviceWithSensorsDto, object>({
        path: `/devices`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags Measurements
     * @name Ingest
     * @summary Ingest sensor measurements sent by a specific device
     * @request POST:/devices/measurements
     */
    ingest: (
      query: {
        /** Derived ID header, used to identify the device */
        "X-DERIVED-ID": string;
        /** HMAC signature header, used to verify the authenticity of the data */
        "X-HMAC-SIG": string;
      },
      data: DeviceMeasurementDto,
      params: RequestParams = {},
    ) =>
      this.request<object, object>({
        path: `/devices/measurements`,
        method: "POST",
        query: query,
        body: data,
        type: ContentType.Json,
        ...params,
      }),
  };
  username = {
    /**
     * @description Assign device to user by serial number.
     *
     * @tags User Devices
     * @name AssignDeviceToUser
     * @summary Assign device to user
     * @request POST:/{username}/devices/{serialNumber}
     */
    assignDeviceToUser: (username: string, serialNumber: string, params: RequestParams = {}) =>
      this.request<DeviceDto, object>({
        path: `/${username}/devices/${serialNumber}`,
        method: "POST",
        ...params,
      }),

    /**
     * @description Unassign device from user by serial number.
     *
     * @tags User Devices
     * @name UnassignDeviceFromUser
     * @summary Unassign device from user
     * @request DELETE:/{username}/devices/{serialNumber}
     */
    unassignDeviceFromUser: (username: string, serialNumber: string, params: RequestParams = {}) =>
      this.request<object, object>({
        path: `/${username}/devices/${serialNumber}`,
        method: "DELETE",
        ...params,
      }),
  };
  auth = {
    /**
     * No description
     *
     * @tags Auth
     * @name ValidateToken
     * @request POST:/auth/validate-token
     */
    validateToken: (data: TokenToValidate, params: RequestParams = {}) =>
      this.request<AuthenticationResponse, any>({
        path: `/auth/validate-token`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * @description Register user with provided credentials, return JWT token on successful registration.
     *
     * @tags Auth
     * @name Register
     * @summary Register user
     * @request POST:/auth/register
     */
    register: (data: RegisterRequest, params: RequestParams = {}) =>
      this.request<AuthenticationResponse, object>({
        path: `/auth/register`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * @description Validate user credentials and return a JWT token on successful authentication.
     *
     * @tags Auth
     * @name Login
     * @summary Authenticate user
     * @request POST:/auth/login
     */
    login: (data: LoginRequest, params: RequestParams = {}) =>
      this.request<AuthenticationResponse, object>({
        path: `/auth/login`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),
  };
}
