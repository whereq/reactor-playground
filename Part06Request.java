package io.pivotal.literx;

//generic imports to help with simpler IDEs (ie tech.io)
import java.util.*;
import java.util.function.*;
import java.time.*;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to control the demand.
 *
 * @see <a href="https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Request">Request Question Desc</a>
 */
public class Part06Request {

	ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

	// TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
	StepVerifier requestAllExpectFour(Flux<User> flux) {
		return StepVerifier
                .create(flux)
				.expectSubscription()
				.thenRequest(Long.MAX_VALUE)
				.expectNextCount(4)
				.expectComplete();
	}

//========================================================================================

	// TODO Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE then stops verifying by cancelling the source
	StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
		return StepVerifier
                .create(flux)
				.expectSubscription()
				.thenRequest(1L)
				.expectNext(User.SKYLER)
				.thenRequest(1L)
                .expectNext(User.JESSE)
                .thenCancel();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
	Flux<User> fluxWithLog() {
		return repository.findAll().log();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints "Starring:" on subscribe, "firstname lastname" for all values and "The end!" on complete
	Flux<User> fluxWithDoOnPrintln() {
		return repository
			.findAll()
			.doOnSubscribe(s -> System.out.println("Starring:"))
			.doOnNext(u -> System.out.println(u.getFirstname() + " " + u.getLastname()))
			.doOnComplete(() -> System.out.println("The end!"));
	}

}
