import * as oracleDBUtil from '@/utils/oracleDBUtil';
import * as oracledb from 'oracledb';
import { ApplicationProfile } from '@/dto/ApplicationProfile';
import { ConfigData } from '@/dto/ConfigData';
import { ConfigDataEntity } from './entity/ConfigDataEntity';
import { logger } from "@/logger";
export const findAll = async () => {
  let connection;
  try {
    logger.info('Oracle db init>>>>>>>>>>>>>>>>>');
    await oracleDBUtil.init();
    // Get a connection from the default pool
    logger.info('Get connection >>>>>');
    connection = await oracledb.getConnection();
    const sql = `SELECT p.APPLICATION, p.PROFILE, p.LABEL, p.PROP_KEY, p.PROP_VALUE FROM APP_PROPERTIES p ORDER BY p.APPLICATION asc, p.PROFILE asc, p.LABEL asc, p.PROP_KEY asc`;
    // const options = { outFormat: oracledb.OUT_FORMAT_OBJECT };
    logger.info('Execute statement >>>>>');
    const result = await connection.execute(sql, [], {resultSet: true});
    console.log(result.rows);
    // oracledb.getPool().logStatistics(); // show pool statistics.  pool.enableStatistics must be true
    // return result.rows as ConfigDataEntity[];
    const rs = result.resultSet;
    let row;
    let i = 1;
    const configurationModel = [];
    while ((row = await rs.getRow())) {
      logger.info("getRow(): row " + i++);
       const key = `${row[0]}-${row[1]}-${row[2]}`;
      let config = configurationModel.find(e => e.key === key);
      if (!config) {
        config = {
          key: key,
          application: row[0],
          profile: row[1],
          label: row[2],
          props: []
        };
        configurationModel.push(config);
    }
      config.props.push({
        propKey: row[3],
        propValue: row[4]
      });
    }
    // always close the ResultSet
    await rs.close();
    return configurationModel;
  } catch (err) {
    console.error(err);
  } finally {
    if (connection) {
      try {
        // Put the connection back in the pool
        await connection.close();
      } catch (err) {
        logger.error(err);
      }
    }
  }
}

export const findByApplication = async (application: string) => {
  let connection;
  try {
    console.log('Oracle db init>>>>>>>>>>>>>>>>>');
    await oracleDBUtil.init();
    // Get a connection from the default pool
    connection = await oracledb.getConnection();
    const sql = `SELECT p.APPLICATION, p.PROFILE, p.LABEL, p.PROP_KEY, p.PROP_VALUE FROM APP_PROPERTIES WHERE application = :application`;
    const binds = [application];
    const options = { 
      // outFormat: oracledb.OUT_FORMAT_OBJECT 
      resultSet: true
    };
    const result = await connection.execute(sql, binds, options);
    console.log(result.rows);
    // oracledb.getPool().logStatistics(); // show pool statistics.  pool.enableStatistics must be true
    // return result.rows as ConfigDataEntity[];
    const rs = result.resultSet;
    let row;
    let i = 1;
    const configurationModel = [];
    while ((row = await rs.getRow())) {
      console.log("getRow(): row " + i++);
       const key = `${row[0]}-${row[1]}-${row[2]}`;
      let config = configurationModel.find(e => e.key === key);
      if (!config) {
        config = {
          key: key,
          application: row[0],
          profile: row[1],
          label: row[2],
          props: []
        };
        configurationModel.push(config);
    }
      config.props.push({
        propKey: row[3],
        propValue: row[4]
      });
    }
    // always close the ResultSet
    await rs.close();
    console.log(configurationModel);
    return configurationModel;
  } catch (err) {
    console.error(err);
  } finally {
    if (connection) {
      try {
        // Put the connection back in the pool
        await connection.close();
      } catch (err) {
        console.error(err);
      }
    }
  }
}
export const deleteProfiles = async (configData: ConfigData[]) => {
  const sql = "DELETE FROM APP_PROPERTIES WHERE application = :application and profile = :profile";
  const options = {
    autoCommit: true,
    bindDefs: {
      application: { type: oracledb.STRING },
      profile: { type: oracledb.STRING}
    }
  };
  const binds: ApplicationProfile[] = [];
  configData.forEach(e => {
    binds.push({
      application: e.application,
      profile: e.profile
    });
  });
  
  console.log('Will delete >>>', binds);
  let connection = null;
  try {
    console.log('Oracle db init>>>>>>>>>>>>>>>>>');
    await oracleDBUtil.init();
    // Get a connection from the default pool
    console.log('Get connection >>>>>');
    connection = await oracledb.getConnection();
    await connection.executeMany(sql, binds, options);
  } catch (err) {
    console.error(err);
  } finally {
    if (connection) {
      try {
        // Put the connection back in the pool
        await connection.close();
      } catch (err) {
        console.error(err);
      }
    }
  }
}

export const saveAll = async (configData: ConfigData[]) => {
  const sql = "INSERT INTO APP_PROPERTIES(APPLICATION, PROFILE, LABEL, PROP_KEY, PROP_VALUE) values(:application, :profile, :label, :propKey, :propValue)";
  const options = {
    autoCommit: true,
    bindDefs: {
      application: { type: oracledb.STRING },
      profile: { type: oracledb.STRING}
    }
  };
  const binds: ConfigDataEntity[] = [];
  configData.forEach(config => {
    config.props.forEach(prop => {
      binds.push({
        application: config.application,
        profile: config.profile ,
        label: config.label,
        propKey: prop.propKey,
        propValue: prop.propValue
      });
    });
  });
  console.log('Will delete >>>', binds);
  let connection = null;
  try {
    console.log('Oracle db init>>>>>>>>>>>>>>>>>');
    await oracleDBUtil.init();
    // Get a connection from the default pool
    console.log('Get connection >>>>>');
    connection = await oracledb.getConnection();
    await connection.executeMany(sql, binds, options);
  } catch (err) {
    console.error(err);
  } finally {
    if (connection) {
      try {
        // Put the connection back in the pool
        await connection.close();
      } catch (err) {
        console.error(err);
      }
    }
  }
}