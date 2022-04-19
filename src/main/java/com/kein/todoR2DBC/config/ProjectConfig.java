package com.kein.todoR2DBC.config;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.*;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.function.BiFunction;
import java.util.function.Function;

@Configuration
@EnableTransactionManagement
public class ProjectConfig {

    ConnectionFactory connectionFactory() {
        return MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                .host("127.0.0.1")
                .port(3306)
                .username("root")
                .password("Kien17101998")
                .database("backend-demo")
                .build());
    }
    @Bean
    public static ConnectionFactory getFactory(){

        ConnectionFactoryOptions options =
                ConnectionFactoryOptions.builder()
                        .option(ConnectionFactoryOptions.DRIVER, "oracle")
                        .option(ConnectionFactoryOptions.USER, "devPilo")
                        .option(ConnectionFactoryOptions.PASSWORD,"dev83plo")
                        .option(ConnectionFactoryOptions.HOST, "125.212.192.150")
                        .option(ConnectionFactoryOptions.PORT, 1521)
                        .option(ConnectionFactoryOptions.DATABASE, "aladin123")
                        .build();

        return  ConnectionFactories.get(options);
    }

    public static void main(String[] args) {
        ConnectionFactory factory= getFactory();
        Publisher<Connection> connection = (Publisher<Connection>) factory.create();
        Flux.from(connection).flatMap(
                conn ->Flux.from(
                        conn.createStatement("Select * from task")
                            .execute()  //return Publisher<? extends Result>
                        )
                        .flatMap(result-> // Result Obj, flatMap to change Result stream to String stream
                                result.map(
                                            (row,metadata)->{

                                                 String name = row.get(0,String.class);
                                                 String status = row.get(1,String.class);

                                                return String.format(" Name: %s, Status: %s",name,status);
                                                }
                                            )
                                )
        ).doOnNext(string -> System.err.println(string)).subscribe();
//        Flux.from(connection).flatMap(
//                conn-> Flux.from(
//                        conn.createStatement("Insert into task(name,status) values(?,?)")
//                            .bind(0,"kienpham").bind(1,"completed") //return Statement;
//                                .execute() //return Publisher<? extend Result>,dua vao Flux de xu ly;
//                            )
//                            .flatMap(result        //Result obj has: map(BiFunction<row,metadata,T> bi) va getRowsUpdated()
//                                    -> result.getRowsUpdated()
//                            )
//                )
//                .subscribe(row -> System.out.println("RowsUpdated: "+row));
        ReactiveTransactionManager tm = new R2dbcTransactionManager(factory);
        DatabaseClient db = DatabaseClient.create(factory);
        TransactionalOperator rxtx = TransactionalOperator.create(tm);

        rxtx.transactional(db.sql("update task set name='pham van a' where id = 61")
                .fetch().rowsUpdated()
                .then(db.sql("update task set name='add some task' where id = 44")
                        .fetch().rowsUpdated().doOnError(err -> System.err.println("loi"))
                        .then())).subscribe();


//        db.sql("update task set name='pham van a' where id = 61")
//                .fetch().rowsUpdated()
//                .then(db
//                        .sql("update task set name='add some task 1' where id = 44")
//                        .fetch().rowsUpdated().doOnError(err -> System.err.println("loi"))
//                        .then())
//                .as(thisMono -> rxtx.transactional(thisMono)).subscribe();

        try {
            Thread.sleep(60*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
