package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public static class User {
		public UUID uuid;

		public User() {
		}

		public User(UUID uuid) {
			this.uuid = uuid;
		}
	}

	public static class Product {
		public UUID uuid;

		public Product() {
		}

		public Product(UUID uuid) {
			this.uuid = uuid;
		}
	}

	public static class UserProduct {
		public User user;
		public Product product;

		public UserProduct() {
		}

		public UserProduct(User user, Product product) {
			this.user = user;
			this.product = product;
		}
	}

	@RestController
	public static class TestController {

		// Force the user to set a limit if the media-type is application/json
		// Otherwise since a application/json collection cannot be streamed, the server will burn
		@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
		Flux<User> getUsersWithRequiredLimit(@RequestParam Integer limit) {
			return getUsers(limit);
		}

		@GetMapping(value = "/users", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
		Flux<User> getUsers(@RequestParam(required = false) Integer limit) {
			var users = Flux.fromStream(Stream.generate(() -> new User(UUID.randomUUID()))).log();
			if (limit != null) {
				users = users.take(limit);
			}
			return users;
		}

		@GetMapping(value = "/products", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
		Flux<Product> getProducts() {
			return Flux.fromStream(Stream.generate(() -> new Product(UUID.randomUUID())))
					.delayElements(Duration.ofMillis(100));
		}

		@GetMapping(value = "/userProducts", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
		Flux<UserProduct> getUserProducts() {
			var users = WebClient.create("localhost:8080/users").get()
					.accept(MediaType.APPLICATION_STREAM_JSON)
					.retrieve()
					.bodyToFlux(User.class);
			var products = WebClient.create("localhost:8080/products").get()
					.accept(MediaType.APPLICATION_STREAM_JSON)
					.retrieve()
					.bodyToFlux(Product.class);

			return users.zipWith(products)
					.map(zipped -> new UserProduct(zipped.getT1(), zipped.getT2()));
		}
	}



}

