import log4js from 'log4js';
export default function log4jsInit() {
    log4js.addLayout('json', function(config) {
      return function(logEvent) { return JSON.stringify(logEvent) + config.separator; }
    });
    log4js.configure({
        appenders: {
            // out: { type: "stdout" },
            // app: { type: "file", filename: "application.log" },
            out: { type: 'stdout', layout: { type: 'json', separator: ',' }}
        },
        categories: {
            // default: { appenders: ["out", "app"], level: "debug" },
            default: { appenders: ["out"], level: "debug" },
        },
    });
}