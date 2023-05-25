package org.antonioxocoy.cecommerce.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.util.HashSet;
import java.util.Set;

@TestConfiguration
@TestPropertySource(properties = {
        "amazon.endpoint.url=http://localhost:8000/",
        "amazon.access.key=test1",
        "amazon.access.secret-key=test231" })
public class LocalDBTesting {
    @Autowired
    protected AmazonDynamoDB amazonDynamoDB;

    protected static Set<Class> classes = new HashSet<>();

    @PostConstruct
    public void init() {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        if (classes != null && !classes.isEmpty()) {
            for (Class classe: classes) {
                CreateTableRequest tableUserRequest = dynamoDBMapper
                        .generateCreateTableRequest(classe);
                tableUserRequest.setProvisionedThroughput(
                        new ProvisionedThroughput(1L, 1L));
                TableUtils.createTableIfNotExists(amazonDynamoDB, tableUserRequest);
            }
        }
    }

    @AfterTestClass
    public void afterAll() {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        if (classes != null && !classes.isEmpty()) {
            for (Class classe: classes) {
                DeleteTableRequest tableUserRequest = dynamoDBMapper
                        .generateDeleteTableRequest(classe);
                amazonDynamoDB.deleteTable(tableUserRequest);
            }
        }
        classes = null;
    }
}
