package com.group.libraryapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryAppApplicationTests {

	@Autowired
	JpaProperties jpaProperties;

	@Test
	void test() {
		System.out.println(jpaProperties.getProperties());
	}

	@Test
	void contextLoads() {
	}

}
