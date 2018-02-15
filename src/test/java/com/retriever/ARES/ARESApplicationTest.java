package com.retriever.ARES;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


import com.retriever.ARES.utils.QueryBuilderUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;

import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
public class ARESApplicationTest {

	@Test
	public void panicIfFails() {
		assertTrue(true);
		assertEquals(1, 1);
		assertNotEquals(1, 2);
	}
	@Test
	public void blah() {
		assertTrue(true);
	}
	@Test
	public void blah2() {
		String inputQuery = "hellström håkan OR håkan AND hellström OR haj AND banan";
		QueryBuilder query = QueryBuilderUtils.getRootQuery(inputQuery).get();

		assertTrue(true);

	}


}
