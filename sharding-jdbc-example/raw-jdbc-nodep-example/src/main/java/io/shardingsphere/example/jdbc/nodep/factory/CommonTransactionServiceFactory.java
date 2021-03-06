/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shardingsphere.example.jdbc.nodep.factory;

import io.shardingsphere.example.jdbc.nodep.config.MasterSlaveConfiguration;
import io.shardingsphere.example.jdbc.nodep.config.ShardingDatabasesAndTablesConfigurationPrecise;
import io.shardingsphere.example.jdbc.nodep.config.ShardingDatabasesAndTablesConfigurationRange;
import io.shardingsphere.example.jdbc.nodep.config.ShardingDatabasesConfigurationPrecise;
import io.shardingsphere.example.jdbc.nodep.config.ShardingMasterSlaveConfigurationPrecise;
import io.shardingsphere.example.jdbc.nodep.config.ShardingTablesConfigurationPrecise;
import io.shardingsphere.example.jdbc.nodep.config.ShardingTablesConfigurationRange;
import io.shardingsphere.example.repository.api.service.CommonService;
import io.shardingsphere.example.repository.api.service.TransactionService;
import io.shardingsphere.example.repository.jdbc.repository.JDBCOrderItemTransactionRepositotyImpl;
import io.shardingsphere.example.repository.jdbc.repository.JDBCOrderTransactionRepositoryImpl;
import io.shardingsphere.example.repository.jdbc.service.RawPojoTransactionService;
import io.shardingsphere.example.type.ShardingType;

import javax.sql.DataSource;
import java.sql.SQLException;

public class CommonTransactionServiceFactory {
    
    public static TransactionService newInstance(final ShardingType shardingType) throws SQLException {
        switch (shardingType) {
            case SHARDING_DATABASES:
                return createTransactionService(new ShardingDatabasesConfigurationPrecise().getDataSource());
            case SHARDING_TABLES:
                return createTransactionService(new ShardingTablesConfigurationPrecise().getDataSource());
            case SHARDING_TABLES_RANGE:
                return createTransactionService(new ShardingTablesConfigurationRange().getDataSource());
            case SHARDING_DATABASES_AND_TABLES:
                return createTransactionService(new ShardingDatabasesAndTablesConfigurationPrecise().getDataSource());
            case SHARDING_DATABASES_AND_TABLES_RANGE:
                return createTransactionService(new ShardingDatabasesAndTablesConfigurationRange().getDataSource());
            case MASTER_SLAVE:
                return createTransactionService(new MasterSlaveConfiguration().getDataSource());
            case SHARDING_MASTER_SLAVE:
                return createTransactionService(new ShardingMasterSlaveConfigurationPrecise().getDataSource());
            default:
                throw new UnsupportedOperationException(shardingType.name());
        }
    }
    
    private static TransactionService createTransactionService(final DataSource dataSource) throws SQLException {
        return new RawPojoTransactionService(new JDBCOrderTransactionRepositoryImpl(dataSource), new JDBCOrderItemTransactionRepositotyImpl(dataSource), dataSource);
    }
}
