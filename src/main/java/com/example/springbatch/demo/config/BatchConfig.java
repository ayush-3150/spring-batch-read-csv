package com.example.springbatch.demo.config;

// import javax.batch.api.chunk.ItemWriter;
import org.springframework.batch.item.ItemWriter;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.springbatch.demo.data.UserRepository;
import com.example.springbatch.demo.data.UserRepository;
import com.example.springbatch.demo.model.User;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired(required = true)
    private JobBuilderFactory jobBuilderFactory;

    @Autowired(required = true)
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    DataSource dataSource;

    @Bean
    public FlatFileItemReader<User> reader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("User.csv"));
        reader.setLineMapper(getLineMapper());
        reader.setLinesToSkip(1);
        // reader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());
        
        return reader;
    }

    private LineMapper<User> getLineMapper() {
        String[] FIELD_NAMES = { "country", "region", "population", "percentage", "totalUser" };
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<User>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        // lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames(FIELD_NAMES);

        BeanWrapperFieldSetMapper<User> fieldSetter = new BeanWrapperFieldSetMapper<>();

        fieldSetter.setTargetType(User.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetter);

        return lineMapper;
    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Autowired
    UserRepository repository;

    @Bean
    public ItemWriter<User> writer() {
        return User -> {
            System.out.println("Saving User Records: " + User);
            repository.saveAll(User);
        };
    }

    @Bean
    public JobExecutionListener listener() {
        return new UserListener();
    }

    // @Bean
    // @DependsOnDatabaseInitialization
    // public JdbcBatchItemWriter<User> writer() {
    // System.out.println("here item writer");
    // return new JdbcBatchItemWriterBuilder<User>()
    // .itemSqlParameterSourceProvider(new
    // BeanPropertyItemSqlParameterSourceProvider<>())
    // .sql("INSERT INTO User (id,brand, model, processor_brand,
    // processor_name,latest_price,discount,ratings, reviews)"
    // + "VALUES ( :id,:brand, :model, :processorBrand, :processorName,
    // :latestPrice, :discount, :ratings, :reviews)")
    // .dataSource(this.dataSource).build();
    // }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory
                .get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    // JdbcBatchItemWriter<User> writer
    public Step step1() {
        return stepBuilderFactory
                .get("step1")
                .<User, User>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
