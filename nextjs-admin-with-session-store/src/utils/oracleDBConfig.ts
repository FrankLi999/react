const oracleDBConfig = {
    // user: process.env['NODE_ORACLEDB_USER'],
    // password: process.env['NODE_ORACLEDB_PASSWORD'],
    // connectString: process.env['NODE_ORACLEDB_CONNECTIONSTRING']
    user: 'C##Camel',
    password: 'Passw0rd',
    connectString: 'localhost:1521/FREE'
}

// if (process.env['NODE_ORACLEDB_DBA_USER']) {
//     oracleDBConfig.DBA_user = process.env['NODE_ORACLEDB_DBA_USER'];
// }

// if (process.env['NODE_ORACLEDB_DBA_PASSWORD']) {
//     oracleDBConfig.DBA_password = process.env['NODE_ORACLEDB_DBA_PASSWORD'];
// }

export default oracleDBConfig;
