import Redis from "ioredis";

const redis = new Redis({
    host: process.env['REDIS_HOST'],
    port:  process.env['REDIS_PORT'],
    password:  process.env['REDIS_PASSWORD'],
    db: 2
});

if (process.env.NODE_ENV === "development") {
    global.redis = redis;
}

export default redis;


// new Redis({
//     port: 6379, // Redis port
//     host: "127.0.0.1", // Redis host
//     username: "default", // needs Redis >= 6
//     password: "my-top-secret",
//     db: 0, // Defaults to 0
//   });