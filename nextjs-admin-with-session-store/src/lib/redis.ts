import Redis, { RedisOptions } from "ioredis";

const redis = new Redis({
    host: process.env['REDIS_HOST'] as string, // 'localhost'
    port: Number(process.env['REDIS_PORT']), // 6379
    password: process.env['REDIS_PASSWORD']  as string,
    db: parseInt(process.env['REDIS_DATABASE'] as string)
} as RedisOptions);

if (process.env['NODE_ENV'] === "development") {
    (global as any).redis = redis;
}

export default redis;


// new Redis({
//     port: 6379, // Redis port
//     host: "127.0.0.1", // Redis host
//     username: "default", // needs Redis >= 6
//     password: "my-top-secret",
//     db: 0, // Defaults to 0
//   });
