package com.mjduan.project.chapter11_serviceDiscovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 * Created by Duan on 2017/4/6.
 */
public class Example1 extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Example1.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(super.vertx,
                new ServiceDiscoveryOptions()
                        .setAnnounceAddress("service-announce")
                        .setName("my-name"));
        Record record1 = new Record()
                .setType("eventbus-service-proxy")
                .setLocation(new JsonObject().put("endpoint", "the service-address"))
                .setName("my-service")
                .setMetadata(new JsonObject().put("some-label", "some-value"));

        discovery.publish(record1, recordAsyncResult -> {
            if (recordAsyncResult.failed()) {
                recordAsyncResult.cause().printStackTrace();
                return;
            }
            System.out.println("\"" + record1.getName() + "\" successfully");
            Record publishedRecord = recordAsyncResult.result();
        });

        //--------------------------------------------
        Record record2 = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
        discovery.publish(record2, recordAsyncResult -> {
            if (recordAsyncResult.failed()) {
                recordAsyncResult.cause().printStackTrace();
                return;
            }
            System.out.println("\"" + record2.getName() + "\" published");
            Record publishedRecord = recordAsyncResult.result();
        });

        discovery.unpublish(record1.getRegistration(), handler -> {
            if (handler.failed()) {
                handler.cause().printStackTrace();
                return;
            }
            System.out.println("\"" + record1.getName() + "\" unpublish successfully");
        });


        discovery.getRecord(record -> record.getName().equals(record2.getName()),
                handler -> {
                    if (handler.failed()) {
                        handler.cause().printStackTrace();
                        return;
                    }
                    if (handler.result() == null) {
                        System.out.println("rec null");
                        return;
                    }

                    ServiceReference reference = discovery.getReference(handler.result());
                    HttpClient httpClient = reference.get();
                    httpClient.getNow(8080, "localhost", "/api", gethandler -> {
                        System.out.println("get response");
                        reference.release();
                    });
                    httpClient.getNow("/api", httpClientResponse -> {
                        System.out.println("get response");
                        reference.release();
                    });

                });
    }
}
