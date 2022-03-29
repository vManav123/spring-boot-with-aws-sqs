# Spring Boot with AWS SQS Messaging

## Introduction
Amazon Simple Queue Service (SQS) is a fully managed message queuing service that enables you to decouple and scale your applications. It is a fully managed service, so you will not have to take care of managing the service yourself. You can create queues, send and receive messages, send messages to a Dead Letter Queue when they could not be processed successfully, etc.

## Setup
Before you can get started, you need to have an AWS account and you need to do some configuration on your local machine in order to be able to use the AWS SDK.

In order to use the AWS SDK for Java, it is necessary to add the BOM (Bill of Materials) of the AWS SDK to the dependencyManagement section of the pom file.
```javascript
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
In order to be able to use the SQS functionality, you only need to add the sqs dependency to the dependencies section
```javascript
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-aws-messaging</artifactId>
            <version>2.2.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>io.awspring.cloud</groupId>
            <artifactId>spring-cloud-starter-aws</artifactId>
            <version>2.3.2</version>
        </dependency>
```

## Configuration
```javascript
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;


    @Autowired
    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(@Qualifier(value="amazonSQSAsync") AmazonSQSAsync amazonSQSAsync)
    {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Primary
    @Bean(name = "amazonSQSAsync")
    public AmazonSQSAsync amazonSQSAsync()
    {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey,secretKey)
                ))
                .build();
    }
```
