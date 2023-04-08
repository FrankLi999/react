export type Error = {
  code?: number;
  message?: string;
  params?: string[];
};

export type ErrorApi = {
  errors?: Error[];
};

export type PageError = {
  key?: string;
  label?: string;
  message?: string;
  func?: any;
};
