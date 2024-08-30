/** Signature of a logging function */
export interface LogFn {
  (message?: any, ...optionalParams: any[]): void;
}

/** Basic logger interface */
export interface Logger {
  log: LogFn;
  warn: LogFn;
  error: LogFn;
}

export type LogLevel = 'log' | 'warn' | 'error';
export const NO_OP: LogFn = (message?: any, ...optionalParams: any[]) => {
  // TODO - for build
  if (message) {
    console.log(message, ...optionalParams);
  }
};

/** The App environment */
export type Environment = 'development' | 'production';
/*
 * yarn start  # Will show all logs
 * REACT_APP_APP_ENV='production' yarn start  # Will show only 'warn' and'error' logs
 * # Remember to build the production App with:
 * REACT_APP_APP_ENV='production' yarn build
 */
export const APP_ENV: Environment =
  process.env.REACT_APP_APP_ENV === 'production' ? 'production' : 'development';
export const LOG_LEVEL: LogLevel = APP_ENV === 'production' ? 'warn' : 'log';

/**
 * const logger = new ConsoleLogger({ level: 'warn' });
 */
export class ConsoleLogger implements Logger {
  readonly log: LogFn;
  readonly warn: LogFn;
  readonly error: LogFn;

  constructor(options?: { level?: LogLevel }) {
    const { level } = options || {};

    this.error = console.error.bind(console);
    this.warn = level === 'error' ? NO_OP : console.warn.bind(console);
    this.log = level === 'log' ? console.warn.bind(console) : NO_OP;
  }
}

export const logger = new ConsoleLogger({ level: LOG_LEVEL });
