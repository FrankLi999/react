import winston from "winston";
const { combine, timestamp, simple, json } = winston.format;

const logger = winston.createLogger({
  level: "info",
  format: combine(timestamp(), json()),
  transports: [
    // new winston.transports.File({ filename: "app.log"}),
    new winston.transports.Console({
        format: json(),
      })
  ],
});
export { logger };