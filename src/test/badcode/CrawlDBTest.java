package test.badcode; 

import badcode.CrawlDB;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
* CrawlDB Tester.
*
* @author <Authors name>
* @since <pre>May 8, 2017</pre>
* @version 1.0
*/
public class CrawlDBTest {
    private CrawlDB crawlDB = new CrawlDB();

@Before
public void before() throws Exception {
    crawlDB.addDirtyURL("test_dirty");
    crawlDB.addCleanURL("test_clean");
    crawlDB.addCleanURL("test_clean_content", true);
    crawlDB.addCleanURL("test_clean_no_content", false);
}

@After
public void after() throws Exception {

}

/**
*
* Method: addDirtyURL(String url)
*
*/
@Test
public void testAddDirtyURL() throws Exception {
    assertTrue(crawlDB.addDirtyURL("test_clean") == 0);
}

/**
*
* Method: addCleanURL(String url)
*
*/
@Test
public void testAddCleanURLUrl() throws Exception {

}

/**
*
* Method: addCleanURL(String url, boolean hasContent)
*
*/
@Test
public void testAddCleanURLForUrlHasContent() throws Exception {

}

/**
*
* Method: getURL()
*
*/
@Test
public void testGetURL() throws Exception {
    assertEquals("test_dirty", crawlDB.getURL());
}

/**
*
* Method: getTTL(String url)
*
*/
@Test
public void testGetTTL() throws Exception {
    System.out.println(crawlDB.getTTL("test_clean"));
    System.out.println(crawlDB.getTTL("test_clean_content"));
    System.out.println(crawlDB.getTTL("test_clean_no_content"));
    assertEquals(0, crawlDB.getTTL("test_dirty"));
}

} 
