package com.example.demo.OrderController;
import com.example.demo.Entity.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@RestController

public class OrderController {

    private WebClient client = WebClient.create("http://localhost:9999");

    @PostMapping("/ragsAddProduct")
    @CircuitBreaker(name = "ragsAddProductFallBack", fallbackMethod = "ragsAddProductFallBacks")
    public Mono<Product> addProduct(@RequestBody Product product) throws ConnectException {
        System.out.println("First Entered....");

            return client.post()
                    .uri("/ecommerce/addProduct")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(product), Product.class)
                    .retrieve()
                    .bodyToMono(Product.class);
    }

    public Mono<Product> ragsAddProductFallBacks(Product product, Exception e) {
        System.out.println("Inside fallback method callNsFallback,cause");
        return null;
    }

    @GetMapping("/ragsProducts")
    @CircuitBreaker(name = "ragsProductsFallBack", fallbackMethod = "ragsProductsFallBack")
    public Flux<Product> getAllProducts()  {
            return client.get()
                .uri("/ecommerce/products")
                .retrieve()
                .bodyToFlux(Product.class);
    }
    public Flux<Product> ragsProductsFallBack(Exception e) {
        System.out.println("Inside fallback method callNsFallback,cause");
        return null;
    }

    @GetMapping("/ragsProduct/{id}")
    @CircuitBreaker(name = "ragsGetSingleProductsFallBack", fallbackMethod = "ragsGetSingleProductsFallBack")

    public Mono<Product> retrieveSingleProduct(@PathVariable int id)  {
         return client.get()
                .uri("/ecommerce/product/"+ id)
                 .retrieve()
                 .bodyToMono(Product.class);
    }
    public Mono<Product> ragsGetSingleProductsFallBack(int id, Exception e) {
        System.out.println("Inside fallback method callNsFallback,cause");
        return null;
    }

    @PutMapping("/ragsUpdateProduct/{id}")
    @CircuitBreaker(name = "ragsUpdateSingleProductsFallBack", fallbackMethod = "ragsUpdateSingleProductsFallBack")
    public Mono<Product> updateProduct(@RequestBody Product product, @PathVariable int id)  {
        return client.put()
                .uri("/ecommerce/updateProduct/"+ id)
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Mono<Product> ragsUpdateSingleProductsFallBack(Product product, int id, Exception e) {
        System.out.println("Inside fallback method callNsFallback,cause");
        return null;
    }

    @DeleteMapping("/ragsDeleteProduct/{id}")
    @CircuitBreaker(name = "ragsDeleteProductFallBack", fallbackMethod = "ragsDeleteProductFallBack")
    public Mono<Void> deleteProduct(@PathVariable int id)  {
        return client.delete()
                .uri("/ecommerce/deleteProduct/"+ id)
                .retrieve()
                .bodyToMono(Void.class);
    }
    public Mono<Void> ragsDeleteProductFallBack(int id, Exception e) {
        System.out.println("Inside fallback method callNsFallback,cause");
        return null;
    }
}
